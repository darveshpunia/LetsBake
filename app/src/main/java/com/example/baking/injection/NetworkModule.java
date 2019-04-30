package com.example.baking.injection;

import com.example.baking.BuildConfig;
import com.example.baking.models.RecipeModel;
import com.example.baking.networking.IRecipeService;
import com.example.baking.repository.RecipeRepository;
import com.example.baking.utils.Constants;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.subjects.PublishSubject;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

  private final RecipeApplication application;

  public NetworkModule(RecipeApplication application) {
    this.application = application;
  }

  private final int TIMEOUT = 60;

  @Singleton
  @Provides
  OkHttpClient provideOkHttpClient(){
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
    return new OkHttpClient.Builder()
        .addInterceptor(logging)
        .readTimeout(TIMEOUT, TimeUnit.SECONDS)
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        .build();
  }

  @Singleton
  @Provides
  Retrofit provideRetrofit(OkHttpClient client) {
    return new Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(client)
        .build();
  }

  @Singleton
  @Provides
  IRecipeService provideM99Service(Retrofit retrofit) {
    return retrofit.create(IRecipeService.class);
  }

  @Singleton
  @Provides
  RecipeRepository provideRecipeRepo() {
    return new RecipeRepository();
  }

  @Singleton
  @Provides @Named("widgetSubject")
  PublishSubject<RecipeModel> widgetSubject() {
    return PublishSubject.create();
  }
}
