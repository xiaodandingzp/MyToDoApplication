package com.example.basesdkzp.baseapi.brief.data

import java.lang.NumberFormatException

/**
 * Created by zp on 2022/7/12 17:28
 */
class BriefPrimitive<T>(override var value: T) : BriefElement<T>, PrimitiveTransform {

    companion object {

        private val PRIMITIVE_TYPES = arrayOf(
            Number::class.javaPrimitiveType,
            String::class.javaPrimitiveType,
            Int::class.javaPrimitiveType,
            Long::class.javaPrimitiveType,
            Short::class.javaPrimitiveType,
            Float::class.javaPrimitiveType,
            Double::class.javaPrimitiveType,
            Byte::class.javaPrimitiveType,
            Boolean::class.javaPrimitiveType,
            Char::class.javaPrimitiveType,
            java.lang.Integer::class.java,
            java.lang.Long::class.java,
            java.lang.Short::class.java,
            java.lang.Float::class.java,
            java.lang.Double::class.java,
            java.lang.Byte::class.java,
            java.lang.Boolean::class.java,
            java.lang.Character::class.java
        )

        fun isPrimitive(value: Any?): Boolean {
            return value?.let {
                val classType = value::class.java
                for (standardPrimitive in PRIMITIVE_TYPES) {
                    if (standardPrimitive?.isAssignableFrom(classType) == true) {
                        true
                    }
                }
                false
            } ?: false
        }
    }

    override fun asBoolean(): Boolean? {
        return if (value is Boolean) {
            value as Boolean
        } else if (value is String) {
            value == "true"
        } else {
            null
        }
    }

    override fun asInt(): Int? {
        when (value) {
            is Int -> {
                return value as Int
            }
            is Number -> {
                return (value as Number).toInt()
            }
            is String -> {

            }
        }
        return null
    }

    override fun asDouble(): Double? = toDouble(value)

    override fun asFloat(): Float? = toAsFloat(value)

    private fun toAsFloat(value: Any?): Float? {
        return when (value) {
            is Float -> {
                value
            }
            is Number -> {
                value.toFloat()
            }
            is String -> {
                value.toFloat()
            }
            else -> null
        }
        null
    }

    private fun toDouble(value: Any?): Double? {
        when (value) {
            is Double -> {
                return value
            }
            is Number -> {
                return value.toDouble()
            }
            is String -> {
                try {
                    return java.lang.Double.parseDouble(value)
                } catch (ignored: NumberFormatException) {
                }
            }
        }
        return null
    }

    override fun asLong(): Long? = toAsLong(value)

    private fun toAsLong(value: Any?): Long? {
        return when (value) {
            is Long -> value
            is Number -> value.toLong()
            is String -> value.toLong()
            else -> null
        }
    }

    override fun asString(): String? = value.toString()

    override fun toOrigin(): Any? {
        return value
    }
}