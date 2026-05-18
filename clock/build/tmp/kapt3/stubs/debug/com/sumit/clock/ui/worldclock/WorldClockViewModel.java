package com.sumit.clock.ui.worldclock;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\tJ\u000e\u0010\u000f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\tR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0006\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\b0\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u0010"}, d2 = {"Lcom/sumit/clock/ui/worldclock/WorldClockViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/sumit/clock/worldclock/WorldClockRepository;", "<init>", "(Lcom/sumit/clock/worldclock/WorldClockRepository;)V", "zoneIds", "Lkotlinx/coroutines/flow/StateFlow;", "", "", "getZoneIds", "()Lkotlinx/coroutines/flow/StateFlow;", "add", "", "zoneId", "remove", "clock_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class WorldClockViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.sumit.clock.worldclock.WorldClockRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<java.lang.String>> zoneIds = null;
    
    @javax.inject.Inject()
    public WorldClockViewModel(@org.jetbrains.annotations.NotNull()
    com.sumit.clock.worldclock.WorldClockRepository repository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<java.lang.String>> getZoneIds() {
        return null;
    }
    
    public final void add(@org.jetbrains.annotations.NotNull()
    java.lang.String zoneId) {
    }
    
    public final void remove(@org.jetbrains.annotations.NotNull()
    java.lang.String zoneId) {
    }
}