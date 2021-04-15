package com.devmobile.keephegelite.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.devmobile.keephegelite.business.Keep;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class KeepDBHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "keeps_db";

	public KeepDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(Keep.CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + Keep.TABLE_NAME);
		onCreate(db);
	}

	public long insertKeep(Keep keep) {
		String color = keep.getColor();
		SQLiteDatabase db = this.getWritableDatabase(); // Pour pouvoir écrire dans la BDD
		ContentValues values = new ContentValues();
		values.put(Keep.COLUMN_TITRE, keep.getTitre());
		values.put(Keep.COLUMN_TEXTE, keep.getTexte());
		values.put(Keep.COLUMN_TAG, keep.getTag());
		values.put(Keep.COLUMN_COLOR, keep.getColor());
//		Log.d("L'couleur injecte", color);
//		values.put(Keep.COLOR, color);
		Log.d("L'Les values : ", values.toString());
		long id = db.insert(Keep.TABLE_NAME, null, values); // Insertion du tuple
		db.close();
		return id;
	}

	public Keep getKeep(long id) {
		SQLiteDatabase db = this.getReadableDatabase(); // Pour pouvoir lire dans la BDD
		Cursor cursor = db.query(Keep.TABLE_NAME,
				new String[] {Keep.COLUMN_TITRE, Keep.COLUMN_TEXTE},
				Keep.COLUMN_NUM + " = ?",
				new String[]{String.valueOf(id)}, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Keep keep = new Keep(cursor.getString(cursor.getColumnIndex(Keep.COLUMN_TITRE)), cursor.getString(cursor.getColumnIndex(Keep.COLUMN_TEXTE)));
		cursor.close();
		return keep;
	}

	public List<Keep> getAllKeeps() {
		List<Keep> keeps = new ArrayList<>();
		String selectQuery = "SELECT  * FROM " + Keep.TABLE_NAME + " ORDER BY " + Keep.COLUMN_NUM + " DESC";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Keep keep = new Keep(cursor.getString(cursor.getColumnIndex(Keep.COLUMN_TITRE)), cursor.getString(cursor.getColumnIndex(Keep.COLUMN_TEXTE)));
				keeps.add(keep);
			} while (cursor.moveToNext());
		}
		db.close();
		return keeps;
	}

	public int deleteKeep (String titre) {
		SQLiteDatabase db = this.getWritableDatabase();
		int numero = db.delete(Keep.TABLE_NAME, Keep.COLUMN_TITRE + " = ?",
				new String[]{String.valueOf(titre)});
		db.close();
		return numero;
	}

	public int updateKeep (String titre, String texte) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Keep.COLUMN_TEXTE, texte);
		return db.update(Keep.TABLE_NAME, values, Keep.COLUMN_TITRE + " = ?",
				new String[]{String.valueOf(titre)});
	}
}