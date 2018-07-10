package com.small.saasuser.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

 

/**
 * Created by Administrator on 2016/9/28.
 */
public class UserGuideActivity extends  BaseActivity {
	private ImageButton btn_left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_guide);
        btn_left= (ImageButton) findViewById(R.id.btn_left_user_guide);
        btn_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
				
			}
		});
      
    }

    
}
