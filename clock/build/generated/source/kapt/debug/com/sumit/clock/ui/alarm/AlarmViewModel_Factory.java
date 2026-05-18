package com.sumit.clock.ui.alarm;

import com.sumit.clock.alarm.AlarmRepository;
import com.sumit.clock.alarm.AlarmScheduler;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class AlarmViewModel_Factory implements Factory<AlarmViewModel> {
  private final Provider<AlarmRepository> repositoryProvider;

  private final Provider<AlarmScheduler> schedulerProvider;

  public AlarmViewModel_Factory(Provider<AlarmRepository> repositoryProvider,
      Provider<AlarmScheduler> schedulerProvider) {
    this.repositoryProvider = repositoryProvider;
    this.schedulerProvider = schedulerProvider;
  }

  @Override
  public AlarmViewModel get() {
    return newInstance(repositoryProvider.get(), schedulerProvider.get());
  }

  public static AlarmViewModel_Factory create(Provider<AlarmRepository> repositoryProvider,
      Provider<AlarmScheduler> schedulerProvider) {
    return new AlarmViewModel_Factory(repositoryProvider, schedulerProvider);
  }

  public static AlarmViewModel newInstance(AlarmRepository repository, AlarmScheduler scheduler) {
    return new AlarmViewModel(repository, scheduler);
  }
}
