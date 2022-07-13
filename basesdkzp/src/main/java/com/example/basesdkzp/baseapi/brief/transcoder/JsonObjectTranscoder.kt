package com.example.basesdkzp.baseapi.brief.transcoder

import com.example.basesdkzp.baseapi.brief.data.BriefArray
import com.example.basesdkzp.baseapi.brief.data.BriefObject
import com.example.basesdkzp.baseapi.brief.data.BriefPrimitive
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by zp on 2022/7/12 21:38
 */
class JsonObjectTranscoder : DataTranscoder<JSONObject, BriefObject> {
    override val classTranscoder: Pair<Class<*>, Class<*>> =
        Pair(JSONObject::class.java, BriefObject::class.java)

    override fun transcode(source: JSONObject): BriefObject? {
        val obj = BriefObject()
        parseJSONObject(obj, source)
        return obj
    }

    fun parseJSONObject(obj: BriefObject, source: JSONObject) {
        for (key: String in source.keys()) {
            val jsonValue = source.get(key)
            when {
                jsonValue != null && BriefPrimitive.isPrimitive(jsonValue) -> {
                    obj.put(key, BriefPrimitive(jsonValue))
                }
                jsonValue is JSONObject -> {
                    val briefObjectTemp = BriefObject()
                    parseJSONObject(briefObjectTemp, jsonValue)
                    obj.put(key, briefObjectTemp)
                }
                jsonValue is JSONArray -> {
                    val briefArray = BriefArray()
                    parseJsonArray(briefArray, jsonValue)
                    obj.put(key, briefArray)
                }
            }
        }
    }

    private fun parseJsonArray(briefArray: BriefArray, source: JSONArray) {
        val first = source.opt(0)
        when {
            first != null && BriefPrimitive.isPrimitive(first) -> {
                for (i in 0..source.length()) {
                    briefArray.put(BriefPrimitive(source.get(i)))
                }
            }
            first is JSONObject -> {
                for (i in 0..source.length()) {
                    (source.opt(i) as? JSONObject)?.let {
                        val briefObject = BriefObject()
                        parseJSONObject(briefObject, it)
                        briefArray.put(briefObject)
                    }
                }
            }
            first is JSONArray -> {
                for (i in 0..source.length()) {
                    (source.opt(i) as? JSONArray)?.let {
                        val briefObject = BriefArray()
                        parseJsonArray(briefObject, it)
                        briefArray.put(briefObject)
                    }
                }
            }
        }

    }
}