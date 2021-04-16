package com.devmobile.keephegelite.views;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.devmobile.keephegelite.R;
import com.devmobile.keephegelite.business.Keep;
import com.devmobile.keephegelite.storage.KeepDBHelper;

// TODO : Ne pas ajouter si vide !!!

@RequiresApi(api = Build.VERSION_CODES.O)
public class NewKeep extends AppCompatActivity {
	private KeepDBHelper db;
	private EditText titre;
	private EditText texte;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_keep);
		db = new KeepDBHelper(this);
		titre = (EditText) findViewById(R.id.New_Keep_Titre);
		texte = (EditText) findViewById(R.id.New_Keep_Texte);
		titre.setHint("Votre titre ici");
		texte.setHint("Votre texte ici");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem actionViewItem = menu.findItem(R.id.menu_color);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.menu_color:
				Toast.makeText(this, "Menu couleur", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.menu_delete:
				Toast.makeText(this, "Menu supprimer note", Toast.LENGTH_SHORT).show();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (!titre.getText().toString().isEmpty() || !texte.getText().toString().isEmpty()) {
			db.insertKeep(new Keep(titre.getText().toString(), texte.getText().toString()));
		}
	}
}