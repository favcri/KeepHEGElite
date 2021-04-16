package com.devmobile.keephegelite.recyclerview;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
//		findViewById(R.id.fab_main).setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(getApplicationContext(), KeepAffichage.class);
//					intent.putExtra("KeepTitre", tvTitre.getText().toString());
//					intent.putExtra("Keep", numKeep.getText().toString());
//				intent.putExtra("Keep", keep.getNumKeep());
//				startActivity(intent);
//			}
//		});

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
//		ActionBar actionBar = getActionBar();
//		actionBar.setDisplayHomeAsUpEnabled(true);
		return true;
	}

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