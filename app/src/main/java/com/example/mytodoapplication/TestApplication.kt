package com.example.mytodoapplication

import android.app.Application
import android.content.Context
import com.example.mytodoapplication.hook.HookClassLoader


/**
 * Created by zp on 2021/8/17 14:38
 */
class TestApplication : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        HookClassLoader.hook()
    }
}