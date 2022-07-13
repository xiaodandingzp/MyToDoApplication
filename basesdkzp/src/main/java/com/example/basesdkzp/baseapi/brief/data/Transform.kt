package com.example.basesdkzp.baseapi.brief.data

/**
 * Created by zp on 2022/7/9 18:16
 */

interface PrimitiveTransform {
    fun asBoolean(): Boolean?
    fun asInt(): Int?
    fun asDouble(): Double?
    fun asFloat(): Float?
    fun asLong(): Long?
    fun asString(): String?
}

interface OriginTransform {
    fun toOrigin(): Any?
}

interface ChildElementTransform {
    fun getChild(key: String): ElementTransform<*>
}

interface ElementTransform<T> : PrimitiveTransform, OriginTransform, ChildElementTransform