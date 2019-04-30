package com.example.baking.ui.recipedetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.example.baking.base.BaseActivity;
import com.example.baking.R;
import com.example.baking.injection.RecipeApplication;
import com.example.baking.models.RecipeModel;
import com.example.baking.repository.RecipeRepository;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowIngredientsActivity extends BaseActivity {

  @Inject
  RecipeRepository recipeRepository;

  @BindView(R.id.table)
  TableLayout table;
  @BindView(R.id.tv_title)
  TextView title;

  private int recipeId;
  RecipeModel recipe;

  private static String RECIPE_ID = "RECIPE_ID";

  public static Intent getActivityIntent(Context context, int recipeId) {
    Intent i = new Intent(context, ShowIngredientsActivity.class);
    i.putExtra(RECIPE_ID, recipeId);
    return i;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_show_ingredients);
    ButterKnife.bind(this);
    ((RecipeApplication) getApplication()).getAppComponent().inject(this);
    getExtras();
    createTable();
  }

  private void getExtras() {
    recipeId = getIntent().getIntExtra(RECIPE_ID, 0);
    if (recipeId == 0)
      handleError();
    recipe = recipeRepository.getDataById(recipeId);
    title.setText(recipe.getName());
  }

  void createTable(){
    table.removeAllViews();
    Stream.of(recipe.getIngredients()).forEachIndexed((n, i) -> {
      table.addView(createTableRow((n + 1)+". " +i.getQuantity() +" " + i.getMeasure() +" " + i.getIngredient()));
    });
  }

  private View createTableRow(String info) {
    TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.table_1c_basic, table, false);
    ((TextView) row.findViewById(R.id.label)).setText(info);
    return row;
  }

}
