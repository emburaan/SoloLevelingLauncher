package com.sumit.clock.alarm;

import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;

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
public final class AlarmReceiver_MembersInjector implements MembersInjector<AlarmReceiver> {
  private final Provider<AlarmRepository> repositoryProvider;

  private final Provider<AlarmScheduler> schedulerProvider;

  public AlarmReceiver_MembersInjector(Provider<AlarmRepository> repositoryProvider,
      Provider<AlarmScheduler> schedulerProvider) {
    this.repositoryProvider = repositoryProvider;
    this.schedulerProvider = schedulerProvider;
  }

  public static MembersInjector<AlarmReceiver> create(Provider<AlarmRepository> repositoryProvider,
      Provider<AlarmScheduler> schedulerProvider) {
    return new AlarmReceiver_MembersInjector(repositoryProvider, schedulerProvider);
  }

  @Override
  public void injectMembers(AlarmReceiver instance) {
    injectRepository(instance, repositoryProvider.get());
    injectScheduler(instance, schedulerProvider.get());
  }

  @InjectedFieldSignature("com.sumit.clock.alarm.AlarmReceiver.repository")
  public static void injectRepository(AlarmReceiver instance, AlarmRepository repository) {
    instance.repository = repository;
  }

  @InjectedFieldSignature("com.sumit.clock.alarm.AlarmReceiver.scheduler")
  public static void injectScheduler(AlarmReceiver instance, AlarmScheduler scheduler) {
    instance.scheduler = scheduler;
  }
}
