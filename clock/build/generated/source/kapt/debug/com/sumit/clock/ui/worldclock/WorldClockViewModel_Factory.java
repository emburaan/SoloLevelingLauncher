package com.sumit.clock.ui.worldclock;

import com.sumit.clock.worldclock.WorldClockRepository;
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
public final class WorldClockViewModel_Factory implements Factory<WorldClockViewModel> {
  private final Provider<WorldClockRepository> repositoryProvider;

  public WorldClockViewModel_Factory(Provider<WorldClockRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public WorldClockViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static WorldClockViewModel_Factory create(
      Provider<WorldClockRepository> repositoryProvider) {
    return new WorldClockViewModel_Factory(repositoryProvider);
  }

  public static WorldClockViewModel newInstance(WorldClockRepository repository) {
    return new WorldClockViewModel(repository);
  }
}
