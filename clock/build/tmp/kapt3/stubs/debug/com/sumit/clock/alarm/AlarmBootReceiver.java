package com.sumit.clock.alarm;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0018\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0016R\u001e\u0010\u0004\u001a\u00020\u00058\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u001e\u0010\n\u001a\u00020\u000b8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000f\u00a8\u0006\u0016"}, d2 = {"Lcom/sumit/clock/alarm/AlarmBootReceiver;", "Landroid/content/BroadcastReceiver;", "<init>", "()V", "repository", "Lcom/sumit/clock/alarm/AlarmRepository;", "getRepository", "()Lcom/sumit/clock/alarm/AlarmRepository;", "setRepository", "(Lcom/sumit/clock/alarm/AlarmRepository;)V", "scheduler", "Lcom/sumit/clock/alarm/AlarmScheduler;", "getScheduler", "()Lcom/sumit/clock/alarm/AlarmScheduler;", "setScheduler", "(Lcom/sumit/clock/alarm/AlarmScheduler;)V", "onReceive", "", "context", "Landroid/content/Context;", "intent", "Landroid/content/Intent;", "clock_debug"})
public final class AlarmBootReceiver extends android.content.BroadcastReceiver {
    @javax.inject.Inject()
    public com.sumit.clock.alarm.AlarmRepository repository;
    @javax.inject.Inject()
    public com.sumit.clock.alarm.AlarmScheduler scheduler;
    
    public AlarmBootReceiver() {
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
}