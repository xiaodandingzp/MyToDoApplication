package com.example.basesdkzp.baseapi.kotlinex

import androidx.annotation.MainThread
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Created by zp on 2022/7/7 17:38
 */
@MainThread
suspend fun Lifecycle.tryWaitState(
    state: Lifecycle.State,
    timeOut: Long = 200,
    beforeAwait: (() -> Unit)? = null
): Boolean {
    return if (timeOut == Long.MAX_VALUE) {
        suspendCancellableCoroutine<Boolean> { continuation ->
            if (currentState.isAtLeast(state)) {
                continuation.resume(true)
            } else {
                object : LifecycleEventObserver {
                    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                        if (event.targetState == state) {
                            continuation.resume(true)
                            removeObserver(this)
                        }
                    }
                }.apply {
                    beforeAwait?.invoke()
                    addObserver(this)
                }
            }
        }

    } else {
        if (!currentState.isAtLeast(state)) {
            beforeAwait?.invoke()
            delay(timeOut)
            currentState.isAtLeast(state)
        } else true
    }
}

suspend fun test() {
}