package com.sumit.launcher.llama

import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LlamaCpp @Inject constructor(
    @ApplicationContext private val context: android.content.Context
) {
    private val native = LlamaNative()
    private val mutex = Mutex()

    @Volatile private var handle: Long = 0L
    @Volatile private var loadedPath: String? = null

    suspend fun loadModel(path: String) = withContext(Dispatchers.IO) {
        mutex.withLock {
            if (handle != 0L && loadedPath == path) return@withContext
            if (handle != 0L) {
                native.freeModel(handle)
                handle = 0L
                loadedPath = null
            }
            val newHandle = native.loadModel(path)
            check(newHandle != 0L) { "Failed to load model at $path" }
            handle = newHandle
            loadedPath = path
        }
    }

    suspend fun complete(prompt: String, maxTokens: Int = 256): String =
        withContext(Dispatchers.IO) {
            mutex.withLock {
                check(handle != 0L) { "Model not loaded — call loadModel first" }
                native.complete(handle, prompt, maxTokens)
            }
        }

    fun isLoaded(): Boolean = handle != 0L

    fun release() {
        if (handle != 0L) {
            native.freeModel(handle)
            handle = 0L
            loadedPath = null
        }
    }
}
