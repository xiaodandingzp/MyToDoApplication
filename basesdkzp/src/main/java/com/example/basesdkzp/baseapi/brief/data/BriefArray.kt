package com.example.basesdkzp.baseapi.brief.data

/**
 * Created by zp on 2022/7/13 10:41
 */
class BriefArray : BriefElement<MutableList<BriefElement<*>>>, PrimitiveTransform {
    override var value: MutableList<BriefElement<*>> = arrayListOf()

    fun put(v: BriefElement<*>) {
        value.add(v)
    }

    fun putAll(list: List<BriefElement<*>>) {
        value.addAll(list)
    }

    override fun toOrigin(): Any? {
        return if (value == null) {
            listOf<Any>()
        } else {
            val first = value[0]
            if (first is BriefObject || first is BriefPrimitive || first is BriefArray) {
                value.map {
                    it.toOrigin()
                }
            } else {
                listOf<Any>()
            }
        }
    }

    override fun asBoolean(): Boolean? = if (value.size > 0) {
        (value[0] as? PrimitiveTransform)?.asBoolean()
    } else {
        null
    }

    override fun asInt(): Int? = if (value.size > 0) {
        (value[0] as? PrimitiveTransform)?.asInt()
    } else {
        null
    }

    override fun asDouble(): Double? = if (value.size > 0) {
        (value[0] as? PrimitiveTransform)?.asDouble()
    } else {
        null
    }

    override fun asFloat(): Float? = if (value.size > 0) {
        (value[0] as? PrimitiveTransform)?.asFloat()
    } else {
        null
    }

    override fun asLong(): Long? = if (value.size > 0) {
        (value[0] as? PrimitiveTransform)?.asLong()
    } else {
        null
    }

    override fun asString(): String? = if (value.size > 0) {
        (value[0] as? PrimitiveTransform)?.asString()
    } else {
        null
    }
}