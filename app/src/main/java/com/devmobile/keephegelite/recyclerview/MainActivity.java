package com.devmobile.keephegelite.recyclerview;

import android.app.ActionBar;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
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
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		return true;
	}

//	@Override
//	public boolean onPrepareOptionsMenu(Menu menu) {
//		MenuItem logToggle = menu.findItem(R.id.menu_color);
//		logToggle.setVisible(findViewById(R.id.sample_output) instanceof ViewAnimator);
//		logToggle.setTitle(mLogShown ? R.string.sample_hide_log : R.string.sample_show_log);
//
//		return super.onPrepareOptionsMenu(menu);
//	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
//			case R.id.menu_color:
//				Toast.makeText(this, "Menu couleur", Toast.LENGTH_SHORT).show();
//				break;
//			case R.id.menu_delete:
//				Toast.makeText(this, "Menu supprimer note", Toast.LENGTH_SHORT).show();
//				break;
//			default:
//				break;
		}
		return super.onOptionsItemSelected(item);
	}
}