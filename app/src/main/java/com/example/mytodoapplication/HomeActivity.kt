package com.example.mytodoapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.basesdkzp.test.Test
import com.example.mytodoapplication.tool.MyTabHost
import com.example.mytodoapplication.tool.TabHostInfo

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        initTabHost()
        Test.test()
    }

    private fun initTabHost() {
        val tabHost = findViewById<MyTabHost>(R.id.tabhost)
        tabHost.setUp(this, supportFragmentManager, R.id.real_tab_content)
        for (i in 0..TabHostInfo.getSize()) {
            TabHostInfo.of(i)?.let { info ->
                val tabSpec = tabHost.newTabSpec(info.toString())
                    .setIndicator(info.getIndicatorView(this))
                tabHost.addTab(tabSpec, info.getFragment(), Bundle())

            }
        }
    }
}