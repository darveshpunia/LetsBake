package com.example.baking.base;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baking.R;

public abstract class BaseActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  public void handleError() {
    Toast.makeText(this, getString(R.string._something_went_wrong), Toast.LENGTH_LONG).show();
    finish();
  }
}
