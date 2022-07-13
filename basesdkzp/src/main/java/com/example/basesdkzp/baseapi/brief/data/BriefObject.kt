package com.example.basesdkzp.baseapi.brief.data

import org.json.JSONObject
import java.util.*

/**
 * Created by zp on 2022/7/12 20:16
 */
class BriefObject : BriefElement<MutableMap<String, BriefElement<*>>>, PrimitiveTransform {
    override var value: MutableMap<String, BriefElement<*>> =
        TreeMap<String, BriefElement<*>>()

    override fun toOrigin(): Any {
        return value
    }

    fun combine(briefObject: BriefObject) {
        value.putAll(briefObject.value)
    }

    fun put(key: String, briefElement: BriefElement<*>) {
        value[key] = briefElement
    }

    fun get(key: String): BriefElement<*> {
        return value[key] ?: BriefElement.create(null)
    }

    override fun asBoolean(): Boolean? = null

    override fun asInt(): Int? = null

    override fun asDouble(): Double? = null

    override fun asFloat(): Float? = null

    override fun asLong(): Long? = null

    override fun asString(): String? {
        val result = JSONObject()
        value.forEach {
            result.put(it.key, it.value.toOrigin())
        }
        return result.toString()
    }
}