package com.sumit.clock.alarm;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0005\u0018\u0000 \u001d2\u00020\u0001:\u0001\u001dB\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\b\u001a\u00020\tH\u0016J\"\u0010\n\u001a\u00020\u00072\b\u0010\u000b\u001a\u0004\u0018\u00010\f2\u0006\u0010\r\u001a\u00020\u00072\u0006\u0010\u000e\u001a\u00020\u0007H\u0016J\b\u0010\u000f\u001a\u00020\tH\u0016J\u0014\u0010\u0010\u001a\u0004\u0018\u00010\u00112\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0016J\u0010\u0010\u0012\u001a\u00020\t2\u0006\u0010\u0013\u001a\u00020\u0014H\u0002J\b\u0010\u0015\u001a\u00020\tH\u0002J\u0018\u0010\u0016\u001a\u00020\t2\u0006\u0010\u0017\u001a\u00020\f2\u0006\u0010\u0018\u001a\u00020\u0019H\u0002J\b\u0010\u001a\u001a\u00020\tH\u0002J\b\u0010\u001b\u001a\u00020\tH\u0002J\b\u0010\u001c\u001a\u00020\tH\u0002R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001e"}, d2 = {"Lcom/sumit/clock/alarm/AlarmRingService;", "Landroid/app/Service;", "<init>", "()V", "ringer", "Landroid/media/MediaPlayer;", "lockedAlarmVolume", "", "onCreate", "", "onStartCommand", "intent", "Landroid/content/Intent;", "flags", "startId", "onDestroy", "onBind", "Landroid/os/IBinder;", "startForegroundCompat", "label", "", "createNotificationChannel", "launchRingActivity", "source", "alarmId", "", "startRinger", "stopRinging", "lockAlarmVolume", "Companion", "clock_debug"})
public final class AlarmRingService extends android.app.Service {
    @org.jetbrains.annotations.Nullable()
    private android.media.MediaPlayer ringer;
    private int lockedAlarmVolume = -1;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_STOP = "com.sumit.clock.action.ALARM_STOP";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String CHANNEL_ID = "alarm_ring";
    private static final int NOTIFICATION_ID = 7474;
    @org.jetbrains.annotations.NotNull()
    public static final com.sumit.clock.alarm.AlarmRingService.Companion Companion = null;
    
    public AlarmRingService() {
        super();
    }
    
    @java.lang.Override()
    public void onCreate() {
    }
    
    @java.lang.Override()
    public int onStartCommand(@org.jetbrains.annotations.Nullable()
    android.content.Intent intent, int flags, int startId) {
        return 0;
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public android.os.IBinder onBind(@org.jetbrains.annotations.Nullable()
    android.content.Intent intent) {
        return null;
    }
    
    private final void startForegroundCompat(java.lang.String label) {
    }
    
    private final void createNotificationChannel() {
    }
    
    private final void launchRingActivity(android.content.Intent source, long alarmId) {
    }
    
    private final void startRinger() {
    }
    
    private final void stopRinging() {
    }
    
    private final void lockAlarmVolume() {
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0016\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eJ\u000e\u0010\u000f\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fR\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/sumit/clock/alarm/AlarmRingService$Companion;", "", "<init>", "()V", "ACTION_STOP", "", "CHANNEL_ID", "NOTIFICATION_ID", "", "start", "", "context", "Landroid/content/Context;", "fireIntent", "Landroid/content/Intent;", "stop", "clock_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        public final void start(@org.jetbrains.annotations.NotNull()
        android.content.Context context, @org.jetbrains.annotations.NotNull()
        android.content.Intent fireIntent) {
        }
        
        public final void stop(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
        }
    }
}