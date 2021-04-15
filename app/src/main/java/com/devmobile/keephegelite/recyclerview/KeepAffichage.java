package com.devmobile.keephegelite.recyclerview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.devmobile.keephegelite.R;

public class KeepAffichage extends AppCompatActivity {
	TextView tvTitre;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_keep_affichage);
		Bundle extras = getIntent().getExtras();
		String titre = null;
		if (extras != null) {
			titre = getIntent().getStringExtra("KeepTitre");
			Log.d("L'titre du keep dans le fragment", titre);
			this.tvTitre = (TextView) findViewById(R.id.Affichage_Keep_Titre);
			tvTitre.setText(titre);
		}
	}
}