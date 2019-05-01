package com.example.baking.ui.recipedetails;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.baking.base.BaseActivity;
import com.example.baking.R;
import com.example.baking.injection.RecipeApplication;
import com.example.baking.models.RecipeModel;
import com.example.baking.repository.RecipeRepository;
import com.example.baking.utils.Utils;
import com.example.player_lib.VideoPlayerFragment;

import javax.inject.Inject;

public class RecipeDetailsActivity extends BaseActivity implements RecipeListFragment.IFragListener {

  private static String RECIPE_ID = "RECIPE_ID";
  private int recipeId;

  RecipeModel recipe;

  @Inject
  RecipeRepository recipeRepository;
  private LinearLayout.LayoutParams params;

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
    if (savedInstanceState == null) {
      getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.frame_video, VideoPlayerFragment.newInstance(recipe.getSteps().get(0).getVideoURL()))
        .commit();

      getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.frame_steps, RecipeListFragment.newInstance(recipe.getId()))
        .commit();
    }
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
      params = (LinearLayout.LayoutParams) findViewById(R.id.frame_video).getLayoutParams();
      params.width = ViewGroup.LayoutParams.MATCH_PARENT;
      params.height = ViewGroup.LayoutParams.MATCH_PARENT;
      findViewById(R.id.frame_video).setLayoutParams(params);
    } else {
      params = (LinearLayout.LayoutParams) findViewById(R.id.frame_video).getLayoutParams();
      params.width = ViewGroup.LayoutParams.MATCH_PARENT;
      params.height = 0;
    }
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
