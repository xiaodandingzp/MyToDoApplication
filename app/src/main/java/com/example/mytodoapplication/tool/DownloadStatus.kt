package com.example.mytodoapplication.tool

import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File

sealed class DownloadStatus {
    object None : DownloadStatus()
    data class Progress(val value: Int) : DownloadStatus()
    data class Error(val throwable: Throwable) : DownloadStatus()
    data class Done(val file: File) : DownloadStatus()
}

object DownloadManager {
    fun download(url: String, file: File): Flow<DownloadStatus> {
        return flow {
            val request = Request.Builder().url(url).get().build()
            val response = OkHttpClient.Builder().build().newCall(request).execute()
            if (response.isSuccessful) {
                response.body()!!.let { body ->
                    val tatal = body.contentLength()
                    //文件读写
                    file.outputStream().use { output ->
                        val input = body.byteStream()
                        var emittedProgress = 0
//                        文件处理
                        emit(DownloadStatus.Progress(emittedProgress))
                    }
                }
                emit(DownloadStatus.Done(file))
            }
        }.catch {
            file.delete()
            emit(DownloadStatus.Error(it))
        }
    }
}

fun test() {
    val URL = ""
    val file = File("")
    GlobalScope.launch {
        DownloadManager.download(URL, file).collect { stats ->
            when (stats) {
                is DownloadStatus.Progress -> {
                    Log.i("test", "${stats.value}")
                }
                is DownloadStatus.Done -> {
                    Log.i("test", "${stats.file.length()}")
                }
                is DownloadStatus.Error -> {
                    Log.i("test", "${stats.throwable}")
                }
                is DownloadStatus.None -> {
                    Log.i("test", "none")
                }
            }

        }
    }
}
