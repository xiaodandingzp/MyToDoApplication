package com.example.mytodoapplication.coroutine

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.mytodoapplication.R
import kotlinx.coroutines.*

/*
* launch 启动新的协程
* 当使用join函数时，会挂起函数直到结果执行结束
* 不会阻塞直到结果返回  不会阻塞线程  并行执行
* withContext: 挂起协程 没有启动新的
* 会阻塞当前协程直到函数返回
* 从指定的Dispatcher执行函数
* 当执行函数的时候不会阻塞线程
* 串行执行
* async 启动新的协程
* 当使用awiat函数时，会阻塞直到结果返回
* 如果不使用await，其效果与launch一样
* 适用于多个并行任务但需要等待结果返回情形
* 并行执行
*
*
* 当async作为根协程或者使用supervisorScope时，被封装到deferred对象中的异常才会在调用await时抛出。
* 如果async作为一个子协程时，那么异常并不会等到调用await时抛出，而是立刻抛出异常。

* 这也就是为什么我们明明在await的地方进行try catch，但是程序仍然会崩溃的原因。
* 因为被立刻抛出的异常没有被得到处理，所以只能崩溃了。
*
* launch作为子协程，异常是立即被抛出的。
 */

class CorotineActivity : AppCompatActivity() {

    companion object {
        const val TAG = "CorotineActivity"
    }

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_corotine)
        findViewById<Button>(R.id.button_corotine_test).setOnClickListener {
            Thread {
                Log.i(TAG, "thread000:${Thread.currentThread().name}")
                GlobalScope.launch {
                    delay(3000)
                    Log.i(TAG, "thread111:${Thread.currentThread().name}")
                    Log.i(TAG, "1111111")
                }
                Log.i(TAG, "222222")
            }.start()
            Log.i(TAG, "thread333:${Thread.currentThread().name}")
            Log.i(TAG, "3333333333")
        }
    }

    private fun testAsync() {
        GlobalScope.launch {
            val test1 = async {
                delay(2000)
                Log.i(TAG, "test1    1111111")
                2
            }
            val test2 = async {
                delay(3000)
                Log.i(TAG, "test1    222222222")
                3
            }
//            await挂起当前协程 等待相应协程执行结束
            val result = test1.await() + test2.await()
            Log.i(TAG, "result: $result")
        }
    }

    private fun testLaunch() {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                delay(2000)
                Log.i(TAG, "launch111")
            }
            launch {
                delay(1000)
                Log.i(TAG, "launch222")
            }
            Log.i(TAG, "launch launch launch")
        }
        Log.i(TAG, "out out out")
    }

    private fun testAsyncExcept() {
        GlobalScope.launch {
//            scope是supervisorScope
            supervisorScope {
                val test1 = async {
                    throw Exception("test 111")
                }

                try {
                    test1.await()
                } catch (e: Exception) {
                    Log.i(TAG, "test 111 exception")
                }
                Log.i(TAG, "out out out 11111")
            }
        }

//        runBlocking将主线程变成了协程
        runBlocking {
            launch {
                Log.i(TAG, "")
            }
//            async作为根协程
            val test2 = GlobalScope.async {
                throw Exception("test2222")
            }
            try {
                test2.await()
            } catch (e: Exception) {
                Log.i(TAG, "test 2222 e:${e.message}")
            }
        }


        CoroutineScope(Job()).launch {
            val test = async {
                throw Exception("test")
            }
            try {
                test.await()  //异常直接抛出 不会等到await的调用
            } catch (e: Exception) {
                Log.i(TAG, "test exception ")
            }

            launch {
                Log.i(TAG, "hhhhhhhhhhhhhhhhhhh")
            }
        }
    }

    private fun testCorotineScope() {
        val coroutineScope = CoroutineScope(Job())

        coroutineScope.launch() {
            try {
                throw Exception("test")
            } catch (e: Exception) {
                Log.i(TAG, "test except")
            }
        }
        coroutineScope.launch {
            Log.d("xys", "test")
        }
    }

    private fun testGlobalScope() {
        GlobalScope.launch {
            Log.i(TAG, "testGlobalScope")
        }
    }
}