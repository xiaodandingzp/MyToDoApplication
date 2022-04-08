package com.example.mytodoapplication.tool

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TabHost
import android.widget.TabHost.OnTabChangeListener
import android.widget.TabWidget
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.mytodoapplication.R
import java.util.*

/**
 * Created by zp on 2022/4/6 20:05
 *
 * 切换tab操作
 * HomeActivity布局
 */
class MyTabHost @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : TabHost(context, attrs, defStyleAttr, defStyleRes), OnTabChangeListener {

    private var mContext: Context? = null
    private var mManagerFragment: FragmentManager? = null
    private var mContentId: Int = 0
    private var mRealTabContent: FrameLayout? = null
    private val mTabs: ArrayList<TabInfo> = ArrayList<TabInfo>()
    private val mAttached = true
    private var mLastTab: TabInfo? = null

    companion object {
        const val TAG = "MyTabHost"
    }

    init {
        super.setOnTabChangedListener(this)
    }

    fun setUp(context: Context, manager: FragmentManager, containerId: Int) {
        ensureHierarchy(context)
        super.setup()
        mContext = context
        mManagerFragment = manager
        mContentId = containerId
        ensureContent()
    }


    fun setUp(context: Context, manager: FragmentManager) {
        ensureHierarchy(context)
        super.setup()
        mContext = context
        mManagerFragment = manager
        ensureContent()
    }

    private fun ensureHierarchy(context: Context) {
        // If owner hasn't made its own view hierarchy, then as a convenience
        // we will construct a standard one here.
        if (findViewById<View?>(android.R.id.tabs) == null) {
            Log.i(TAG, "findViewById<View?>(R.id.tabs) == null")
            val ll = LinearLayout(context)
            val tw = TabWidget(context)
            tw.id = android.R.id.tabs
            tw.orientation = TabWidget.HORIZONTAL
            ll.addView(
                tw, LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT, 0.0f
                )
            )
            tw.gravity = Gravity.BOTTOM
            ll.orientation = LinearLayout.VERTICAL
            addView(
                ll, LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
            var fl = FrameLayout(context)
            fl.id = android.R.id.tabcontent
            ll.addView(fl, LinearLayout.LayoutParams(0, 0, 0f))
            fl = FrameLayout(context)
            mRealTabContent = fl
            mRealTabContent?.id = mContentId
//            fl.visibility = INVISIBLE
            ll.addView(
                fl, LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f
                )
            )
            ll.clipChildren = false
        }
    }

    private fun ensureContent() {
        if (mRealTabContent == null) {
            mRealTabContent = findViewById<View>(mContentId) as FrameLayout
            checkNotNull(mRealTabContent) { "No tab content FrameLayout found for id $mContentId" }
        }
    }

    fun addTab(tabSpec: TabSpec, clss: String, args: Bundle) {
        tabSpec.setContent(DummyTabFactory(mContext!!))
        val tag = tabSpec.tag
        val info = TabInfo(tag, clss, args)
        if (mAttached) {
            // If we are already attached to the window, then check to make
            // sure this tab's fragment is inactive if it exists.  This shouldn't
            // normally happen.
            info.fragment = mManagerFragment?.findFragmentByTag(tag)
            if (info.fragment != null && info.fragment?.isDetached != true) {
                val ft: FragmentTransaction? = mManagerFragment?.beginTransaction()
                ft?.detach(info.fragment!!)
                ft?.commitAllowingStateLoss()
            }
        }
        mTabs.add(info)
        addTab(tabSpec)
    }


    internal class DummyTabFactory(private val mContext: Context) :
        TabContentFactory {
        override fun createTabContent(tag: String): View {
            val v = View(mContext)
            v.minimumWidth = 0
            v.minimumHeight = 0
            return v
        }
    }

    override fun onTabChanged(tabId: String) {
        if (mAttached) {
            val ft: FragmentTransaction = doTabChanged(tabId, null)
            if (ft != null) {
                ft.commitNowAllowingStateLoss()
            }
        }
    }


    private fun doTabChanged(tabId: String, ftt: FragmentTransaction?): FragmentTransaction {
        var ft = ftt
        var newTab: TabInfo? = null
        for (i in mTabs.indices) {
            val tab: TabInfo = mTabs[i]
            if (tab.tag == tabId) {
                newTab = tab
            }
        }
        checkNotNull(newTab) { "No tab known for tag $tabId" }
        newTab.let { newTab ->
            Log.i(TAG, "newTab.clss:${newTab.clssName}")
            if (mLastTab != newTab || mLastTab?.fragment?.javaClass?.name != newTab.clssName) {
                if (ft == null) {
                    ft = mManagerFragment?.beginTransaction()
                }
                if (mLastTab != null) {
                    if (mLastTab?.fragment != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            var lastTabIndex = -1
                            var newTabIndex = -1
                            for (i in mTabs.indices) {
                                if (mLastTab == mTabs[i]) {
                                    lastTabIndex = i
                                }
                                if (newTab == mTabs[i]) {
                                    newTabIndex = i
                                }
                                if (lastTabIndex != -1 && newTabIndex != -1) {
                                    break
                                }
                            }
                            if (lastTabIndex != -1 && newTabIndex != -1) {
                                if (lastTabIndex > newTabIndex) {
                                    ft?.setCustomAnimations(
                                        R.anim.hp_home_tab_left_in,
                                        R.anim.hp_home_tab_right_out
                                    )
                                } else {
                                    ft?.setCustomAnimations(
                                        R.anim.hp_home_tab_right_in,
                                        R.anim.hp_home_tab_left_out
                                    )
                                }
                            }
                        }
                        ft?.hide(mLastTab?.fragment!!)
                    }
                }
                if (newTab.clssName != null) {
                    //tab下内容为空或者tab的内容（class）变化，则更新
                    if (newTab.fragment == null || newTab.fragment?.javaClass?.name != newTab.clssName) {
                        newTab.fragment = Fragment.instantiate(
                            mContext!!, newTab.clssName, newTab.args
                        )
                    }
                    if (!newTab.fragment?.isAdded!!) {
                        ft?.add(mContentId, newTab.fragment!!, newTab.tag)
                    }
                    if (newTab.fragment!!.isDetached()) {
                        ft?.attach(newTab.fragment!!)
                        ft?.show(newTab.fragment!!)
                    } else {
                        ft?.show(newTab.fragment!!)
                    }
                }
                mLastTab = newTab
            }
        }
        return ft!!
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }
}

internal class TabInfo(
    val tag: String,
    val clssName: String,
    var args: Bundle
) {
    private val loadingClass: Class<*>? = null
    var fragment: Fragment? = null
}