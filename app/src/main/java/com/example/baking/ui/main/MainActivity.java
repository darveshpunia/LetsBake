package com.example.baking.ui.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baking.base.BaseActivity;
import com.example.baking.Custom.RvItemDecoration;
import com.example.baking.R;
import com.example.baking.injection.RecipeApplication;
import com.example.baking.models.RecipeModel;
import com.example.baking.networking.DataManager;
import com.example.baking.ui.recipedetails.RecipeDetailsActivity;
import com.example.baking.ui.recipedetails.ShowIngredientsActivity;
import com.example.baking.utils.Constants;
import com.example.baking.utils.Utils;
import com.google.gson.GsonBuilder;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;

public class MainActivity extends BaseActivity {

  @Inject
  DataManager dataManager;

  CompositeDisposable compositeDisposable = new CompositeDisposable();

  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;
  @BindView(R.id.progress_bar)
  ProgressBar progressBar;

  @Inject @Named("widgetSubject")
  PublishSubject<RecipeModel> recipeModelPublishSubject;

  SharedPreferences sharedPreferences;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    ((RecipeApplication) getApplication()).getAppComponent().inject(this);
    sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
    getData();
  }

  private void getData() {
    boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
    progressBar.setVisibility(View.VISIBLE);
    recyclerView.setVisibility(View.GONE);
    compositeDisposable.add(
      dataManager.getData()
        .subscribe(r -> {
          progressBar.setVisibility(View.GONE);
          recyclerView.setVisibility(View.VISIBLE);
          if (tabletSize) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            recyclerView.addItemDecoration(new RvItemDecoration(10));
          }
          else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
          }
          recyclerView.setAdapter(new RecipesAdapter(this, r));
          String data = sharedPreferences.getString(Constants.CURRENT_WIDGET_DATA, null);
          if (data == null)
            sharedPreferences.edit().putString(Constants.CURRENT_WIDGET_DATA, new GsonBuilder().create().toJson(r.get(0))).apply();
        }, ex -> {
          Utils.captureException(ex);
          handleError();
        })
    );
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    compositeDisposable.clear();
  }

  public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {
    private Context context;
    private List<RecipeModel> recipes;

    public RecipesAdapter(Context context, List<RecipeModel> recipes) {
      this.context = context;
      this.recipes = recipes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_recipe_item, parent, false);
      return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      holder.title.setText(recipes.get(position).getName());
      holder.steps.setText(String.valueOf(recipes.get(position).getSteps().size()));
      holder.ingredients.setText(String.valueOf(recipes.get(position).getIngredients().size()));
      holder.servings.setText(String.valueOf(recipes.get(position).getServings()) + " Servings");
      holder.layoutSteps.setOnClickListener(__->{
        context.startActivity(RecipeDetailsActivity.getActivityIntent(context, recipes.get(position).getId()));
        recipeModelPublishSubject.onNext(recipes.get(position));
        sharedPreferences.edit().putString(Constants.CURRENT_WIDGET_DATA, new GsonBuilder().create().toJson(recipes.get(position))).apply();
      });
      holder.layoutIngredients.setOnClickListener(__->{
        context.startActivity(ShowIngredientsActivity.getActivityIntent(context, recipes.get(position).getId()));
        recipeModelPublishSubject.onNext(recipes.get(position));
        sharedPreferences.edit().putString(Constants.CURRENT_WIDGET_DATA, new GsonBuilder().create().toJson(recipes.get(position))).apply();
      });
    }

    @Override
    public int getItemCount() {
      return recipes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
      TextView title;
      TextView steps;
      TextView servings;
      TextView ingredients;
      CardView layout;
      LinearLayout layoutSteps;
      LinearLayout layoutIngredients;
      ViewHolder(View view) {
        super(view);
        title = view.findViewById(R.id.text_title);
        servings = view.findViewById(R.id.text_serving_count);
        steps = view.findViewById(R.id.text_step_count);
        ingredients = view.findViewById(R.id.text_ingrd_count);
        layout = view.findViewById(R.id.layout_recipe_card);
        layoutSteps = view.findViewById(R.id.layout_steps);
        layoutIngredients = view.findViewById(R.id.layout_ingredients);
        //subTitle = view.findViewById(R.id.sub_title);
      }
    }
  }


}
