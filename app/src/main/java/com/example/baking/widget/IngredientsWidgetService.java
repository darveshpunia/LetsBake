package com.example.baking.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.baking.R;
import com.example.baking.injection.RecipeApplication;
import com.example.baking.models.RecipeModel;
import com.example.baking.utils.Constants;
import com.example.baking.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;

public class IngredientsWidgetService extends RemoteViewsService {

  RecipeModel recipeModel;

  @Inject @Named("widgetSubject")
  PublishSubject<RecipeModel> recipeModelPublishSubject;

  CompositeDisposable compositeDisposable = new CompositeDisposable();
  SharedPreferences sharedPreferences;

  @Override
  public RemoteViewsFactory onGetViewFactory(Intent intent) {
    ((RecipeApplication) getApplication()).getAppComponent().inject(this);
    sharedPreferences = getApplication().getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
    String data = sharedPreferences.getString(Constants.CURRENT_WIDGET_DATA, null);
    recipeModel = new Gson().fromJson(data, RecipeModel.class);
    return new RecipeWidgetItemFactory(getApplicationContext(), recipeModel);
  }

  class RecipeWidgetItemFactory implements RemoteViewsFactory {
    Context context;
    RecipeModel recipeModel;

    RecipeWidgetItemFactory (Context context, RecipeModel recipeModel) {
      this.context = context;
      this.recipeModel = recipeModel;
    }

    @Override
    public void onCreate() {
      // listen to click events when an ingredient is selected from the home screen and update the sharedPref data
      compositeDisposable.add(
        recipeModelPublishSubject
          .subscribe(data -> {
            sharedPreferences.edit().putString(Constants.CURRENT_WIDGET_DATA, new GsonBuilder().create().toJson(data)).apply();
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, ShowIngredientsWidget.class));
            if (appWidgetIds.length > 0)
              appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_view);
            for (int w: appWidgetIds) {
              ShowIngredientsWidget.updateAppWidget(context, appWidgetManager, w, data);
            }
          }, Utils::captureException)
      );
    }

    @Override
    public void onDataSetChanged() {}

    @Override
    public void onDestroy() {
      compositeDisposable.clear();
    }

    @Override
    public int getCount() {
      return recipeModel.getIngredients().size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
      RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_item);
      views.setTextViewText(R.id.tv_wg_label, (position + 1) + ". " +recipeModel.getIngredients().get(position).getQuantity() +" " + recipeModel.getIngredients().get(position).getMeasure() + " " + recipeModel.getIngredients().get(position).getIngredient());
      return views;
    }

    @Override
    public RemoteViews getLoadingView() {
      return null;
    }

    @Override
    public int getViewTypeCount() {
      return 1;
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public boolean hasStableIds() {
      return true;
    }
  }

}
