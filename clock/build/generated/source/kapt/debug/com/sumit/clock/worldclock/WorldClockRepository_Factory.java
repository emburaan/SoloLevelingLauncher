package com.sumit.clock.worldclock;

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
public final class WorldClockRepository_Factory implements Factory<WorldClockRepository> {
  private final Provider<Context> contextProvider;

  public WorldClockRepository_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public WorldClockRepository get() {
    return newInstance(contextProvider.get());
  }

  public static WorldClockRepository_Factory create(Provider<Context> contextProvider) {
    return new WorldClockRepository_Factory(contextProvider);
  }

  public static WorldClockRepository newInstance(Context context) {
    return new WorldClockRepository(context);
  }
}
