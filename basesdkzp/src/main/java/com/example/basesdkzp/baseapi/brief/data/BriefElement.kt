package com.example.basesdkzp.baseapi.brief.data

/**
 * Created by zp on 2022/7/9 16:29
 */
interface BriefElement<T> : OriginTransform {
    var value: T

    companion object {
        fun <T> create(obj: T) = when {
            obj is Any && BriefPrimitive.isPrimitive(obj) -> {
                BriefPrimitive<T>(obj)
            }
            else -> {
                object : BriefElement<T> {
                    override var value: T = obj
                    override fun toOrigin(): Any? = value
                }
            }
        }
    }

}