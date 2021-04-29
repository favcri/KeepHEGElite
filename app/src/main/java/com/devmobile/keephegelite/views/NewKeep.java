package com.devmobile.keephegelite.views;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.devmobile.keephegelite.R;
import com.devmobile.keephegelite.business.Keep;
import com.devmobile.keephegelite.storage.KeepDBHelper;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.Calendar;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class NewKeep extends AppCompatActivity {
	private KeepDBHelper db;
	private EditText titre;
	private EditText texte;
	private Button date;
	private Button color;
	private Button save;
	private String colorFinal;
	private String dateFinal;
	private String tagFinal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_keep);
		db = new KeepDBHelper(this);
		titre = (EditText) findViewById(R.id.New_Keep_Titre);
		texte = (EditText) findViewById(R.id.New_Keep_Texte);
		save = (Button) findViewById(R.id.New_Keep_Save);
		titre.setHint("Votre titre ici");
		texte.setHint("Votre texte ici");
		save.setText("Enregistrer le Keep");
		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
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
		menu.findItem(R.id.menu_delete).setVisible(false);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.menu_tag:
				AlertDialog.Builder builderSingle = new AlertDialog.Builder(NewKeep.this);
				builderSingle.setTitle("Sélectionnez un tag existant");
				final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(NewKeep.this, android.R.layout.simple_list_item_1);
				List<String> tags = db.getAllTags();
				arrayAdapter.addAll(tags);
				builderSingle.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				builderSingle.setNeutralButton("Ajouter un tag", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						AlertDialog.Builder tagDialog = new AlertDialog.Builder(NewKeep.this);
						tagDialog.setTitle("Saisissez votre tag");
						final EditText newTag = new EditText(NewKeep.this);
						tagDialog.setView(newTag);
						tagDialog.setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								tagFinal = newTag.getText().toString();
								dialog.cancel();
							}
						});
						tagDialog.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						});
						tagDialog.show();
					}
				});
				builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						tagFinal = arrayAdapter.getItem(which);
						dialog.cancel();
					}
				});
				builderSingle.show();
				return true;
			case R.id.menu_color:
				ColorPickerDialogBuilder
						.with(NewKeep.this)
						.setTitle("Choisissez votre couleur de fond")
						.initialColor(0xFFF)
						.wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
						.density(12)
						.setPositiveButton("OK", new ColorPickerClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
								findViewById(R.id.New_Keep).setBackgroundColor(selectedColor);
								colorFinal = Integer.toHexString(selectedColor);
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
			case R.id.menu_date:
				DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						StringBuilder sb = new StringBuilder();
						sb.append(year).append(monthOfYear).append(dayOfMonth);
						dateFinal = sb.toString();
					}
				};
				Calendar c = Calendar.getInstance();
				DatePickerDialog datePickerDialog = new DatePickerDialog(NewKeep.this, dateSetListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
				datePickerDialog.show();
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (!titre.getText().toString().isEmpty() || !texte.getText().toString().isEmpty()) {
			if (colorFinal == null) // Init la couleur à blanc si c'est vide
				db.insertKeep(new Keep(titre.getText().toString(), texte.getText().toString(), "FFFFFF", tagFinal, dateFinal));
			else
				db.insertKeep(new Keep(titre.getText().toString(), texte.getText().toString(), colorFinal, tagFinal, dateFinal));
		}
	}
}