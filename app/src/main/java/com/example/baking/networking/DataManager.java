package com.example.baking.networking;

import com.example.baking.models.RecipeModel;
import com.example.baking.repository.RecipeRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DataManager {
  private IRecipeService service;
  private RecipeRepository recipeRepository;

  @Inject
  public DataManager(IRecipeService service, RecipeRepository recipeRepository) {
    this.service = service;
    this.recipeRepository = recipeRepository;
  }

  public Flowable<List<RecipeModel>> getData(){
    return service
      .getData()
      .doOnNext(data -> { if (data != null) recipeRepository.addData(data); })
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread());
  }

}
