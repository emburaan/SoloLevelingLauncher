package com.sumit.launcher.llama

/**
 * Thin JNI surface for llama.cpp. The actual implementations live in
 * `src/main/cpp/llama-android.cpp`. Don't call these directly from the app
 * code — go through [LlamaCpp] instead.
 */
internal class LlamaNative {
    external fun loadModel(path: String): Long
    external fun complete(handle: Long, prompt: String, maxTokens: Int): String
    external fun freeModel(handle: Long)

    companion object {
        init {
            System.loadLibrary("llama-android")
        }
    }
}
