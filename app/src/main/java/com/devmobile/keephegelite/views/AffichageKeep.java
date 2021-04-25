package com.devmobile.keephegelite.views;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.devmobile.keephegelite.R;
import com.devmobile.keephegelite.business.Keep;
import com.devmobile.keephegelite.recyclerview.RecyclerViewFragment;
import com.devmobile.keephegelite.storage.KeepDBHelper;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.Calendar;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AffichageKeep extends AppCompatActivity {
	private static Keep k;
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
			k = keep;
			View view = findViewById(R.id.Affichage_Keep);
			view.setBackgroundColor(Color.parseColor(formatCouleur(keep.getColor())));
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
		if (keep.getDateLimite() == null)
			menu.findItem(R.id.menu_date).setTitle("Ajouter une date");
		else
			menu.findItem(R.id.menu_date).setTitle("Changer la date");
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.menu_color:
				ColorPickerDialogBuilder
						.with(this)
						.setTitle("Choisissez votre couleur de fond")
						.initialColor(0xFFF)
						.wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
						.density(12)
						.setPositiveButton("OK", new ColorPickerClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
								keep.setColor(Integer.toHexString(selectedColor));
								findViewById(R.id.Affichage_Keep).setBackgroundColor(selectedColor);
							}
						})
						.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
							}
						})
						.showColorEdit(false)
						.build()
						.show();
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
			case R.id.menu_date:
				DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						StringBuilder sb = new StringBuilder();
						sb.append(year).append(monthOfYear).append(dayOfMonth);
						keep.setDateLimite(sb.toString());
					}
				};
				Calendar c = Calendar.getInstance();
				DatePickerDialog datePickerDialog = new DatePickerDialog(AffichageKeep.this, dateSetListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
				datePickerDialog.show();
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
//		if (!titre.getText().toString().equals(k.getTitre()) || !texte.getText().toString().equals(k.getTexte()) || keep.getDateLimite() != k.getDateLimite()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Voulez-vous garder vos modifications ?");
			builder.setPositiveButton("Enregistrer", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					db.updateKeep(keep.getNumKeep(), titre.getText().toString(), texte.getText().toString(), keep.getColor(), keep.getDateLimite());
					AffichageKeep.super.onBackPressed();
				}
			});
			builder.setNegativeButton("Annuler les modifications", new DialogInterface.OnClickListener() {
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
//		}
	}

	protected String formatCouleur (String color) { // Pour éviter des bugs de parsing
		StringBuilder sbColor = new StringBuilder();
		if (!keep.getColor().substring(0, 0).contains("#"))
			sbColor.append("#");
		sbColor.append(color);
		return sbColor.toString();
	}
}