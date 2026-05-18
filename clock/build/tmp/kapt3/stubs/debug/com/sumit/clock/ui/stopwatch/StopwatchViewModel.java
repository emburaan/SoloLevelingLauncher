package com.sumit.clock.ui.stopwatch;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\t\b\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0006\u0010\u0010\u001a\u00020\u0011J\u0006\u0010\u0012\u001a\u00020\u0011J\u0006\u0010\u0013\u001a\u00020\u0011J\b\u0010\u0014\u001a\u00020\u0011H\u0002J\b\u0010\u0015\u001a\u00020\u0011H\u0002R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00060\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2 = {"Lcom/sumit/clock/ui/stopwatch/StopwatchViewModel;", "Landroidx/lifecycle/ViewModel;", "<init>", "()V", "_state", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/sumit/clock/ui/stopwatch/StopwatchUiState;", "state", "Lkotlinx/coroutines/flow/StateFlow;", "getState", "()Lkotlinx/coroutines/flow/StateFlow;", "startElapsedMs", "", "accumulatedMs", "tickJob", "Lkotlinx/coroutines/Job;", "toggle", "", "reset", "lap", "start", "pause", "clock_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class StopwatchViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.sumit.clock.ui.stopwatch.StopwatchUiState> _state = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.sumit.clock.ui.stopwatch.StopwatchUiState> state = null;
    private long startElapsedMs = 0L;
    private long accumulatedMs = 0L;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job tickJob;
    
    @javax.inject.Inject()
    public StopwatchViewModel() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.sumit.clock.ui.stopwatch.StopwatchUiState> getState() {
        return null;
    }
    
    public final void toggle() {
    }
    
    public final void reset() {
    }
    
    public final void lap() {
    }
    
    private final void start() {
    }
    
    private final void pause() {
    }
}