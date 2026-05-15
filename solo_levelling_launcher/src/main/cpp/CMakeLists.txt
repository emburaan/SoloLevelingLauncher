cmake_minimum_required(VERSION 3.22.1)
project("llama-android")

include(FetchContent)
FetchContent_Declare(
    llama
    GIT_REPOSITORY https://github.com/ggerganov/llama.cpp.git
    GIT_TAG        b4404
)

# Keep the build lean: skip llama.cpp's own tools/tests/curl.
set(LLAMA_BUILD_COMMON     ON  CACHE BOOL "" FORCE)
set(LLAMA_BUILD_EXAMPLES   OFF CACHE BOOL "" FORCE)
set(LLAMA_BUILD_TESTS      OFF CACHE BOOL "" FORCE)
set(LLAMA_BUILD_SERVER     OFF CACHE BOOL "" FORCE)
set(LLAMA_CURL             OFF CACHE BOOL "" FORCE)
set(GGML_LLAMAFILE         OFF CACHE BOOL "" FORCE)

FetchContent_MakeAvailable(llama)

add_library(${CMAKE_PROJECT_NAME} SHARED llama-android.cpp)

target_link_libraries(${CMAKE_PROJECT_NAME}
    llama
    common
    android
    log)
