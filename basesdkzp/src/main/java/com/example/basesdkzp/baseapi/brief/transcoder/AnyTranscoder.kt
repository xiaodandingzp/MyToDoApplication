package com.example.basesdkzp.baseapi.brief.transcoder

import com.example.basesdkzp.baseapi.brief.data.BriefObject

/**
 * Created by zp on 2022/7/13 16:22
 */
class AnyTranscoder : DataTranscoder<Any, BriefObject> {
    override val classTranscoder: Pair<Class<*>, Class<*>> =
        Pair(Any::class.java, BriefObject::class.java)

    val transcoders: MutableList<DataTranscoder<*, BriefObject>> = arrayListOf()
    override fun transcode(source: Any): BriefObject? {
        return transcoders.find {
            val classType = it.classTranscoder.first
            val isHit = classType.isInstance(source)
            isHit
        }?.let {
            (it as? DataTranscoder<Any, BriefObject>)?.transcode(source)
        }
    }

    fun add(dataTranscoder: DataTranscoder<*, BriefObject>) {
        transcoders.add(dataTranscoder)
    }
}