package com.devmobile.keephegelite.views;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.devmobile.keephegelite.R;
import com.devmobile.keephegelite.business.Keep;
import com.devmobile.keephegelite.storage.KeepDBHelper;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AffichageKeep extends AppCompatActivity {
	int SELECT_PHOTO = 1;
	private static Keep k;
	private Keep keep;
	private KeepDBHelper db;
	private EditText titre;
	private EditText texte;
	private TextView date;
	private TextView tag;
	private ImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = new KeepDBHelper(this);
		setContentView(R.layout.activity_keep_affichage);
		date = (TextView) findViewById(R.id.Affichage_Keep_Date);
		date.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				keep.setDateLimite(null);
				date.setText(null);
				findViewById(R.id.Affichage_Keep_Titre_Date).setVisibility(View.GONE);
				return true;
			}
		});
		tag = (TextView) findViewById(R.id.Affichage_Keep_Tag);
		tag.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				keep.setDateLimite(null);
				tag.setText(null);
				findViewById(R.id.Affichage_Keep_Titre_Tag).setVisibility(View.GONE);
				return true;
			}
		});
		imageView = (ImageView) findViewById(R.id.Affichage_Keep_Image);
		imageView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(AffichageKeep.this);
				builder.setMessage("Voulez-vous supprimer la photo du Keep ?");
				builder.setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						imageView.setImageURI(null);
						keep.setImagePath(null);
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
		titre = (EditText) findViewById(R.id.Affichage_Keep_Titre);
		titre.setFocusable(false);
		texte = (EditText) findViewById(R.id.Affichage_Keep_Texte);
		texte.setFocusable(false);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			int numKeep = getIntent().getIntExtra("Keep", 0);
			keep = db.getKeep(numKeep);
//			Log.d("L'uri end", keep.getImagePath());
			Log.d("L'atomic", String.valueOf(keep.getNumKeep()));
			k = keep;
			View view = findViewById(R.id.Affichage_Keep);
			view.setBackgroundColor(Color.parseColor(formatCouleur(keep.getColor())));
			titre.setText(keep.getTitre());
			texte.setText(keep.getTexte());
			modifiable (titre);
			modifiable (texte);
			if (keep.getDateLimite() == null)
				findViewById(R.id.Affichage_Keep_Titre_Date).setVisibility(View.GONE);
			else
				date.setText(keep.getDateLimite());
			if (keep.getTag() == null)
				findViewById(R.id.Affichage_Keep_Titre_Tag).setVisibility(View.GONE);
			else
				tag.setText(keep.getTag());
			if (keep.getImagePath() != null) {
//				Bitmap bitmap = BitmapFactory.decodeFile(keep.getImagePath());
//				imageView.setImageBitmap(bitmap);
//				imageView.setVisibility(View.VISIBLE);
				imageView.setImageURI(Uri.parse(keep.getImagePath()));
			}
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
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null) {
			Uri uri = data.getData();
			keep.setImagePath(uri.toString());
			imageView.setImageURI(uri);
//			imageFinal = uri.toString();
		}
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
		if (keep.getImagePath() == null)
			menu.findItem(R.id.menu_image).setTitle("Ajouter une image");
		else
			menu.findItem(R.id.menu_image).setTitle("Modifier l'image");
		if (keep.getTag() == null)
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
			case R.id.menu_tag:
				if (keep.getTag() == null) {
					android.app.AlertDialog.Builder builderSingle = new android.app.AlertDialog.Builder(AffichageKeep.this);
					builderSingle.setTitle("Sélectionnez un tag existant");
					final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AffichageKeep.this, android.R.layout.simple_list_item_1);
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
							android.app.AlertDialog.Builder tagDialog = new android.app.AlertDialog.Builder(AffichageKeep.this);
							tagDialog.setTitle("Saisissez votre tag");
							final EditText newTag = new EditText(AffichageKeep.this);
							newTag.setSingleLine();
							tagDialog.setView(newTag);
							tagDialog.setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									keep.setTag(newTag.getText().toString().toUpperCase());
									tag.setText(keep.getTag());
									findViewById(R.id.Affichage_Keep_Titre_Tag).setVisibility(View.VISIBLE);
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
							keep.setTag(arrayAdapter.getItem(which));
							tag.setText(keep.getTag());
							dialog.cancel();
						}
					});
					builderSingle.show();
					return true;
				}
				else {
					android.app.AlertDialog.Builder builderSingle = new android.app.AlertDialog.Builder(AffichageKeep.this);
					builderSingle.setTitle("Sélectionnez un tag existant");
					final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AffichageKeep.this, android.R.layout.simple_list_item_1);
					List<String> tags = db.getAllTags();
					arrayAdapter.addAll(tags);
					builderSingle.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
					builderSingle.setNeutralButton("Supprimez le tag en cours", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							keep.setTag(null);
							tag.setText(null);
							findViewById(R.id.Affichage_Keep_Titre_Tag).setVisibility(View.GONE);
							dialog.cancel();
						}
					});
					builderSingle.setNeutralButton("Ajouter un tag", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							android.app.AlertDialog.Builder tagDialog = new android.app.AlertDialog.Builder(AffichageKeep.this);
							tagDialog.setTitle("Saisissez votre tag");
							final EditText newTag = new EditText(AffichageKeep.this);
							newTag.setSingleLine();
							tagDialog.setView(newTag);
							tagDialog.setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									keep.setTag(newTag.getText().toString().toUpperCase());
									tag.setText(keep.getTag());
									findViewById(R.id.Affichage_Keep_Titre_Tag).setVisibility(View.VISIBLE);
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
							keep.setTag(arrayAdapter.getItem(which));
							tag.setText(keep.getTag());
							findViewById(R.id.Affichage_Keep_Titre_Tag).setVisibility(View.VISIBLE);
							dialog.cancel();
						}
					});
					builderSingle.show();
					return true;
				}
			case R.id.menu_color:
				ColorPickerDialogBuilder
						.with(this)
						.setTitle("Choisissez votre couleur de fond")
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
						keep.setDateLimite(sb.append(year).append("-").append(monthOfYear+1).append("-").append(dayOfMonth).toString());
						date.setText(keep.getDateLimite());
						findViewById(R.id.Affichage_Keep_Titre_Date).setVisibility(View.VISIBLE);
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
					db.updateKeep(keep.getNumKeep(), titre.getText().toString(), texte.getText().toString(), keep.getColor(), keep.getTag(), keep.getDateLimite(), keep.getImagePath());
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