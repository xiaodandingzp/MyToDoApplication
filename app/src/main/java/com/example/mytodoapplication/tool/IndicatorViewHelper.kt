package com.example.mytodoapplication.tool

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mytodoapplication.FirstFragment
import com.example.mytodoapplication.R
import com.example.mytodoapplication.SecondFragment
import com.example.mytodoapplication.ThirdFragment

/**
 * Created by zp on 2022/4/7 11:07
 */

//暂时不用
object IndicatorViewHelper {

    fun getIndicatorView(activity: Activity, tabInfo: TabHostInfo): View {
        val indicatorView =
            LayoutInflater.from(activity).inflate(R.layout.tabhost_indicator, null, false)
        indicatorView.findViewById<TextView>(R.id.icon_tabhost).apply {
            text = tabInfo.icon
        }
        indicatorView.findViewById<TextView>(R.id.des_tabhost).apply {
            text = tabInfo.dess
        }
        return indicatorView
    }
}

enum class TabHostInfo(val index: Int, val icon: String, val dess: String) {
    FIRST(0, "First", "首页") {
        override fun toString(): String {
            return "First"
        }

        override fun getFragment(): String {
            return FirstFragment()::class.java.name
        }
    },
    SECOND(1, "Second", "排行榜") {
        override fun toString(): String {
            return "Second"
        }

        override fun getFragment(): String {
            return SecondFragment::class.java.name
        }
    },
    THIRD(2, "Third", "个人中心") {
        override fun toString(): String {
            return "Third"
        }

        override fun getFragment(): String {
            return ThirdFragment::class.java.name
        }
    };

    fun getIndicatorView(activity: Activity): View {
        val indicatorView =
            LayoutInflater.from(activity).inflate(R.layout.tabhost_indicator, null, false)
        indicatorView.findViewById<TextView>(R.id.icon_tabhost).apply {
            text = icon
        }
        indicatorView.findViewById<TextView>(R.id.des_tabhost).apply {
            text = dess
        }
        return indicatorView
    }

    abstract override fun toString(): String
    abstract fun getFragment(): String

    companion object {
        fun of(index: Int): TabHostInfo? {
            return values().firstOrNull { it.index == index }
        }

        fun getSize(): Int {
            return values().size
        }
    }
}