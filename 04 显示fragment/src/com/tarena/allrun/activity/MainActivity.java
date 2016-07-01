package com.tarena.allrun.activity;

import com.tarena.allrun.R;
import com.tarena.allrun.TAppliction;
import com.tarena.allrun.R.layout;
import com.tarena.allrun.R.menu;
import com.tarena.allrun.util.BaseActivity;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MotionEvent;

public class MainActivity extends BaseActivity {

    private TAppliction tAppliction;


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       
        
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				startActivity(new Intent(MainActivity.this,MainFragmentActivity.class));
				tAppliction.activityList.remove(this);
				finish();
			}
		}, 2000);
        
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
String s=null;
s.toCharArray();
    	return super.onTouchEvent(event);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
