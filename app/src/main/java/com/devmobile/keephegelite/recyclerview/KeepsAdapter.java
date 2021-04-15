package com.devmobile.keephegelite.recyclerview;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
			StringBuilder sbColor = new StringBuilder();
			if (!color.substring(0, 0).contains("#"))
				sbColor.append("#");
			sbColor.append(color);
			color = sbColor.toString();
			itemView.setBackgroundColor(Color.parseColor(color));
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
		viewHolder.setBackgroundColor(mKeeps.get(position).getColor());
	}

	@Override
	public int getItemCount() {
		if (mKeeps == null)
			return 0;
		else
			return mKeeps.size();
	}
}