package com.example.baking.injection;

import com.example.baking.ui.main.MainActivity;
import com.example.baking.ui.recipedetails.RecipeDetailsActivity;
import com.example.baking.ui.recipedetails.RecipeListFragment;
import com.example.baking.ui.recipedetails.ShowIngredientsActivity;
import com.example.baking.widget.IngredientsWidgetService;
import com.example.baking.widget.ShowIngredientsWidget;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NetworkModule.class})
public interface AppComponent {
  void inject(MainActivity mainActivity);
  void inject(RecipeDetailsActivity recipeDetailsActivity);
  void inject(RecipeListFragment recipeListFragment);
  void inject(ShowIngredientsActivity showIngredientsActivity);
  void inject(IngredientsWidgetService ingredientsWidgetService);
  void inject(ShowIngredientsWidget showIngredientsWidget);
}