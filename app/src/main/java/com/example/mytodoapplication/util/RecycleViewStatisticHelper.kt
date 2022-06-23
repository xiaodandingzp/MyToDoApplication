package com.example.mytodoapplication.util

import android.os.Handler
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

/**
 * Created by zp on 2022/5/31 17:59
 * recyclerView相关的数据上报
 */
class RecycleViewStatisticHelper {

    companion object {
        const val TAG = "RecycleViewStatisticHelper"
    }

    private var recyclerView: RecyclerView? = null
    private var onVisibleChange: Disposable? = null
    private val handler = Handler()

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(view: RecyclerView, newState: Int) {
//            sendStatistic(view, calTime = newState != SCROLL_STATE_IDLE)
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                Log.i(TAG, "onScrollStateChanged newState is RecyclerView.SCROLL_STATE_IDLE")
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            Log.i(TAG, "onScrolled onScrolled onScrolled")
        }
    }

    private val childAttachListener = object : RecyclerView.OnChildAttachStateChangeListener {
        override fun onChildViewAttachedToWindow(view: View) {
//            do something about expose
        }

        override fun onChildViewDetachedFromWindow(view: View) {
//            do something
        }

    }


//    onVisibleChange 页面的可见性
    fun setUp(
        recyclerView: RecyclerView,
        onVisibleChange: Observable<Boolean>? = null,
        currentVisible: Boolean
    ) {
        this.recyclerView = recyclerView.apply {
            addOnScrollListener(scrollListener)
            addOnChildAttachStateChangeListener(childAttachListener)
        }
        this.onVisibleChange = onVisibleChange?.subscribe {
//            do something
//            可以上报可见的view
        }
    }

    fun clear() {
        onVisibleChange?.dispose()
    }
}