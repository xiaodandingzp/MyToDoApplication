package com.example.mytodoapplication.coroutine

import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.sync.withPermit

object CoroutineObject {

    const val TAG = "CoroutineObject"

    fun testEntrance() {
        testSelectChannel()
    }

    fun testSelectChannel() = runBlocking {
        val test1 = async {
            "string111"
        }
        val test2 = async {
            "string222"
        }

        listOf(test1, test2)
            .map {
                flow {
                    emit(it.await())
                }
            }
            .merge()
            .collect {
                Log.i(TAG, "it:$it")
            }
    }

    fun testSafeSemaphore() = runBlocking {
        var count = 0
        val semaphore = Semaphore(1)
        List(1000) {
            GlobalScope.launch {
                semaphore.withPermit {
                    count++
                }
            }
        }.joinAll()
        Log.i(TAG, "count:$count")
    }

    fun testSafeMutex() = runBlocking {
        var count = 0
        val mutex = Mutex()
        List(1000) {
            GlobalScope.launch {
                mutex.withLock {
                    count++
                }
            }
        }.joinAll()
        Log.i(TAG, "count:$count")
    }

    fun testSelect() = runBlocking {
        val coroutine1 = async<Int> {
            delay(500)
            Log.i(TAG, "coroutine1")
            111
        }
        val coroutine2 = async<String> {
            Log.i(TAG, "coroutine2")
            "zpppp2"
        }
        val result = select<String> {
            coroutine1.onAwait {
                it.toString()
            }
            coroutine2.onAwait {
                it
            }
        }
        Log.i(TAG, "result: $result")
    }

    fun testChannel() {
        val channel = Channel<Int>(5)
        GlobalScope.launch {
            List(3) {
                channel.send(it)
            }
        }

        GlobalScope.launch {
            while (true) {
                val dd = channel.receive()
                Log.i(TAG, "dd: $dd")
            }
        }
    }
}