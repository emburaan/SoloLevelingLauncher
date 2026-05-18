package com.sumit.clock.alarm;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c0\u0002\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eJ\u0006\u0010\u000f\u001a\u00020\fR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082T\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0018\u00010\tR\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/sumit/clock/alarm/AlarmWakeLock;", "", "<init>", "()V", "TAG", "", "TIMEOUT_MS", "", "wakeLock", "Landroid/os/PowerManager$WakeLock;", "Landroid/os/PowerManager;", "acquire", "", "context", "Landroid/content/Context;", "release", "clock_debug"})
public final class AlarmWakeLock {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "AlarmReceiver:fire";
    private static final long TIMEOUT_MS = 60000L;
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile android.os.PowerManager.WakeLock wakeLock;
    @org.jetbrains.annotations.NotNull()
    public static final com.sumit.clock.alarm.AlarmWakeLock INSTANCE = null;
    
    private AlarmWakeLock() {
        super();
    }
    
    @kotlin.jvm.Synchronized()
    public final synchronized void acquire(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    @kotlin.jvm.Synchronized()
    public final synchronized void release() {
    }
}