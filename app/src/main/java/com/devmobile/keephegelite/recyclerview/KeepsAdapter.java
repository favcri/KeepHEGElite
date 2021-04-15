package com.devmobile.keephegelite.recyclerview;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.devmobile.keephegelite.R;
import com.devmobile.keephegelite.business.Keep;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class KeepsAdapter extends RecyclerView.Adapter<KeepsAdapter.ViewHolder> {
	private static final String TAG = "KeepsAdapter";
	private List<Keep> mKeeps;

	public static class ViewHolder extends RecyclerView.ViewHolder {
		private final TextView tvTitre;
		private final TextView tvTexte;
		private final TextView tvColor;

		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(itemView.getContext(),getTvTitre().getText().toString() + " :: " + getTvTexte().getText().toString(), Toast.LENGTH_SHORT).show();
//					ItemFragment itemFragment = new ItemFragment();
//					Bundle args = new Bundle();
//					args.putString("Keep", getTvTitre().getText().toString());
//					itemFragment.setArguments(args);
				}
			});
			this.tvTitre = (TextView) itemView.findViewById(R.id.Keep_Titre);
			this.tvTexte = (TextView) itemView.findViewById(R.id.Keep_Texte);
			this.tvColor = (TextView) itemView.findViewById(R.id.Keep_Color);
		}

		public void setBackgroundColor (String color) {
			color = "#" + color;
			this.tvTitre.setBackgroundColor(Color.parseColor(color));
			this.tvTexte.setBackgroundColor(Color.parseColor(color));
		}

		public TextView getTvTitre() {
			return this.tvTitre;
		}
		public TextView getTvTexte() {
			return this.tvTexte;
		}
		public TextView getTvColor() {
			return this.tvColor;
		}
	}

	public KeepsAdapter(List<Keep> keeps) {
		mKeeps = keeps;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.text_row_item, viewGroup, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, final int position) {
		viewHolder.getTvTitre().setText(mKeeps.get(position).getTitre());
		viewHolder.getTvTexte().setText(mKeeps.get(position).getTexte());
//		viewHolder.getTvColor().setText(mKeeps.get(position).getBackgroundColor());
//		Log.d("L'color du keep ecrit", mKeeps.get(position).getBackgroundColor());
//		Log.d("L'color du keep", mKeeps.get(position).getBackgroundColor());
//		viewHolder.setBackgroundColor(mKeeps.get(position).getBackgroundColor());
		viewHolder.setBackgroundColor("FF00FF");
//		String color = "#" + "FF00FF";
//		viewHolder.getTvTitre().setBackgroundColor(Color.parseColor(color));
//		Log.d("L'titre du keep", mKeeps.get(position).getTitre());
//		Log.d("L'texte du keep", mKeeps.get(position).getTexte());
	}

	@Override
	public int getItemCount() {
		if (mKeeps == null)
			return 0;
		else
			return mKeeps.size();
	}
}