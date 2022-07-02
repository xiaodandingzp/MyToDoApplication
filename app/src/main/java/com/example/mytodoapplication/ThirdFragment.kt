package com.example.mytodoapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.mytodoapplication.coroutine.CorotineActivity

/**
 * Created by zp on 2022/4/7 14:48
 */
class ThirdFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.third_fragment, null).apply {
            findViewById<Button>(R.id.coroutine).setOnClickListener {
                startActivity(Intent(this@ThirdFragment.context, CorotineActivity::class.java))
            }
        }
        return view
    }

}