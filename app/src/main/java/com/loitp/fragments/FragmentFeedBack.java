/*
 * This is the source code of DMPLayer for Android v. 1.0.0.
 * You should have received a copy of the license in this archive (see LICENSE).
 * Copyright @Dibakar_Mistry, 2015.
 */
package com.loitp.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loitp.R;

public class FragmentFeedBack extends Fragment {

	public FragmentFeedBack() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootview = inflater.inflate(R.layout.fragment_feedback, null);
		setupInitialViews(rootview);
		return rootview;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void setupInitialViews(View inflatreView) {

	}
}
