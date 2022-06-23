package com.example.mytodoapplication.util

/**
 * Created by zp on 2022/4/14 15:12
 */
class MyLinked {
    enum class LinkedState {
        IDEAL, UNUSE
    }

    var firstProperty: String = ""
    var secondProperty: Int = 0

    constructor(firstProperty: String, secondProperty: Int) {
        this.firstProperty = firstProperty
        this.secondProperty = secondProperty
    }

    private var linkedState = LinkedState.IDEAL
    private var next: MyLinked? = null

    companion object {
        private const val TAG = "MyLinked"
        private const val maxCount = 20

        private var linkedPool: MyLinked? = null

        private val syncPool = Any()

        private var curSize = 0

        @JvmStatic
        fun obtain(firstProperty: String, secondProperty: Int): MyLinked {
            synchronized(syncPool) {
                linkedPool?.let {
                    val tem = it
                    linkedPool = tem.next
                    tem.next = null
                    curSize--
                    tem.secondProperty = secondProperty
                    tem.firstProperty = firstProperty
                    tem.linkedState = LinkedState.IDEAL
                }
                return MyLinked(firstProperty, secondProperty)
            }
        }

        @JvmStatic
        fun destroyPool() {
            curSize = 0
            linkedPool = null
        }
    }

    fun recycler() {
        if (linkedState == LinkedState.IDEAL) {
            realRecycle()
        }
    }

    private fun realRecycle() {
        linkedState = LinkedState.UNUSE
        synchronized(syncPool) {
            if (curSize < maxCount) {
                next = linkedPool
                linkedPool = this
                curSize++
            }
        }
    }
}