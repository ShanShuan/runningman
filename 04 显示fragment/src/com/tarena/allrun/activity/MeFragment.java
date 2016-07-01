package com.tarena.allrun.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.tarena.allrun.R;
import com.tarena.allrun.TAppliction;

public class MeFragment extends Fragment {
	View view;
	Button button;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_me, null);
		
		setViews();
		addListeners();
		return view;
	}
	private void addListeners() {
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TAppliction app=(TAppliction) getActivity().getApplication();
				app.finishActivity();
				
			}
		});
		
	}
	private void setViews() {
		button=(Button) view.findViewById(R.id.btn_me_exit);
		
	}

}
