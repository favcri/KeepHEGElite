package com.devmobile.keephegelite.recyclerview;

import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.devmobile.keephegelite.R;
import com.devmobile.keephegelite.business.Keep;
import com.devmobile.keephegelite.storage.KeepDBHelper;

import java.util.List;

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
		if (mKeeps.get(position).getTag() == null)
			viewHolder.getbTag().setVisibility(View.GONE);
		else
			viewHolder.getbTag().setText(mKeeps.get(position).getTag());
		if (mKeeps.get(position).getTitre() == null)
			viewHolder.getTvTitre().setVisibility(View.GONE);
		else
			viewHolder.getTvTitre().setText(mKeeps.get(position).getTitre());
		if (mKeeps.get(position).getTexte() == null)
			viewHolder.getTvTexte().setVisibility(View.GONE);
		else
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
		private final TextView tvDate;
		private final Button bTag;

		public ViewHolder(View itemView) {
			super(itemView);
			itemView.setTag(this);
			itemView.setOnClickListener(mOnItemClickListener);
			this.tvTitre = (TextView) itemView.findViewById(R.id.Row_Keep_Titre);
			this.tvTexte = (TextView) itemView.findViewById(R.id.Row_Keep_Texte);
			this.tvDate = (TextView) itemView.findViewById(R.id.Row_Keep_Date);
			this.bTag = (Button) itemView.findViewById(R.id.Row_Keep_Tag);
			this.bTag.setTag(this);
		}

		public void setBackgroundColor(String color) { // Pour éviter des bugs de parsing
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

		public TextView getTvDate() {
			return this.tvTexte;
		}

		public TextView getbTag() {
			return this.bTag;
		}
	}
}