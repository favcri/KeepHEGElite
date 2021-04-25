package com.devmobile.keephegelite.recyclerview;

import android.content.Intent;
import android.graphics.Color;
import android.location.GnssAntennaInfo;
import android.net.sip.SipSession;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devmobile.keephegelite.R;
import com.devmobile.keephegelite.business.Keep;
import com.devmobile.keephegelite.storage.KeepDBHelper;
import com.devmobile.keephegelite.views.AffichageKeep;

import java.util.List;

// TODO : Faire pour que le RecyclerView se refresh tout seul quand il y a changement (ou clique sur le tag)

@RequiresApi(api = Build.VERSION_CODES.O)
public class KeepsAdapter extends RecyclerView.Adapter<KeepsAdapter.ViewHolder> {
	private static final String TAG = "KeepsAdapter";
	private List<Keep> mKeeps;
	private KeepDBHelper db;
	private static View.OnClickListener mOnItemClickListener;

	public KeepsAdapter(List<Keep> keeps) {
		mKeeps = keeps;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.keep_row_item, viewGroup, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, final int position) {
		viewHolder.keep = mKeeps.get(position);
//		viewHolder.getbTag().setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) { // Pour afficher que les Keeps qui ont le même tag
//			}
//		});
		viewHolder.getbTag().setText(mKeeps.get(position).getTag());
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

	public void setOnItemClickListener(View.OnClickListener itemClickListener) {
		mOnItemClickListener = itemClickListener;
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		private Keep keep;
		private final TextView tvTitre;
		private final TextView tvTexte;
		private final Button bTag;
//		private final ImageView imageView;

		public ViewHolder(View itemView) {
			super(itemView);
			itemView.setTag(this);
			itemView.setOnClickListener(mOnItemClickListener);
			this.tvTitre = (TextView) itemView.findViewById(R.id.Row_Keep_Titre);
			this.tvTexte = (TextView) itemView.findViewById(R.id.Row_Keep_Texte);
			this.bTag = (Button) itemView.findViewById(R.id.Row_Keep_Tag);
			this.bTag.setVisibility(View.GONE); // En attendant que le clic sur le tag fonctionne
//			this.bTag.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//				}
//			});
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
		public TextView getbTag() {
			return this.bTag;
		}
	}
}