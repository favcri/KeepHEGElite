package com.devmobile.keephegelite.recyclerview;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devmobile.keephegelite.R;
import com.devmobile.keephegelite.business.Keep;
import com.devmobile.keephegelite.storage.KeepDBHelper;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class RecyclerViewFragment extends Fragment {

	private static final String TAG = "RecyclerViewFragment";
	private static final String KEY_LAYOUT_MANAGER = "layoutManager";
	private static final int SPAN_COUNT = 2;
	private static final int DATASET_COUNT = 60;

	private enum LayoutManagerType {GRID_LAYOUT_MANAGER, LINEAR_LAYOUT_MANAGER}

	protected LayoutManagerType mCurrentLayoutManagerType;

	protected RadioButton mLinearLayoutRadioButton;
	protected RadioButton mGridLayoutRadioButton;

	protected RecyclerView mRecyclerView;
	protected KeepsAdapter mAdapter;
	protected RecyclerView.LayoutManager mLayoutManager;
	protected List<Keep> mKeeps;
	private KeepDBHelper db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.db = new KeepDBHelper(getContext());
		initDataset();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.recycler_view_frag, container, false);
		rootView.setTag(TAG);

		// BEGIN_INCLUDE(initializeRecyclerView)
		mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

		// LinearLayoutManager is used here, this will layout the elements in a similar fashion
		// to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
		// elements are laid out.
		mLayoutManager = new LinearLayoutManager(getActivity());

		mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

		if (savedInstanceState != null) {
			// Restore saved layout manager type.
			mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
					.getSerializable(KEY_LAYOUT_MANAGER);
		}
		setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

		mAdapter = new KeepsAdapter (mKeeps);
		// Set CustomAdapter as the adapter for RecyclerView.
		mRecyclerView.setAdapter(mAdapter);
		// END_INCLUDE(initializeRecyclerView)

		mLinearLayoutRadioButton = (RadioButton) rootView.findViewById(R.id.linear_layout_rb);
		mLinearLayoutRadioButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setRecyclerViewLayoutManager(LayoutManagerType.LINEAR_LAYOUT_MANAGER);
			}
		});

		mGridLayoutRadioButton = (RadioButton) rootView.findViewById(R.id.grid_layout_rb);
		mGridLayoutRadioButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setRecyclerViewLayoutManager(LayoutManagerType.GRID_LAYOUT_MANAGER);
			}
		});

		return rootView;
	}

	/**
	 * Set RecyclerView's LayoutManager to the one given.
	 *
	 * @param layoutManagerType Type of layout manager to switch to.
	 */
	public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
		int scrollPosition = 0;

		// If a layout manager has already been set, get current scroll position.
		if (mRecyclerView.getLayoutManager() != null) {
			scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
					.findFirstCompletelyVisibleItemPosition();
		}

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
		// Save currently selected layout manager.
		savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
		super.onSaveInstanceState(savedInstanceState);
	}

	@SuppressLint("LongLogTag")
	private void initDataset() {
		Keep keep = new Keep ("Keep 1", "Un texte 1 de la BDD", "FF0000");
		Log.d("L'titre et le texte du keep 1", keep.getTitre() + " :: " + keep.getTexte() + " :: " + keep.getColor());
		long idKeep = db.insertKeep (keep);
		Keep keepDB = db.getKeep(idKeep);
		Log.d("L'titre et le texte du keep 1 de la BDD", keepDB.getTitre() + " :: " + keepDB.getTexte() + " . Couleur: " + keepDB.getColor());
		db.insertKeep (new Keep("Keep 2", "Un textee 2 de la BDD", "FFFF00"));
		db.insertKeep (new Keep("Keep 3", "Un teexte 3 de la BDD", "FF00FF"));
		db.insertKeep (new Keep("Keep 4", "Un texxte 4 de la BDD", "00FF00"));
		mKeeps = db.getAllKeeps();
	}
}