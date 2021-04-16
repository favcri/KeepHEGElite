package com.devmobile.keephegelite.views;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.devmobile.keephegelite.R;
import com.devmobile.keephegelite.business.Keep;
import com.devmobile.keephegelite.storage.KeepDBHelper;

@RequiresApi(api = Build.VERSION_CODES.O)
public class KeepAffichage extends AppCompatActivity {
	private Keep keep;
	private KeepDBHelper db;
	private EditText titre	;
	private EditText texte;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = new KeepDBHelper(this);
		setContentView(R.layout.activity_keep_affichage);
//		findViewById(R.id.menu_color).setVisibility(View.VISIBLE);
		Bundle extras = getIntent().getExtras();
		titre = (EditText) findViewById(R.id.Affichage_Keep_Titre);
		texte = (EditText) findViewById(R.id.Affichage_Keep_Texte);
		if (extras != null) {
			int numKeep = getIntent().getIntExtra("Keep", 0);
			Log.d("L'numKeep a la sortie", String.valueOf(numKeep));
			if (numKeep == 0) {
				titre.setHint("Votre titre ici");
				texte.setHint("Votre texte ici");
			}
			else {
				keep = db.getKeep(numKeep);
				View view = findViewById(R.id.Affichage_Keep);
				StringBuilder sbColor = new StringBuilder();
				if (!keep.getColor().substring(0, 0).contains("#"))
					sbColor.append("#");
				sbColor.append(keep.getColor());
				view.setBackgroundColor(Color.parseColor(sbColor.toString()));
				titre.setText(keep.getTitre());
				texte.setText(keep.getTexte());
			}
		}
		else {
			titre.setHint("Votre titre ici");
			texte.setHint("Votre texte ici");
		}
	}
}