package com.example.basesdkzp.baseapi.brief.model

import com.example.basesdkzp.baseapi.brief.data.BriefElement
import com.example.basesdkzp.baseapi.brief.data.BriefObject
import com.example.basesdkzp.baseapi.brief.repository.IRepository
import com.example.basesdkzp.baseapi.brief.transcoder.DataTranscoder

/**
 * Created by zp on 2022/7/13 11:46
 */
class BriefModel<DATASOURCE>(
    val id: Int,
    val tag: String,
    val repo: IRepository<DATASOURCE>,
    val transcoder: DataTranscoder<Any, BriefObject>,
) {

    protected val briefObject = BriefObject()

    fun initalize() {
        repo.observeData(object : IRepository.DataListener<DATASOURCE> {
            override fun onResult(dataSource: DATASOURCE?, code: Int) {
                if (code == IRepository.RESULT_SUCCESS) {
                    transcoder.transcode(dataSource as Any)?.let {
                        briefObject.combine(it)
                    }
                }
            }
        })
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> query(key: String): T? {
        return queryElement<T>(key)?.toOrigin() as? T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> queryElement(key: String): BriefElement<T>? {
        val keys = key.split("#>")
        var briefObjectTemp: BriefElement<*>? = briefObject
        keys?.forEach {
            if (briefObjectTemp is BriefObject) {
                briefObjectTemp = (briefObjectTemp as BriefObject).get(key)
            } else {
                return null
            }
        }
        return briefObjectTemp as? BriefElement<T>
    }
}