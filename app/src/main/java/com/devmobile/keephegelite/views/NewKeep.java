package com.devmobile.keephegelite.views;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.devmobile.keephegelite.R;
import com.devmobile.keephegelite.business.Keep;
import com.devmobile.keephegelite.storage.KeepDBHelper;

@RequiresApi(api = Build.VERSION_CODES.O)
public class NewKeep extends AppCompatActivity {
	private KeepDBHelper db;
	private EditText titre;
	private EditText texte;
	private Keep keep;

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
	public void onBackPressed() {
		super.onBackPressed();
		keep = new Keep (titre.getText().toString(), texte.getText().toString());
		Log.d("L'Keep manuel", keep.toString());
		db.insertKeep(keep);
	}
}