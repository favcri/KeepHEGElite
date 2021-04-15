package com.devmobile.keephegelite.recyclerview;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.devmobile.keephegelite.R;
import com.devmobile.keephegelite.business.Keep;
import com.devmobile.keephegelite.storage.KeepDBHelper;

@RequiresApi(api = Build.VERSION_CODES.O)
public class KeepAffichage extends AppCompatActivity {
	private Keep keep;
	private KeepDBHelper db;
	private TextView tvTitre;
	private EditText texte;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = new KeepDBHelper(this);
		setContentView(R.layout.activity_keep_affichage);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			int numKeep = getIntent().getIntExtra("Keep", 0);
			Log.d("L'numKeep a la sortie", String.valueOf(numKeep));
			tvTitre = (TextView) findViewById(R.id.Affichage_Keep_Titre);
			if (numKeep == 0)
				tvTitre.setText("Pas de Keep trouve...");
			else {
				keep = db.getKeep(numKeep);
				View view = findViewById(R.id.Affichage_Keep);
				StringBuilder sbColor = new StringBuilder();
				if (!keep.getColor().substring(0, 0).contains("#"))
					sbColor.append("#");
				sbColor.append(keep.getColor());
				view.setBackgroundColor(Color.parseColor(sbColor.toString()));
				tvTitre.setText(keep.getTitre());
				tvTitre = (TextView) findViewById(R.id.Affichage_Keep_Titre);
				texte = (EditText) findViewById(R.id.Affichage_Keep_Texte);
				texte.setText(keep.getTexte());
			}
		}
	}

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if ((keyCode == KeyEvent.KEYCODE_BACK))
//			finish();
//		return super.onKeyDown(keyCode, event);
//	}
}