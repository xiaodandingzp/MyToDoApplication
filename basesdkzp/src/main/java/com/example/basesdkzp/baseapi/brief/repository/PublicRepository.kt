package com.example.basesdkzp.baseapi.brief.repository

import javax.sql.DataSource

/**
 * PublicRepository
 * Created by kakin on 2021/9/26
 */
class PublicRepository : IRepository<Any?> {

    private companion object {
        const val MODEL_ID_UNKNOWN = -31 //未知Model id
    }

    private val mRepositories: MutableMap<IRepository<*>, Int> = hashMapOf()
    private var mListener: IRepository.DataListener<Any?>? = null

    override fun observeData(listener: IRepository.DataListener<Any?>) {
        mListener = listener
    }

    fun <SOURCE> addRepository(repository: IRepository<SOURCE>, modelId: Int? = null) {
        mRepositories[repository] = modelId ?: MODEL_ID_UNKNOWN
        repository.observeData(object : IRepository.DataListener<SOURCE> {
            override fun onResult(dataSource: SOURCE?, code: Int) {
                mListener?.onResult(dataSource, code)
            }
        })
    }
}