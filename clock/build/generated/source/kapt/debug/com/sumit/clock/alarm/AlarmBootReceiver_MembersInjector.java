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
public final class AlarmBootReceiver_MembersInjector implements MembersInjector<AlarmBootReceiver> {
  private final Provider<AlarmRepository> repositoryProvider;

  private final Provider<AlarmScheduler> schedulerProvider;

  public AlarmBootReceiver_MembersInjector(Provider<AlarmRepository> repositoryProvider,
      Provider<AlarmScheduler> schedulerProvider) {
    this.repositoryProvider = repositoryProvider;
    this.schedulerProvider = schedulerProvider;
  }

  public static MembersInjector<AlarmBootReceiver> create(
      Provider<AlarmRepository> repositoryProvider, Provider<AlarmScheduler> schedulerProvider) {
    return new AlarmBootReceiver_MembersInjector(repositoryProvider, schedulerProvider);
  }

  @Override
  public void injectMembers(AlarmBootReceiver instance) {
    injectRepository(instance, repositoryProvider.get());
    injectScheduler(instance, schedulerProvider.get());
  }

  @InjectedFieldSignature("com.sumit.clock.alarm.AlarmBootReceiver.repository")
  public static void injectRepository(AlarmBootReceiver instance, AlarmRepository repository) {
    instance.repository = repository;
  }

  @InjectedFieldSignature("com.sumit.clock.alarm.AlarmBootReceiver.scheduler")
  public static void injectScheduler(AlarmBootReceiver instance, AlarmScheduler scheduler) {
    instance.scheduler = scheduler;
  }
}
