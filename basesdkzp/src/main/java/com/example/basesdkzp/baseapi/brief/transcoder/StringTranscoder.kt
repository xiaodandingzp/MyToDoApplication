package com.example.basesdkzp.baseapi.brief.transcoder

import com.example.basesdkzp.baseapi.brief.data.BriefObject
import org.json.JSONObject

/**
 * Created by zp on 2022/7/12 20:56
 * transcoder的结构
 * BriefObject数据结构
 */
class StringTranscoder(val transcoder: JsonObjectTranscoder) :
    DataTranscoder<String, BriefObject> {
    override val classTranscoder: Pair<Class<*>, Class<*>> =
        Pair(String::class.java, BriefObject::class.java)

    override fun transcode(data: String): BriefObject? {
        return if (data.isEmpty()) {
            BriefObject()
        } else {
            kotlin.runCatching {
                JSONObject(data)
            }.getOrNull()?.let {
                transcoder.transcode(it)
            } ?: BriefObject()
        }
    }
}