package com.devmobile.keephegelite.views;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.devmobile.keephegelite.R;
import com.devmobile.keephegelite.business.Keep;
import com.devmobile.keephegelite.storage.KeepDBHelper;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AffichageKeep extends AppCompatActivity {
	private Keep keep;
	private KeepDBHelper db;
	private EditText titre;
	private EditText texte;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = new KeepDBHelper(this);
		setContentView(R.layout.activity_keep_affichage);
		Bundle extras = getIntent().getExtras();
		titre = (EditText) findViewById(R.id.Affichage_Keep_Titre);
		titre.setFocusable(false);
		texte = (EditText) findViewById(R.id.Affichage_Keep_Texte);
		texte.setFocusable(false);
		if (extras != null) {
			int numKeep = getIntent().getIntExtra("Keep", 0);
			keep = db.getKeep(numKeep);
			View view = findViewById(R.id.Affichage_Keep);
			StringBuilder sbColor = new StringBuilder();
			if (!keep.getColor().substring(0, 0).contains("#"))
				sbColor.append("#");
			sbColor.append(keep.getColor());
			view.setBackgroundColor(Color.parseColor(sbColor.toString()));
			titre.setText(keep.getTitre());
			texte.setText(keep.getTexte());
			modifiable (titre);
			modifiable (texte);
		}
	}

	private void modifiable(EditText editText) {
		editText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				editText.setFocusableInTouchMode(true); // Pour pouvoir éditer
			}
		});
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
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("Voulez-vous supprimer le Keep ?");
				builder.setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						db.deleteKeep(keep.getNumKeep());
						finish();
					}
				});
				builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
				builder.show();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Voulez-vous garder vos modifications ?");
		builder.setPositiveButton("Enregistrer", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				db.updateKeep(keep.getNumKeep(), titre.getText().toString(), texte.getText().toString());
				AffichageKeep.super.onBackPressed();
			}
		});
		builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				AffichageKeep.super.onBackPressed();
			}
		});
		builder.setNeutralButton("Continuer la saisie", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.show();
	}
//		AlertDialog dialog = new AlertDialog.Builder(this)
//				.setTitle("Voulez-vous garder vos modifications ?")
//				.setPositiveButton("Oui !", new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						if (!k.getTitre().equals(titre.getText().toString()) || !k.getTexte().equals(texte.getText().toString()))
//							db.updateKeep(keep.getNumKeep(), titre.getText().toString(), texte.getText().toString());
//					}
//				})
//				.setNegativeButton("Annuler", null)
//				.setNeutralButton("Continuer l'édition", new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.cancel();
//					}
//				})
//				.create();
//				dialog.show();
//	}
}