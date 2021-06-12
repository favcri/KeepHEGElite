package com.devmobile.keephegelite.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.devmobile.keephegelite.business.Keep;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class KeepDBHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 6;
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
		SQLiteDatabase db = this.getWritableDatabase(); // Pour pouvoir écrire dans la BDD
		ContentValues values = new ContentValues();
		values.put(Keep.COLUMN_TITRE, keep.getTitre());
		values.put(Keep.COLUMN_COLOR, keep.getColor());
		values.put(Keep.COLUMN_TEXTE, keep.getTexte());
		if (keep.getTag() != null)
			values.put(Keep.COLUMN_TAG, keep.getTag().toUpperCase());
		values.put(Keep.COLUMN_DATE, Keep.dateToString(keep.getDateLimite()));
		values.put(Keep.COLUMN_NUM_KEEP, keep.getNumKeep());
		values.put(Keep.COLUMN_IMG, keep.getImagePath());
		long id = db.insert(Keep.TABLE_NAME, null, values); // Insertion du tuple
		db.close();
		return id;
	}

	public Keep getKeep(long id) {
		SQLiteDatabase db = this.getReadableDatabase(); // Pour pouvoir lire dans la BDD
		Cursor cursor = db.query(Keep.TABLE_NAME,
				new String[]{Keep.COLUMN_TITRE, Keep.COLUMN_TEXTE, Keep.COLUMN_TAG, Keep.COLUMN_COLOR, Keep.COLUMN_NUM_KEEP},
				Keep.COLUMN_NUM + " = ?",
				new String[]{String.valueOf(id)}, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();
		Keep keep = new Keep(cursor.getString(cursor.getColumnIndex(Keep.COLUMN_TITRE)), cursor.getString(cursor.getColumnIndex(Keep.COLUMN_TEXTE)), cursor.getString(cursor.getColumnIndex(Keep.COLUMN_COLOR)), cursor.getString(cursor.getColumnIndex(Keep.COLUMN_TAG)), cursor.getInt(cursor.getColumnIndex(Keep.COLUMN_NUM_KEEP)));
		cursor.close();
		db.close();
		return keep;
	}

	public Keep getKeep(int numKeep) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(Keep.TABLE_NAME,
				new String[]{Keep.COLUMN_TITRE, Keep.COLUMN_TEXTE, Keep.COLUMN_TAG, Keep.COLUMN_COLOR, Keep.COLUMN_NUM_KEEP, Keep.COLUMN_DATE, Keep.COLUMN_IMG},
				Keep.COLUMN_NUM_KEEP + " = ?",
				new String[]{String.valueOf(numKeep)}, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();
		Keep keep = new Keep(cursor.getString(cursor.getColumnIndex(Keep.COLUMN_TITRE)), cursor.getString(cursor.getColumnIndex(Keep.COLUMN_TEXTE)), cursor.getString(cursor.getColumnIndex(Keep.COLUMN_COLOR)), cursor.getString(cursor.getColumnIndex(Keep.COLUMN_TAG)), cursor.getInt(cursor.getColumnIndex(Keep.COLUMN_NUM_KEEP)), Keep.stringToDate(cursor.getString(cursor.getColumnIndex(Keep.COLUMN_DATE))), cursor.getString(cursor.getColumnIndex(Keep.COLUMN_IMG)));
		cursor.close();
		db.close();
		return keep;
	}

	public List<String> getAllTags () {
		List<String> tags = new ArrayList<>();
		SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT DISTINCT " + Keep.COLUMN_TAG + " FROM " + Keep.TABLE_NAME + " ORDER BY " + Keep.COLUMN_NUM + " DESC";
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				tags.add(cursor.getString(cursor.getColumnIndex(Keep.COLUMN_TAG)));
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return tags;
	}

	public List<Keep> getAllKeeps() {
		List<Keep> keeps = new ArrayList<>();
		String selectQuery = "SELECT * FROM " + Keep.TABLE_NAME + " ORDER BY " + Keep.COLUMN_NUM + " DESC";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				keeps.add(new Keep(cursor.getString(cursor.getColumnIndex(Keep.COLUMN_TITRE)), cursor.getString(cursor.getColumnIndex(Keep.COLUMN_TEXTE)), cursor.getString(cursor.getColumnIndex(Keep.COLUMN_COLOR)), cursor.getString(cursor.getColumnIndex(Keep.COLUMN_TAG)), cursor.getInt(cursor.getColumnIndex(Keep.COLUMN_NUM_KEEP)), Keep.stringToDate(cursor.getString(cursor.getColumnIndex(Keep.COLUMN_DATE))), cursor.getString(cursor.getColumnIndex(Keep.COLUMN_IMG))));
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return keeps;
	}

	public int deleteKeep(String titre) {
		SQLiteDatabase db = this.getWritableDatabase();
		int numero = db.delete(Keep.TABLE_NAME, Keep.COLUMN_TITRE + " = ?", new String[]{String.valueOf(titre)});
		db.close();
		return numero;
	}

	public int deleteKeep(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		int numero = db.delete(Keep.TABLE_NAME, Keep.COLUMN_NUM_KEEP + " = ?", new String[]{String.valueOf(id)});
		db.close();
		return numero;
	}

	public int updateKeep(int id, String titre, String texte, String color, String tag, String localDate, String imagePath) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Keep.COLUMN_TITRE, titre);
		values.put(Keep.COLUMN_TEXTE, texte);
		values.put(Keep.COLUMN_COLOR, color);
		values.put(Keep.COLUMN_TAG, tag);
		values.put(Keep.COLUMN_DATE, localDate);
		values.put(Keep.COLUMN_IMG, imagePath);
		return db.update(Keep.TABLE_NAME, values, Keep.COLUMN_NUM_KEEP + " = ?", new String[]{String.valueOf(id)});
	}
}