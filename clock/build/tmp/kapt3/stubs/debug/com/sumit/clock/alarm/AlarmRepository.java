package com.sumit.clock.alarm;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0006\b\u0007\u0018\u0000 \u001d2\u00020\u0001:\u0001\u001dB\u0013\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\f0\u000bJ\u0010\u0010\u0012\u001a\u0004\u0018\u00010\f2\u0006\u0010\u0013\u001a\u00020\u0014J\u000e\u0010\u0015\u001a\u00020\f2\u0006\u0010\u0016\u001a\u00020\fJ\u000e\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0013\u001a\u00020\u0014J\b\u0010\u0019\u001a\u00020\u0014H\u0002J\u0016\u0010\u001a\u001a\u00020\u00182\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH\u0002J\u000e\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH\u0002R\u0016\u0010\u0006\u001a\n \b*\u0004\u0018\u00010\u00070\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010\u00a8\u0006\u001e"}, d2 = {"Lcom/sumit/clock/alarm/AlarmRepository;", "", "context", "Landroid/content/Context;", "<init>", "(Landroid/content/Context;)V", "prefs", "Landroid/content/SharedPreferences;", "kotlin.jvm.PlatformType", "_alarms", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "Lcom/sumit/clock/alarm/Alarm;", "alarms", "Lkotlinx/coroutines/flow/StateFlow;", "getAlarms", "()Lkotlinx/coroutines/flow/StateFlow;", "all", "find", "id", "", "upsert", "alarm", "delete", "", "nextId", "save", "list", "load", "Companion", "clock_debug"})
public final class AlarmRepository {
    private final android.content.SharedPreferences prefs = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.sumit.clock.alarm.Alarm>> _alarms = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.sumit.clock.alarm.Alarm>> alarms = null;
    @org.jetbrains.annotations.NotNull()
    @java.lang.Deprecated()
    public static final java.lang.String PREFS = "alarms";
    @org.jetbrains.annotations.NotNull()
    @java.lang.Deprecated()
    public static final java.lang.String KEY_ALARMS = "alarms";
    @org.jetbrains.annotations.NotNull()
    @java.lang.Deprecated()
    public static final java.lang.String KEY_NEXT_ID = "next_id";
    @org.jetbrains.annotations.NotNull()
    private static final com.sumit.clock.alarm.AlarmRepository.Companion Companion = null;
    
    @javax.inject.Inject()
    public AlarmRepository(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.sumit.clock.alarm.Alarm>> getAlarms() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.sumit.clock.alarm.Alarm> all() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.sumit.clock.alarm.Alarm find(long id) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.sumit.clock.alarm.Alarm upsert(@org.jetbrains.annotations.NotNull()
    com.sumit.clock.alarm.Alarm alarm) {
        return null;
    }
    
    public final void delete(long id) {
    }
    
    private final long nextId() {
        return 0L;
    }
    
    private final void save(java.util.List<com.sumit.clock.alarm.Alarm> list) {
    }
    
    private final java.util.List<com.sumit.clock.alarm.Alarm> load() {
        return null;
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/sumit/clock/alarm/AlarmRepository$Companion;", "", "<init>", "()V", "PREFS", "", "KEY_ALARMS", "KEY_NEXT_ID", "clock_debug"})
    static final class Companion {
        
        private Companion() {
            super();
        }
    }
}