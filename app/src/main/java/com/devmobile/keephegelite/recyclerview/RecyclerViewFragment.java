package com.devmobile.keephegelite.recyclerview;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.DividerItemDecoration;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devmobile.keephegelite.R;
import com.devmobile.keephegelite.business.Keep;
import com.devmobile.keephegelite.storage.KeepDBHelper;
import com.devmobile.keephegelite.views.AffichageKeep;
import com.devmobile.keephegelite.views.NewKeep;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class RecyclerViewFragment extends Fragment {
	private static final String TAG = "RecyclerViewFragment";
	private static final String KEY_LAYOUT_MANAGER = "layoutManager";
	private static final int SPAN_COUNT = 2;
	public static final String NOTIFICATION_CHANNEL_ID = "10001";
	private final static String default_notification_channel_id = "default";

	private enum LayoutManagerType {GRID_LAYOUT_MANAGER, LINEAR_LAYOUT_MANAGER}

	protected LayoutManagerType mCurrentLayoutManagerType;

	protected RecyclerView mRecyclerView;
	protected KeepsAdapter mAdapter;
	protected RecyclerView.LayoutManager mLayoutManager;
	protected List<Keep> mKeeps;
	private KeepDBHelper db;

	private View.OnClickListener onItemClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
			Keep keep = mKeeps.get(viewHolder.getAdapterPosition());
			Intent intent = new Intent(view.getContext(), AffichageKeep.class);
			intent.putExtra("Keep", keep.getNumKeep());
			view.getContext().startActivity(intent);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext())
				.setSmallIcon(R.drawable.ic_google_keep_icon) // notification icon
				.setContentTitle("Simple notification") // title
				.setContentText("Hello word") // body message
				.setAutoCancel(true); // clear notification when clicked
		Intent intent = new Intent(getContext(), RecyclerViewFragment.class);
		PendingIntent pi = PendingIntent.getActivity(getContext(), 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
		mBuilder.setContentIntent(pi);
		NotificationManager mNotificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(0, mBuilder.build());
		this.db = new KeepDBHelper(getContext());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.recycler_view_frag, container, false);
		rootView.findViewById(R.id.Bouton_New_Keep).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(rootView.getContext(), NewKeep.class);
				rootView.getContext().startActivity(intent);
			}
		});
		rootView.setTag(TAG);
		mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

		mLayoutManager = new LinearLayoutManager(getActivity());
		mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
		if (savedInstanceState != null)
			mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState.getSerializable(KEY_LAYOUT_MANAGER); // Restore saved layout manager type.
		setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
		mRecyclerView.addItemDecoration(dividerItemDecoration);

		mAdapter = new KeepsAdapter(mKeeps);
		mRecyclerView.setAdapter(mAdapter);
		mAdapter.setOnItemClickListener(onItemClickListener);

		rootView.findViewById(R.id.linear_layout_rb).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setRecyclerViewLayoutManager(LayoutManagerType.LINEAR_LAYOUT_MANAGER);
			}
		});

		rootView.findViewById(R.id.grid_layout_rb).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setRecyclerViewLayoutManager(LayoutManagerType.GRID_LAYOUT_MANAGER);
			}
		});
		return rootView;
	}

	@Override
	public void onResume() { // Pour actualiser le RecyclerView quand on revient sur la page d'accueil
		super.onResume();
		mKeeps.clear();
		mKeeps = db.getAllKeeps();
		mAdapter = new KeepsAdapter(mKeeps);
		mRecyclerView.setAdapter(mAdapter);
	}

	public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
		int scrollPosition = 0;
		if (mRecyclerView.getLayoutManager() != null)
			scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();

		switch (layoutManagerType) {
			case GRID_LAYOUT_MANAGER:
				mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
				mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
				break;
			case LINEAR_LAYOUT_MANAGER:
				mLayoutManager = new LinearLayoutManager(getActivity());
				mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
				break;
			default:
				mLayoutManager = new LinearLayoutManager(getActivity());
				mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
		}
		mRecyclerView.setLayoutManager(mLayoutManager);
		mRecyclerView.scrollToPosition(scrollPosition);
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
		super.onSaveInstanceState(savedInstanceState);
	}
}