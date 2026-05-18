package com.sumit.clock.ui.timer;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class TimerViewModel_Factory implements Factory<TimerViewModel> {
  @Override
  public TimerViewModel get() {
    return newInstance();
  }

  public static TimerViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static TimerViewModel newInstance() {
    return new TimerViewModel();
  }

  private static final class InstanceHolder {
    static final TimerViewModel_Factory INSTANCE = new TimerViewModel_Factory();
  }
}
