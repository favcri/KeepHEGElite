package com.devmobile.keephegelite.recyclerview;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.devmobile.keephegelite.R;
import com.devmobile.keephegelite.business.Keep;
import com.devmobile.keephegelite.views.KeepAffichage;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class KeepsAdapter extends RecyclerView.Adapter<KeepsAdapter.ViewHolder> {
	private static final String TAG = "KeepsAdapter";
	private List<Keep> mKeeps;

	public static class ViewHolder extends RecyclerView.ViewHolder {
		private Keep keep;
		private final TextView tvTitre;
		private final TextView tvTexte;

		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(itemView.getContext() , KeepAffichage.class);
//					intent.putExtra("KeepTitre", tvTitre.getText().toString());
//					intent.putExtra("Keep", numKeep.getText().toString());
					intent.putExtra("Keep", keep.getNumKeep());
					itemView.getContext().startActivity(intent);
				}
			});
//			this.numKeep = (TextView) itemView.findViewById(R.id.Row_Keep_numKeep);
			this.tvTitre = (TextView) itemView.findViewById(R.id.Row_Keep_Titre);
			this.tvTexte = (TextView) itemView.findViewById(R.id.Row_Keep_Texte);
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
	}

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