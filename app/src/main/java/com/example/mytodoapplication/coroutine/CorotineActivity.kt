package com.example.mytodoapplication.coroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.mytodoapplication.R
import kotlinx.coroutines.*

class CorotineActivity : AppCompatActivity() {

    companion object {
        const val TAG = "CorotineActivity"
    }

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_corotine)
        findViewById<Button>(R.id.button_corotine_test).setOnClickListener {
            GlobalScope.launch {
                delay(1000)
                suspendCancellableCoroutine<Unit> {
                    Log.i(TAG, "suspendCoroutine")
                    it.resume(Unit, null) //该行注释时，下面的代码不会执行
                }
                Log.i(TAG, "thread name: ${Thread.currentThread().name}, delay after")
            }
            Log.i(TAG, "thread name: ${Thread.currentThread().name}, click click")
        }
    }
}