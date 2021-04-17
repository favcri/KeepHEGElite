package com.devmobile.keephegelite.views;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.devmobile.keephegelite.R;
import com.devmobile.keephegelite.business.Keep;
import com.devmobile.keephegelite.storage.KeepDBHelper;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.Calendar;

// TODO : Ne pas ajouter si vide !!!

@RequiresApi(api = Build.VERSION_CODES.O)
public class NewKeep extends AppCompatActivity {
	private KeepDBHelper db;
	private EditText titre;
	private EditText texte;
	private Button date;
	private Button color;
	private String colorStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_keep);
		db = new KeepDBHelper(this);
		titre = (EditText) findViewById(R.id.New_Keep_Titre);
		texte = (EditText) findViewById(R.id.New_Keep_Texte);
		date = (Button) findViewById(R.id.New_Keep_Date);
		color = (Button) findViewById(R.id.New_Keep_Color);
		titre.setHint("Votre titre ici");
		texte.setHint("Votre texte ici");
		date.setText("Ajouter une date");
		date.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						StringBuilder sb = new StringBuilder();
						sb.append(year).append(monthOfYear).append(dayOfMonth);
						date.setText(sb.toString());
					}
				};
				Calendar c = Calendar.getInstance();
				DatePickerDialog datePickerDialog = new DatePickerDialog(NewKeep.this, dateSetListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
				datePickerDialog.show();
			}
		});
		color.setText("Ajouter une couleur");
		color.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ColorPickerDialogBuilder
						.with(NewKeep.this)
						.setTitle("Choisissez votre couleur de fond")
						.initialColor(0xFFF)
						.wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
						.density(12)
						.setPositiveButton("OK", new ColorPickerClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
								color.setBackgroundColor(selectedColor);
								colorStr = Integer.toHexString(selectedColor);
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
			Log.d("L'color new", colorStr);
			db.insertKeep(new Keep(titre.getText().toString(), texte.getText().toString(), colorStr, date.getText().toString()));
		}
	}
}