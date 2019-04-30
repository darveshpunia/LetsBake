package com.example.baking.ui.recipedetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.baking.base.BaseActivity;
import com.example.baking.R;
import com.example.baking.injection.RecipeApplication;
import com.example.baking.models.RecipeModel;
import com.example.baking.repository.RecipeRepository;
import com.example.player_lib.VideoPlayerFragment;

import javax.inject.Inject;

public class RecipeDetailsActivity extends BaseActivity implements RecipeListFragment.IFragListener {

  private static String RECIPE_ID = "RECIPE_ID";
  private int recipeId;

  RecipeModel recipe;

  @Inject
  RecipeRepository recipeRepository;

  public static Intent getActivityIntent(Context context, int recipeId) {
    Intent i = new Intent(context, RecipeDetailsActivity.class);
    i.putExtra(RECIPE_ID, recipeId);
    return i;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe_details);
    ((RecipeApplication) getApplication()).getAppComponent().inject(this);
    getExtras();
    getSupportFragmentManager()
      .beginTransaction()
      .replace(R.id.frame_video, VideoPlayerFragment.newInstance(recipe.getSteps().get(0).getVideoURL()))
      .commit();

    getSupportFragmentManager()
      .beginTransaction()
      .replace(R.id.frame_steps, RecipeListFragment.newInstance(recipe.getId()))
      .commit();
  }

  private void getExtras() {
    recipeId = getIntent().getIntExtra(RECIPE_ID, 0);
    if (recipeId == 0)
      handleError();
    recipe = recipeRepository.getDataById(recipeId);
  }

  @Override
  public void updateURL(String Url) {
    getSupportFragmentManager()
      .beginTransaction()
      .replace(R.id.frame_video, VideoPlayerFragment.newInstance(Url))
      .commit();
  }
}
