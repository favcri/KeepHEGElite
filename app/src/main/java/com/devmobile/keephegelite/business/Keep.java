package com.devmobile.keephegelite.business;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.concurrent.atomic.AtomicInteger;

import static java.sql.Types.BLOB;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Keep {
	private static AtomicInteger atomicInteger = new AtomicInteger(1);
	public static final String TABLE_NAME = "keeps";
	public static final String COLUMN_NUM = "num";
	public static final String COLUMN_NUM_KEEP = "numKeep";
	public static final String COLUMN_TITRE = "titre";
	public static final String COLUMN_TEXTE = "texte";
	public static final String COLUMN_TAG = "tag";
	public static final String COLUMN_COLOR = "bg_color";
	public static final String COLUMN_DATE = "date";
	public static final String COLUMN_IMG = "image";
	public static final String CREATE_TABLE =
			"CREATE TABLE " + TABLE_NAME + "("
					+ COLUMN_NUM + " INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ COLUMN_NUM_KEEP + " INT,"
					+ COLUMN_TITRE + " TEXT,"
					+ COLUMN_TEXTE + " TEXT,"
					+ COLUMN_TAG + " TEXT,"
					+ COLUMN_COLOR + " TEXT,"
					+ COLUMN_DATE + " TEXT,"
					+ COLUMN_DATE + BLOB
					+ ")";

	private String titre;
	private String texte;
	private String tag;
	private int numKeep; // Clé unique pour Java
	private boolean done = false;
	private String color = "FFFFFF"; // Couleur de fond blanche par défaut
	private String dateLimite;
//	private LocalDate dateLimite = LocalDate.parse("2000-01-01"); // Pour éviter un NullPointerException dans KeepsAdapter

	public Keep () {
		this.numKeep = atomicInteger.getAndIncrement();
		Log.d("L'atomic", atomicInteger.toString());
	}

	public Keep (String titre, String texte) {
		this();
		this.titre = titre;
		this.texte = texte;
	}

	public Keep (String titre, String texte, String color) {
		this ();
		this.titre = titre;
		this.texte = texte;
		this.color = color;
	}

	public Keep (String titre, String texte, String color, String tag) {
		this ();
		this.titre = titre;
		this.texte = texte;
		this.color = color;
		this.tag = tag;
	}

	public Keep (String titre, String texte, String color, String tag, String dateLimite) {
		this ();
		this.titre = titre;
		this.texte = texte;
		this.color = color;
		this.tag = tag;
		this.dateLimite = dateLimite;
	}

	public Keep (String titre, String texte, String tag, boolean done, String dateLimite) {
		this.titre = titre;
		this.texte = texte;
		this.dateLimite = dateLimite;
		this.tag = tag;
		this.done = done;
	}

	/**
	 * Constructeur seulement pour getKeep dans KeepDBHelper (ici on incrémente pas @param numKeep)
	 */
	public Keep (String titre, String texte, String color, String tag, Integer numKeep) {
		this.titre = titre;
		this.texte = texte;
		this.color = color;
		this.tag = tag;
		this.numKeep = numKeep;
	}

	public Keep(String titre, String texte, String color, String tag, int numKeep, String dateLimite) {
		this.titre = titre;
		this.texte = texte;
		this.color = color;
		this.tag = tag;
		this.numKeep = numKeep;
		this.dateLimite = dateLimite;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getTexte() {
		return texte;
	}

	public void setTexte(String texte) {
		this.texte = texte;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public String getDateLimite() {
		return dateLimite;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public void setDateLimite(String dateLimite) {
		this.dateLimite = dateLimite;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getNumKeep() {
		return numKeep;
	}

	public void setNumKeep(int numKeep) {
		this.numKeep = numKeep;
	}

	@Override
	public String toString () {
		return "Keep {" +
				"titre='" + titre + '\'' +
				", texte='" + texte + '\'' +
				", done=" + done +
				", dateLimite=" + dateLimite +
				'}';
	}
}