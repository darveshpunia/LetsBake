package com.example.baking.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.google.gson.GsonBuilder;

public class Utils {
  public static void logPrettyJson(Object jsonData){
    Log.d("darvesh", new GsonBuilder().setPrettyPrinting().create().toJson(jsonData));
  }

  public static Intent getPlayStoreIntent(String url) {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    return intent;
  }

  public static void captureException(Throwable ex){
    ex.printStackTrace();
  }

  public static boolean isNetworkAvailable(Context context) {
    ConnectivityManager connectivityManager =
      (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
  }
}
