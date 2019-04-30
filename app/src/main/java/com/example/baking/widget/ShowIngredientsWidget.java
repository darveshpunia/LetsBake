package com.example.baking.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Parcel;
import android.widget.RemoteViews;

import com.example.baking.R;
import com.example.baking.injection.RecipeApplication;
import com.example.baking.models.RecipeModel;
import com.example.baking.repository.RecipeRepository;
import com.example.baking.utils.Constants;
import com.example.baking.utils.Utils;
import com.google.gson.Gson;

import org.parceler.Parcels;

import javax.inject.Inject;

import okhttp3.internal.Util;

import static android.content.Context.MODE_PRIVATE;

/**
 * Implementation of App Widget functionality.
 */
public class ShowIngredientsWidget extends AppWidgetProvider {

  SharedPreferences sharedPreferences;
  private RecipeModel recipeModel;
  public static final String EXTRA_RECIPE_ID = "EXTRA_RECIPE_ID";

  static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, RecipeModel recipeModel) {
    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.show_ingredients_widget);
    Intent serviceIntent = new Intent(context, IngredientsWidgetService.class);
    serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
    views.setRemoteAdapter(R.id.list_view, serviceIntent);
    views.setTextViewText(R.id.tv_recipe_name, recipeModel.getName());
    appWidgetManager.updateAppWidget(appWidgetId, views);
  }

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//    ((RecipeApplication) context.getApplicationContext()).getAppComponent().inject(this);
    // There may be multiple widgets active, so update all of them
    sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
    String data = sharedPreferences.getString(Constants.CURRENT_WIDGET_DATA, null);
    recipeModel = new Gson().fromJson(data, RecipeModel.class);
    if (recipeModel != null)
      for (int appWidgetId : appWidgetIds) {
        updateAppWidget(context, appWidgetManager, appWidgetId, recipeModel);
      }
  }

  @Override
  public void onEnabled(Context context) {
    // Enter relevant functionality for when the first widget is created
  }

  @Override
  public void onDisabled(Context context) {
    // Enter relevant functionality for when the last widget is disabled
  }


}

