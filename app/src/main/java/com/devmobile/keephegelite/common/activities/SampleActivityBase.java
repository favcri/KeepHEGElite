package com.devmobile.keephegelite.common.activities;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

public class SampleActivityBase extends FragmentActivity {
	public static final String TAG = "SampleActivityBase";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}
}