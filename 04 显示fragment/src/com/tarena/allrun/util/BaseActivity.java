package com.tarena.allrun.util;

import com.tarena.allrun.TAppliction;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class BaseActivity extends FragmentActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			TAppliction app=(TAppliction) getApplication();
			app.activityList.add(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void onDestroy() {
		try {
			TAppliction app=(TAppliction) getApplication();
			app.activityList.remove(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}
}
