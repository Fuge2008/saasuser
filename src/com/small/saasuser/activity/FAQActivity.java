package com.small.saasuser.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

 






import java.io.InputStream;

import com.small.saasuser.utils.TxtUtils;

/**
 * Created by Administrator on 2016/9/28.
 */
public class FAQActivity extends BaseActivity {
    private TextView tv_faq;
	private ImageButton btn_left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq);
      
        tv_faq= (TextView) findViewById(R.id.faq_tv);
       
        InputStream inputStream = getResources().openRawResource(R.raw.faq);
        String string = TxtUtils.getString(inputStream);
        tv_faq.setText(string);
        btn_left= (ImageButton) findViewById(R.id.btn_left_app_faq);
        btn_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
				
			}
		});
        
    }

 


}
