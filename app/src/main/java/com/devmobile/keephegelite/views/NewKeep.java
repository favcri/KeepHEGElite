package com.devmobile.keephegelite.views;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.devmobile.keephegelite.R;
import com.devmobile.keephegelite.business.Keep;
import com.devmobile.keephegelite.storage.KeepDBHelper;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class NewKeep extends AppCompatActivity {
	int SELECT_PHOTO = 1;
	private KeepDBHelper db;
	private EditText titre;
	private EditText texte;
	private Button buttonDate;
	private TextView date;
	private TextView tag;
	private Button buttonImage;
	private ImageView imageView;
	private Button save;
	private String colorFinal;
	private String dateFinal;
	private String timeFinal;
	private String tagFinal;
	private String imageFinal;
	private FloatingActionButton fab;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_keep);
		db = new KeepDBHelper(this);
		titre = (EditText) findViewById(R.id.New_Keep_Titre);
		texte = (EditText) findViewById(R.id.New_Keep_Texte);

		date = (TextView) findViewById(R.id.New_Keep_Date);
		date.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				dateFinal = null;
				timeFinal = null;
				date.setText(null);
				findViewById(R.id.New_Keep_Titre_Date).setVisibility(View.GONE);
				return true;
			}
		});
//		date.setVisibility(View.GONE);

		tag = (TextView) findViewById(R.id.New_Keep_Tag);
		tag.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				tagFinal = null;
				tag.setText(null);
				findViewById(R.id.New_Keep_Titre_Tag).setVisibility(View.GONE);
				return true;
			}
		});

		imageView = (ImageView) findViewById(R.id.New_Image);
		imageView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(NewKeep.this);
				builder.setMessage("Voulez-vous supprimer la photo du Keep ?");
				builder.setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						imageView.setImageURI(null);
						imageFinal = null;
						dialog.cancel();
					}
				});
				builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
				builder.show();
				return true;
			}
		});

		fab = (FloatingActionButton) findViewById(R.id.New_Keep_Fab_Save);
//		fab.setBackgroundColor(R.color.purple_500);
		fab.setImageResource(R.drawable.ic_baseline_save_24);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		titre.setHint("Votre titre ici");
		texte.setHint("Votre texte ici");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null) {
			Uri uri = data.getData();
			imageView.setImageURI(uri);
			imageFinal = uri.toString();
//			Log.d("L'uri start", imageFinal);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.findItem(R.id.menu_delete).setVisible(false);
		if (dateFinal == null)
			menu.findItem(R.id.menu_date).setTitle("Ajouter une date");
		else
			menu.findItem(R.id.menu_date).setTitle("Changer la date");
		if (timeFinal == null)
			menu.findItem(R.id.menu_time).setTitle("Ajouter une heure");
		else
			menu.findItem(R.id.menu_time).setTitle("Changer l'heure");
		if (imageFinal == null)
			menu.findItem(R.id.menu_image).setTitle("Ajouter une image");
		else
			menu.findItem(R.id.menu_image).setTitle("Modifier l'image");
		if (tagFinal == null)
			menu.findItem(R.id.menu_tag).setTitle("Ajouter un tag");
		else
			menu.findItem(R.id.menu_tag).setTitle("Modifier le tag");
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.menu_image:
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setType("image/*");
				startActivityForResult(intent, SELECT_PHOTO);
				return true;
			case R.id.menu_date:
				DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						StringBuilder sb = new StringBuilder();
						if (timeFinal == null) {
							dateFinal = sb.append(year).append("-").append(monthOfYear + 1).append("-").append(dayOfMonth).toString();
							sb = new StringBuilder();
							timeFinal = sb.append("00").append(":").append("00").toString();
						}
						else
							dateFinal = sb.append(year).append("-").append(monthOfYear+1).append("-").append(dayOfMonth).append(" ").append(timeFinal).toString();
						date.setVisibility(View.VISIBLE);
						findViewById(R.id.New_Keep_Titre_Date).setVisibility(View.VISIBLE);
						date.setText(dateFinal + " " + timeFinal);
					}
				};
				Calendar c = Calendar.getInstance();
				DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateSetListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
				datePickerDialog.show();
				return true;
			case R.id.menu_time:
				Calendar t = Calendar.getInstance();
				TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						StringBuilder sb = new StringBuilder();
						timeFinal = sb.append(hourOfDay).append(":").append(minute).toString();
						if (dateFinal == null) {
							sb = new StringBuilder();
							dateFinal = sb.append(t.get(Calendar.YEAR)).append("-").append(t.get(Calendar.MONTH)+1).append("-").append(t.get(Calendar.DAY_OF_MONTH)).toString();
							date.setText(dateFinal + " " + timeFinal);
							date.setVisibility(View.VISIBLE);
							findViewById(R.id.New_Keep_Titre_Date).setVisibility(View.VISIBLE);
						}
						else {
							date.setText(dateFinal + " " + timeFinal);
						}
					}
				};
				TimePickerDialog timePickerDialog = new TimePickerDialog(this, timeSetListener, t.get(Calendar.HOUR_OF_DAY), t.get(Calendar.MINUTE), true);
				timePickerDialog.show();
				return true;
			case R.id.menu_tag:
				AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
				builderSingle.setTitle("Sélectionnez un tag existant");
				final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
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
						newTag.setSingleLine();
						tagDialog.setView(newTag);
						tagDialog.setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								tagFinal = newTag.getText().toString();
								tag.setText(tagFinal.toUpperCase());
								findViewById(R.id.New_Keep_Titre_Tag).setVisibility(View.VISIBLE);
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
						tag.setText(tagFinal.toUpperCase());
						findViewById(R.id.New_Keep_Titre_Tag).setVisibility(View.VISIBLE);
						dialog.cancel();
					}
				});
				builderSingle.show();
				return true;
			case R.id.menu_color:
				ColorPickerDialogBuilder
						.with(this)
						.setTitle("Choisissez votre couleur de fond")
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
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
//		Log.d("L'uri insert", imageFinal);
		if (!titre.getText().toString().isEmpty() || !texte.getText().toString().isEmpty()) {
			if (colorFinal == null) // Init la couleur à blanc si c'est vide
				db.insertKeep(new Keep(titre.getText().toString(), texte.getText().toString(), "FFFFFF", tagFinal, dateFinal, imageFinal));
			else
				db.insertKeep(new Keep(titre.getText().toString(), texte.getText().toString(), colorFinal, tagFinal, dateFinal, imageFinal));
		}
	}
}