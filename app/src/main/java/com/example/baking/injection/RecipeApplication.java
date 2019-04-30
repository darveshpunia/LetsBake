package com.example.baking.injection;

import android.app.Application;

public class RecipeApplication extends Application {
  private AppComponent appComponent;

  @Override
  public void onCreate() {
    super.onCreate();
  }

  public AppComponent getAppComponent() {
    if (appComponent == null) {
      appComponent = DaggerAppComponent.builder()
          .networkModule(new NetworkModule((RecipeApplication) this.getApplicationContext()))
          .build();
    }
    return appComponent;
  }
}
