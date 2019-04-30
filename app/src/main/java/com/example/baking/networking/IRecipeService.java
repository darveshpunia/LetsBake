package com.example.baking.networking;

import com.example.baking.models.RecipeModel;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IRecipeService {

  @GET("topher/2017/May/59121517_baking/baking.json")
  Flowable<List<RecipeModel>> getData();


}
