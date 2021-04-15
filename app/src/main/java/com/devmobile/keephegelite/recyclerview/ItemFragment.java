package com.devmobile.keephegelite.recyclerview;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devmobile.keephegelite.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemFragment extends Fragment {

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String KEEP_TITRE = "KeepTitre";
//	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String titre;
	private TextView tvTitre;
	private String mParam2;

	public ItemFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment ItemFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static ItemFragment newInstance(String titre) {
		ItemFragment fragment = new ItemFragment();
		Bundle args = new Bundle();
		args.putString(KEEP_TITRE, titre);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			titre = getArguments().getString(KEEP_TITRE);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_item, container, false);
		tvTitre = (TextView) view.findViewById(R.id.Keep_Titre);
		return view;
	}
}