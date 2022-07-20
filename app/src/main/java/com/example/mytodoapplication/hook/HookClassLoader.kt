package com.example.mytodoapplication.hook

import android.util.Log
import com.example.basesdkzp.test.Test
import dalvik.system.DexClassLoader
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers

object HookClassLoader {

    const val TAG = "HookClassLoader"

    fun hook() {
        XposedHelpers.findAndHookMethod(
            Thread::class.java,
            "start",
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    super.beforeHookedMethod(param)
                    Log.i(TAG, "hook  do something")
                }
            }
        )

    }
}