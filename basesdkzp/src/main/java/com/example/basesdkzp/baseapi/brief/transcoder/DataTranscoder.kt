package com.example.basesdkzp.baseapi.brief.transcoder

/**
 * Created by zp on 2022/7/12 20:55
 */
interface DataTranscoder<T, Z> {
    val classTranscoder: Pair<Class<*>, Class<*>>
    fun transcode(source: T): Z?
}