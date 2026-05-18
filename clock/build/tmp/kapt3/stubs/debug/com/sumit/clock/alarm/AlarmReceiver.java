package com.sumit.clock.alarm;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u0000 \u00182\u00020\u0001:\u0001\u0018B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0018\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0016J\u0018\u0010\u0016\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0002J\u0010\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\u0015H\u0002R\u001e\u0010\u0004\u001a\u00020\u00058\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u001e\u0010\n\u001a\u00020\u000b8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000f\u00a8\u0006\u0019"}, d2 = {"Lcom/sumit/clock/alarm/AlarmReceiver;", "Landroid/content/BroadcastReceiver;", "<init>", "()V", "repository", "Lcom/sumit/clock/alarm/AlarmRepository;", "getRepository", "()Lcom/sumit/clock/alarm/AlarmRepository;", "setRepository", "(Lcom/sumit/clock/alarm/AlarmRepository;)V", "scheduler", "Lcom/sumit/clock/alarm/AlarmScheduler;", "getScheduler", "()Lcom/sumit/clock/alarm/AlarmScheduler;", "setScheduler", "(Lcom/sumit/clock/alarm/AlarmScheduler;)V", "onReceive", "", "context", "Landroid/content/Context;", "intent", "Landroid/content/Intent;", "handleFire", "handleSnooze", "Companion", "clock_debug"})
public final class AlarmReceiver extends android.content.BroadcastReceiver {
    @javax.inject.Inject()
    public com.sumit.clock.alarm.AlarmRepository repository;
    @javax.inject.Inject()
    public com.sumit.clock.alarm.AlarmScheduler scheduler;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_FIRE = "com.sumit.clock.action.ALARM_FIRE";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_SNOOZE = "com.sumit.clock.action.ALARM_SNOOZE";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_ALARM_ID = "alarm_id";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_ALARM_LABEL = "alarm_label";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_ALARM_HOUR = "alarm_hour";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_ALARM_MINUTE = "alarm_minute";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_MATH_PROBLEMS = "math_problems";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_MATH_DIFFICULTY = "math_difficulty";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_SHAKE_COUNT = "shake_count";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_TYPING_PHRASE = "typing_phrase";
    public static final long SNOOZE_MS = 300000L;
    @org.jetbrains.annotations.NotNull()
    public static final com.sumit.clock.alarm.AlarmReceiver.Companion Companion = null;
    
    public AlarmReceiver() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.sumit.clock.alarm.AlarmRepository getRepository() {
        return null;
    }
    
    public final void setRepository(@org.jetbrains.annotations.NotNull()
    com.sumit.clock.alarm.AlarmRepository p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.sumit.clock.alarm.AlarmScheduler getScheduler() {
        return null;
    }
    
    public final void setScheduler(@org.jetbrains.annotations.NotNull()
    com.sumit.clock.alarm.AlarmScheduler p0) {
    }
    
    @java.lang.Override()
    public void onReceive(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.content.Intent intent) {
    }
    
    private final void handleFire(android.content.Context context, android.content.Intent intent) {
    }
    
    private final void handleSnooze(android.content.Intent intent) {
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\n\n\u0002\u0010\t\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/sumit/clock/alarm/AlarmReceiver$Companion;", "", "<init>", "()V", "ACTION_FIRE", "", "ACTION_SNOOZE", "EXTRA_ALARM_ID", "EXTRA_ALARM_LABEL", "EXTRA_ALARM_HOUR", "EXTRA_ALARM_MINUTE", "EXTRA_MATH_PROBLEMS", "EXTRA_MATH_DIFFICULTY", "EXTRA_SHAKE_COUNT", "EXTRA_TYPING_PHRASE", "SNOOZE_MS", "", "clock_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}