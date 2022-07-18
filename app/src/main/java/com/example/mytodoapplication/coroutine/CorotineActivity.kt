package com.example.mytodoapplication.coroutine

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.mytodoapplication.R
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.system.measureTimeMillis

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
            CoroutineObject.testEntrance()
        }
    }

    private fun testRunBlocking() = runBlocking {
        launch {
            delay(5000)
            Log.i(TAG, "-1-1-1thread.name:${Thread.currentThread().name}")
        }
        val scope = CoroutineScope(Job())
        Log.i(TAG, "000thread.name:${Thread.currentThread().name}")
        scope.launch {
            delay(2000)
            Log.i(TAG, "111thread.name:${Thread.currentThread().name}")
            Log.i(TAG, "11111")
        }
        scope.launch {
            delay(2000)
            Log.i(TAG, "222thread.name:${Thread.currentThread().name}")
            Log.i(TAG, "22222")
        }
        Log.i(TAG, "4444thread.name:${Thread.currentThread().name}")
        delay(3000)
        scope.cancel()
    }

    private fun testAsync() {
        GlobalScope.launch(Job() + Dispatchers.Default) {
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

    private fun testFlow() = runBlocking {
        val testSequence = sequence<Int> {
//            同步返回多个值 不能调用SequenceScope定义之外的其他的挂起函数
            for (i in 1..3) {
                Thread.sleep(1000)
                yield(i)
            }
        }
        testSequence.forEach {
            Log.i(TAG, "test test i: $it")
        }
        Log.i(TAG, "test test 111111")
        launch {
//                异步返回多个值 可用于进度条进度的更新 可以调用其他挂起函数
//            flow的构建方式一
            val flowTest = flow<Int> {
                for (i in 1..3) {
                    delay(1000)
                    emit(i)
                }
            }
            flowTest.collect {
                Log.i(TAG, "i: $it")
            }
        }
        Log.i(TAG, "test test 22222")
//      flow的构建方式二  flowOn更改flow的上下文
//        flowOn操作符对上游范围有效, 范围是指两个flowOn之间, 如果只有一个flowOn,则上游全部有效
//      launchIn（返回的是Job对象）替换collect 可以在单独的协程中启动流的收集
        flowOf("one", "two", "three")
            .onEach {
                delay(1000)
                Log.i(TAG, "i: $it  thread.name:${Thread.currentThread().name}")
            }.flowOn(Dispatchers.Default)
            .collect {
                Log.i(TAG, "i: $it  thread.name:${Thread.currentThread().name}")
            }
//      flow的构建方式三
        (5..9).asFlow().onEach {
            delay(1000)
        }.collect {
            Log.i(TAG, "it: $it")
        }
    }

    fun testFlowEmit() =
        flow<Int> {
            for (i in 1..5) {
                delay(1000)
                emit(i)
                Log.i(TAG, "testFlowCancel emit: $i")
            }
        }

    /*
    *流构建器对每个发射值执行附加的ensureActive检测以进行取消，这意味着从flow{}发出的繁忙循环是可以取消的
    * 出于性能原因，大多数其他流操作不会自行执行其他取消操作，在协程繁忙的情况下，必须明确检测是否取消
    * 通过cancelable来执行此操作
    */
    private fun testFlowCancelReceive() = runBlocking {
        (1..5).asFlow().cancellable().collect {
            Log.i(TAG, "testFlowCancelReceive: $it")
            if (it == 3) cancel()
        }
    }

    /*
    *背压：发射者发射速度，大于接收者处理的速度
    * buffer(),并发运行流中发射的元素。（）将发送的值先缓存起来
    * conflate(),合并发射项，不对每个值进行处理。积压多个未处理值时，选择最后一个，其他的丢弃
    * collectLatest(),取消并重新发射最后一个值
    */
    private fun testFlowBackPressure() = runBlocking {
        val time = measureTimeMillis {
            testFlowEmit()
//                .buffer(50)
//                .conflate()
                .onEach {
//                    delay(3000)
                }.collectLatest {
//                .collect {
                    delay(2000)
                    Log.i(TAG, "testFlowBackPressure: $it")
                }
        }
        Log.i(TAG, "time: $time")
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
                runBlocking {

                }

                coroutineScope {

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

        coroutineScope.launch {
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
            delay(200)
            delay(2000)
            Log.i(TAG, "sssssss")
        }
    }

    private fun testSuspend() {
        println("main start")
        GlobalScope.launch {
            println("async start")
            val b = async {
                delay(2000)
                "async"
            }
            b.await()
            println("async end")
        }
    }

    private fun testZip() = runBlocking {
        val first = (1..3).asFlow()
        val second = (2..5).asFlow()
        first.zip(second) { i: Int, i1: Int ->
            Log.i(TAG, "i:$i  i1:$i1")
            i + i1
        }.collect {
            Log.i(TAG, "it:$it")
        }
    }

    private fun testFlowExcept() = runBlocking {
        flow<Int> {
            emit(2)
            throw Exception("testFlowExcept")
        }.catch { e: Throwable ->
            Log.i(TAG, "$e")
            emit(3)
        }.onCompletion {
            Log.i(TAG, "completion")
        }.collect {
            Log.i(TAG, "it:$it")
        }
    }

    private fun testSuspendThread() {
        Log.i(TAG, "out thread.name:${Thread.currentThread().name}")
        GlobalScope.launch {
            Log.i(TAG, "GlobalScope thread.name:${Thread.currentThread().name}")
            suspendCancellableCoroutine<Unit> {
                Log.i(TAG, "suspendCancellableCoroutine thread.name:${Thread.currentThread().name}")
                it.resume(Unit, null)
            }
            Log.i(
                TAG,
                " after suspendCancellableCoroutine thread.name:${Thread.currentThread().name}"
            )
        }
    }
}