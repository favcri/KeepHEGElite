package com.devmobile.keephegelite.views;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.devmobile.keephegelite.R;
import com.devmobile.keephegelite.storage.KeepDBHelper;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class SelectTags extends AppCompatActivity {
	KeepDBHelper db;
	ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_tags);
		this.db = new KeepDBHelper(this);
		this.listView = (ListView) findViewById(R.id.Tous_Tags);

		List<String> tags = db.getAllTags();
		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, tags);
//		Log.d("L'salut", tags.toString());
//arrayAdapter.addAll(tags);
		this.listView.setAdapter(arrayAdapter);
	}
}