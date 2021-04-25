package com.devmobile.keephegelite.common.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.devmobile.keephegelite.R;
import com.devmobile.keephegelite.recyclerview.MainActivity;

public class SplashScreen extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run(){
				Intent spIntent = new Intent(SplashScreen.this, MainActivity.class);
				startActivity(spIntent);
				finish();
			}
		}, 500);
	}
}