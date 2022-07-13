package com.example.basesdkzp.baseapi.brief.repository

import androidx.annotation.IntDef
import javax.sql.DataSource


/**
 * IRepository
 * Created by kakin on 2021/9/24
 */
interface IRepository<T> {

    fun observeData(listener: DataListener<T>)

    interface DataListener<T> {
        fun onResult(dataSource: T?, @ResultCode code: Int)
    }


    @IntDef(RESULT_SUCCESS, RESULT_ERROR)
    annotation class ResultCode

    companion object {
        const val RESULT_SUCCESS = 0
        const val RESULT_ERROR = 1
    }
}