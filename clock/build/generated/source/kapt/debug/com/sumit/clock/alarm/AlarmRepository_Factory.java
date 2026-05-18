package com.sumit.clock.alarm;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class AlarmRepository_Factory implements Factory<AlarmRepository> {
  private final Provider<Context> contextProvider;

  public AlarmRepository_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public AlarmRepository get() {
    return newInstance(contextProvider.get());
  }

  public static AlarmRepository_Factory create(Provider<Context> contextProvider) {
    return new AlarmRepository_Factory(contextProvider);
  }

  public static AlarmRepository newInstance(Context context) {
    return new AlarmRepository(context);
  }
}
