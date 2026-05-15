package com.sumit.launcher.command

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sumit.launcher.llama.DownloadEvent
import com.sumit.launcher.llama.LlamaCpp
import com.sumit.launcher.llama.ModelDownloader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface CommandStage {
    object Idle : CommandStage
    data class Downloading(val bytesRead: Long, val total: Long) : CommandStage
    object LoadingModel : CommandStage
    object Ready : CommandStage
    object Parsing : CommandStage
    object Executing : CommandStage
    data class Error(val message: String) : CommandStage
}

data class CommandBarState(
    val stage: CommandStage = CommandStage.Idle,
    val input: String = "",
    val resultMessage: String = "",
    val resultOk: Boolean = true
)

@HiltViewModel
class CommandBarViewModel @Inject constructor(
    private val downloader: ModelDownloader,
    private val llama: LlamaCpp,
    private val executor: CommandExecutor
) : ViewModel() {
    private val _state = MutableStateFlow(CommandBarState())
    val state: StateFlow<CommandBarState> = _state.asStateFlow()

    fun prepare() {
        if (_state.value.stage == CommandStage.Ready ||
            _state.value.stage is CommandStage.Downloading ||
            _state.value.stage == CommandStage.LoadingModel
        ) return

        viewModelScope.launch {
            if (llama.isLoaded()) {
                _state.update { it.copy(stage = CommandStage.Ready) }
                return@launch
            }

            if (!downloader.isDownloaded()) {
                _state.update { it.copy(stage = CommandStage.Downloading(0, -1)) }
                downloader.download().collect { event ->
                    when (event) {
                        is DownloadEvent.Progress -> _state.update {
                            it.copy(stage = CommandStage.Downloading(event.bytesRead, event.total))
                        }
                        is DownloadEvent.Done -> Unit
                        is DownloadEvent.Failed -> {
                            _state.update {
                                it.copy(
                                    stage = CommandStage.Error(
                                        event.cause.message ?: "Download failed"
                                    )
                                )
                            }
                            return@collect
                        }
                    }
                }
                if (_state.value.stage is CommandStage.Error) return@launch
            }

            _state.update { it.copy(stage = CommandStage.LoadingModel) }
            runCatching { llama.loadModel(downloader.modelFile().absolutePath) }
                .onSuccess { _state.update { it.copy(stage = CommandStage.Ready) } }
                .onFailure { t ->
                    _state.update {
                        it.copy(stage = CommandStage.Error(t.message ?: "Model load failed"))
                    }
                }
        }
    }

    fun onInputChange(text: String) {
        _state.update { it.copy(input = text) }
    }

    fun submit() {
        val input = _state.value.input.trim()
        if (input.isEmpty() || _state.value.stage != CommandStage.Ready) return
        viewModelScope.launch {
            _state.update { it.copy(stage = CommandStage.Parsing, resultMessage = "") }
            val raw = runCatching {
                llama.complete(CommandParser.buildPrompt(input), maxTokens = 32)
            }.getOrElse { t ->
                _state.update {
                    it.copy(stage = CommandStage.Error(t.message ?: "Parse failed"))
                }
                return@launch
            }

            val command = CommandParser.parse(raw)
            _state.update { it.copy(stage = CommandStage.Executing) }
            val result = executor.execute(command)
            when (result) {
                is CommandExecutor.Result.Ok -> _state.update {
                    it.copy(
                        stage = CommandStage.Ready,
                        resultMessage = result.message,
                        resultOk = true,
                        input = ""
                    )
                }
                is CommandExecutor.Result.Failed -> _state.update {
                    it.copy(
                        stage = CommandStage.Ready,
                        resultMessage = result.message,
                        resultOk = false
                    )
                }
            }
        }
    }
}
