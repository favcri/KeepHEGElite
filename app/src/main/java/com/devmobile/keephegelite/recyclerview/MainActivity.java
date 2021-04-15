package com.devmobile.keephegelite.recyclerview;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentTransaction;

import com.devmobile.keephegelite.R;
import com.devmobile.keephegelite.common.activities.SampleActivityBase;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends SampleActivityBase {
	public static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			RecyclerViewFragment fragment = new RecyclerViewFragment();
			transaction.replace(R.id.sample_content_fragment, fragment);
			transaction.commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		}
		return super.onOptionsItemSelected(item);
	}
}