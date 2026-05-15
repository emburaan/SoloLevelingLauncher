// Minimal JNI bindings to llama.cpp for a single-shot completion API.
//
// Kotlin surface (com.sumit.launcher.llama.LlamaNative):
//   external fun loadModel(path: String): Long       // returns opaque handle
//   external fun complete(handle: Long, prompt: String, maxTokens: Int): String
//   external fun freeModel(handle: Long)

#include <android/log.h>
#include <jni.h>
#include <string>
#include <vector>

#include "llama.h"
#include "common.h"

#define LOG_TAG "llama-android"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,  LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

namespace {

struct LlamaSession {
    llama_model   * model   = nullptr;
    llama_context * ctx     = nullptr;
    llama_sampler * sampler = nullptr;
};

bool g_backend_inited = false;

void ensure_backend() {
    if (!g_backend_inited) {
        llama_backend_init();
        g_backend_inited = true;
    }
}

} // namespace

extern "C" JNIEXPORT jlong JNICALL
Java_com_sumit_launcher_llama_LlamaNative_loadModel(
        JNIEnv * env, jobject /* this */, jstring path) {
    ensure_backend();

    const char * c_path = env->GetStringUTFChars(path, nullptr);
    LOGI("loadModel: %s", c_path);

    auto * session = new LlamaSession();

    llama_model_params mparams = llama_model_default_params();
    // Default to CPU on Android; user can tune via build flags later.
    mparams.n_gpu_layers = 0;
    session->model = llama_load_model_from_file(c_path, mparams);
    env->ReleaseStringUTFChars(path, c_path);

    if (session->model == nullptr) {
        LOGE("loadModel: llama_load_model_from_file failed");
        delete session;
        return 0;
    }

    llama_context_params cparams = llama_context_default_params();
    cparams.n_ctx     = 512;
    cparams.n_threads = 2;
    cparams.n_batch   = 128;
    session->ctx = llama_new_context_with_model(session->model, cparams);
    if (session->ctx == nullptr) {
        LOGE("loadModel: llama_new_context_with_model failed");
        llama_free_model(session->model);
        delete session;
        return 0;
    }

    llama_sampler_chain_params sparams = llama_sampler_chain_default_params();
    session->sampler = llama_sampler_chain_init(sparams);
    llama_sampler_chain_add(session->sampler, llama_sampler_init_temp(0.8f));
    llama_sampler_chain_add(session->sampler, llama_sampler_init_top_p(0.95f, 1));
    llama_sampler_chain_add(session->sampler, llama_sampler_init_dist(LLAMA_DEFAULT_SEED));

    return reinterpret_cast<jlong>(session);
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_sumit_launcher_llama_LlamaNative_complete(
        JNIEnv * env, jobject /* this */, jlong handle, jstring prompt, jint max_tokens) {
    auto * session = reinterpret_cast<LlamaSession *>(handle);
    if (session == nullptr || session->ctx == nullptr) {
        return env->NewStringUTF("");
    }

    const char * c_prompt = env->GetStringUTFChars(prompt, nullptr);
    std::string prompt_str(c_prompt);
    env->ReleaseStringUTFChars(prompt, c_prompt);

    // Tokenize the prompt.
    std::vector<llama_token> tokens;
    tokens.resize(prompt_str.size() + 8);
    int n_prompt = llama_tokenize(
            session->model,
            prompt_str.c_str(),
            (int) prompt_str.size(),
            tokens.data(),
            (int) tokens.size(),
            /*add_special*/ true,
            /*parse_special*/ true);
    if (n_prompt < 0) {
        tokens.resize(-n_prompt);
        n_prompt = llama_tokenize(
                session->model,
                prompt_str.c_str(),
                (int) prompt_str.size(),
                tokens.data(),
                (int) tokens.size(),
                true,
                true);
    }
    tokens.resize(n_prompt);

    // Feed the prompt as a single batch.
    llama_batch batch = llama_batch_init(std::max(n_prompt, 512), 0, 1);
    for (int i = 0; i < n_prompt; i++) {
        common_batch_add(batch, tokens[i], i, {0}, /*logits*/ i == n_prompt - 1);
    }
    if (llama_decode(session->ctx, batch) != 0) {
        LOGE("complete: llama_decode (prompt) failed");
        llama_batch_free(batch);
        return env->NewStringUTF("");
    }

    std::string output;
    int n_cur = n_prompt;
    int n_decoded = 0;
    char piece_buf[256];

    while (n_decoded < max_tokens) {
        llama_token new_token = llama_sampler_sample(session->sampler, session->ctx, -1);
        if (llama_token_is_eog(session->model, new_token)) {
            break;
        }

        int piece_len = llama_token_to_piece(
                session->model, new_token, piece_buf, sizeof(piece_buf), 0, true);
        if (piece_len > 0) {
            output.append(piece_buf, piece_len);
        }

        common_batch_clear(batch);
        common_batch_add(batch, new_token, n_cur, {0}, true);

        if (llama_decode(session->ctx, batch) != 0) {
            LOGE("complete: llama_decode (token) failed");
            break;
        }
        n_cur++;
        n_decoded++;
    }

    llama_batch_free(batch);

    // Reset kv cache so the next call starts fresh (single-shot semantics).
    llama_kv_cache_clear(session->ctx);

    return env->NewStringUTF(output.c_str());
}

extern "C" JNIEXPORT void JNICALL
Java_com_sumit_launcher_llama_LlamaNative_freeModel(
        JNIEnv * /* env */, jobject /* this */, jlong handle) {
    auto * session = reinterpret_cast<LlamaSession *>(handle);
    if (session == nullptr) return;
    if (session->sampler) llama_sampler_free(session->sampler);
    if (session->ctx)     llama_free(session->ctx);
    if (session->model)   llama_free_model(session->model);
    delete session;
}
