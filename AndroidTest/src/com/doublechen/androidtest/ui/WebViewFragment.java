package com.doublechen.androidtest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.doublechen.androidtest.R;

public class WebViewFragment extends Fragment implements OnClickListener {
	Button mButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_webview, container, false);

		mButton = (Button) rootView.findViewById(R.id.button);
		mButton.setOnClickListener(this);

		// Bundle args = getArguments();
		return rootView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button:
			startActivity(new Intent(getActivity(), WebViewActivity.class));
			break;

		default:
			break;
		}
	}
}
