package com.sumit.clock.alarm;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u0000 \u00142\u00020\u0001:\u0001\u0014B\u0013\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J\u0018\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\rJ\u000e\u0010\u000e\u001a\u00020\t2\u0006\u0010\u000f\u001a\u00020\rJ\u0006\u0010\u0010\u001a\u00020\u0011J\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u000f\u001a\u00020\rH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/sumit/clock/alarm/AlarmScheduler;", "", "context", "Landroid/content/Context;", "<init>", "(Landroid/content/Context;)V", "alarmManager", "Landroid/app/AlarmManager;", "schedule", "", "alarm", "Lcom/sumit/clock/alarm/Alarm;", "triggerAtMillis", "", "cancel", "alarmId", "canScheduleExact", "", "pendingIntent", "Landroid/app/PendingIntent;", "Companion", "clock_debug"})
public final class AlarmScheduler {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final android.app.AlarmManager alarmManager = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.sumit.clock.alarm.AlarmScheduler.Companion Companion = null;
    
    @javax.inject.Inject()
    public AlarmScheduler(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    public final void schedule(@org.jetbrains.annotations.NotNull()
    com.sumit.clock.alarm.Alarm alarm, long triggerAtMillis) {
    }
    
    public final void cancel(long alarmId) {
    }
    
    public final boolean canScheduleExact() {
        return false;
    }
    
    private final android.app.PendingIntent pendingIntent(long alarmId) {
        return null;
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007\u00a8\u0006\b"}, d2 = {"Lcom/sumit/clock/alarm/AlarmScheduler$Companion;", "", "<init>", "()V", "nextTrigger", "", "alarm", "Lcom/sumit/clock/alarm/Alarm;", "clock_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        public final long nextTrigger(@org.jetbrains.annotations.NotNull()
        com.sumit.clock.alarm.Alarm alarm) {
            return 0L;
        }
    }
}