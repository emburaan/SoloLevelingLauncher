package com.sumit.clock.ui.timer;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u000b\b\u0007\u0018\u00002\u00020\u0001B\t\b\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012J\u000e\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u0014\u001a\u00020\u0012J\u000e\u0010\u0015\u001a\u00020\u00102\u0006\u0010\u0016\u001a\u00020\u0012J\u0006\u0010\u0017\u001a\u00020\u0010J\u0006\u0010\u0018\u001a\u00020\u0010J\b\u0010\u0019\u001a\u00020\u0010H\u0002J\b\u0010\u001a\u001a\u00020\u0010H\u0002J\b\u0010\u001b\u001a\u00020\u0010H\u0002J\b\u0010\u001c\u001a\u00020\u0010H\u0002R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00060\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001d"}, d2 = {"Lcom/sumit/clock/ui/timer/TimerViewModel;", "Landroidx/lifecycle/ViewModel;", "<init>", "()V", "_state", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/sumit/clock/ui/timer/TimerUiState;", "state", "Lkotlinx/coroutines/flow/StateFlow;", "getState", "()Lkotlinx/coroutines/flow/StateFlow;", "deadlineElapsedMs", "", "tickJob", "Lkotlinx/coroutines/Job;", "setHours", "", "h", "", "setMinutes", "m", "setSeconds", "s", "toggle", "reset", "start", "pause", "resume", "launchTick", "clock_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class TimerViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.sumit.clock.ui.timer.TimerUiState> _state = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.sumit.clock.ui.timer.TimerUiState> state = null;
    private long deadlineElapsedMs = 0L;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job tickJob;
    
    @javax.inject.Inject()
    public TimerViewModel() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.sumit.clock.ui.timer.TimerUiState> getState() {
        return null;
    }
    
    public final void setHours(int h) {
    }
    
    public final void setMinutes(int m) {
    }
    
    public final void setSeconds(int s) {
    }
    
    public final void toggle() {
    }
    
    public final void reset() {
    }
    
    private final void start() {
    }
    
    private final void pause() {
    }
    
    private final void resume() {
    }
    
    private final void launchTick() {
    }
}