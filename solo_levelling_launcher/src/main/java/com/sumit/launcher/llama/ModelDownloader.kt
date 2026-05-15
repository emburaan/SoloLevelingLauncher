package com.sumit.launcher.llama

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

private const val MODEL_FILE_NAME = "qwen2.5-0.5b-instruct-q4_k_m.gguf"
private const val MODEL_URL =
    "https://huggingface.co/Qwen/Qwen2.5-0.5B-Instruct-GGUF/resolve/main/" +
        "qwen2.5-0.5b-instruct-q4_k_m.gguf"

sealed interface DownloadEvent {
    data class Progress(val bytesRead: Long, val total: Long) : DownloadEvent
    data class Done(val file: File) : DownloadEvent
    data class Failed(val cause: Throwable) : DownloadEvent
}

@Singleton
class ModelDownloader @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun modelFile(): File = File(context.filesDir, "models/$MODEL_FILE_NAME")

    fun isDownloaded(): Boolean = modelFile().exists() && modelFile().length() > 0

    fun download(): Flow<DownloadEvent> = flow {
        val target = modelFile()
        target.parentFile?.mkdirs()
        val tmp = File(target.parentFile, "${target.name}.part")

        var connection: HttpURLConnection? = null
        try {
            connection = (URL(MODEL_URL).openConnection() as HttpURLConnection).apply {
                instanceFollowRedirects = true
                connectTimeout = 30_000
                readTimeout = 60_000
            }
            val total = connection.contentLengthLong.coerceAtLeast(-1L)
            connection.inputStream.use { input ->
                tmp.outputStream().use { output ->
                    val buffer = ByteArray(64 * 1024)
                    var bytesRead = 0L
                    while (true) {
                        val n = input.read(buffer)
                        if (n < 0) break
                        output.write(buffer, 0, n)
                        bytesRead += n
                        emit(DownloadEvent.Progress(bytesRead, total))
                    }
                }
            }
            if (!tmp.renameTo(target)) error("Failed to finalize model file")
            emit(DownloadEvent.Done(target))
        } catch (t: Throwable) {
            tmp.delete()
            emit(DownloadEvent.Failed(t))
        } finally {
            connection?.disconnect()
        }
    }.flowOn(Dispatchers.IO)
}
