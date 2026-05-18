package com.sumit.clock.ui.alarm;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\u0019\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0004\b\u0006\u0010\u0007J\u0006\u0010\u000e\u001a\u00020\u000fJ\u0016\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u000b2\u0006\u0010\u0013\u001a\u00020\u000fJ\u000e\u0010\u0014\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u000bJ\u000e\u0010\u0015\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u000bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\n0\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r\u00a8\u0006\u0016"}, d2 = {"Lcom/sumit/clock/ui/alarm/AlarmViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/sumit/clock/alarm/AlarmRepository;", "scheduler", "Lcom/sumit/clock/alarm/AlarmScheduler;", "<init>", "(Lcom/sumit/clock/alarm/AlarmRepository;Lcom/sumit/clock/alarm/AlarmScheduler;)V", "alarms", "Lkotlinx/coroutines/flow/StateFlow;", "", "Lcom/sumit/clock/alarm/Alarm;", "getAlarms", "()Lkotlinx/coroutines/flow/StateFlow;", "canScheduleExact", "", "setEnabled", "", "alarm", "enabled", "save", "delete", "clock_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class AlarmViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.sumit.clock.alarm.AlarmRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.sumit.clock.alarm.AlarmScheduler scheduler = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.sumit.clock.alarm.Alarm>> alarms = null;
    
    @javax.inject.Inject()
    public AlarmViewModel(@org.jetbrains.annotations.NotNull()
    com.sumit.clock.alarm.AlarmRepository repository, @org.jetbrains.annotations.NotNull()
    com.sumit.clock.alarm.AlarmScheduler scheduler) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.sumit.clock.alarm.Alarm>> getAlarms() {
        return null;
    }
    
    public final boolean canScheduleExact() {
        return false;
    }
    
    public final void setEnabled(@org.jetbrains.annotations.NotNull()
    com.sumit.clock.alarm.Alarm alarm, boolean enabled) {
    }
    
    public final void save(@org.jetbrains.annotations.NotNull()
    com.sumit.clock.alarm.Alarm alarm) {
    }
    
    public final void delete(@org.jetbrains.annotations.NotNull()
    com.sumit.clock.alarm.Alarm alarm) {
    }
}