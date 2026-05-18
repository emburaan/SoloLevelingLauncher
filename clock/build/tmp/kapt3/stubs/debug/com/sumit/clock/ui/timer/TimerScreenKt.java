package com.sumit.clock.ui.timer;

@kotlin.Metadata(mv = {2, 2, 0}, k = 2, xi = 48, d1 = {"\u00008\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0004\u001a\u0012\u0010\u0000\u001a\u00020\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u0007\u001a\u0010\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0006H\u0003\u001a,\u0010\u0007\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u00062\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00010\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00010\tH\u0003\u001a\\\u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\r2\u0012\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00010\u00112\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00010\u00112\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00010\u0011H\u0003\u001a4\u0010\u0014\u001a\u00020\u00012\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\r2\u0006\u0010\u0018\u001a\u00020\r2\u0012\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00010\u0011H\u0003\u00a8\u0006\u001a"}, d2 = {"TimerScreen", "", "viewModel", "Lcom/sumit/clock/ui/timer/TimerViewModel;", "CountdownDisplay", "state", "Lcom/sumit/clock/ui/timer/TimerUiState;", "TimerActions", "onToggle", "Lkotlin/Function0;", "onReset", "DurationPicker", "hours", "", "minutes", "seconds", "onHoursChange", "Lkotlin/Function1;", "onMinutesChange", "onSecondsChange", "NumberStepper", "label", "", "value", "max", "onValueChange", "clock_debug"})
public final class TimerScreenKt {
    
    @androidx.compose.runtime.Composable()
    public static final void TimerScreen(@org.jetbrains.annotations.NotNull()
    com.sumit.clock.ui.timer.TimerViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CountdownDisplay(com.sumit.clock.ui.timer.TimerUiState state) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void TimerActions(com.sumit.clock.ui.timer.TimerUiState state, kotlin.jvm.functions.Function0<kotlin.Unit> onToggle, kotlin.jvm.functions.Function0<kotlin.Unit> onReset) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void DurationPicker(int hours, int minutes, int seconds, kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onHoursChange, kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onMinutesChange, kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onSecondsChange) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void NumberStepper(java.lang.String label, int value, int max, kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onValueChange) {
    }
}