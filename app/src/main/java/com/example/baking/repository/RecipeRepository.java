package com.example.baking.repository;

import android.util.SparseArray;
import android.util.SparseIntArray;

import com.annimon.stream.Stream;
import com.example.baking.models.RecipeModel;

import java.util.ArrayList;
import java.util.List;

/* In memory repository for recipes */
public class RecipeRepository {
  /* keyed by id */
  private SparseArray<RecipeModel> recipes;
  private List<RecipeModel> recipeList;

  public RecipeRepository () {
    recipes = new SparseArray<>();
  }

  public void addData (List<RecipeModel> recipeModel) {
    recipeList = new ArrayList<>(recipeModel);
    Stream.of(recipeModel).forEach(model -> recipes.put(model.getId(), model));
  }

  public List<RecipeModel> getRawData () {
    return recipeList;
  }

  public RecipeModel getDataById (int id) {
    return recipes.get(id);
  }

}
