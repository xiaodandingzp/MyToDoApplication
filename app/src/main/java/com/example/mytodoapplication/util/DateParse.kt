package com.example.mytodoapplication.util

import android.annotation.SuppressLint
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by zp on 2022/4/16 16:40
 */
object DateParse {
    const val TAG = "DateParse"

    @SuppressLint("SimpleDateFormat")
    fun getTimeInMillis(year: String, month: String, day: String): Long {
        val date = "${year}年${month}月${day}日 00:00:00 000"
        try {
            val calendar = Calendar.getInstance()
            calendar.time = SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss SSS").parse(date)!!
            return calendar.timeInMillis
        } catch (e: Exception) {
            Log.i(TAG, "calendar error ${e.stackTrace}")
        }
        return 0L
    }
}