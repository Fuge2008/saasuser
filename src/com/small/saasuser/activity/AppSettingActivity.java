package com.small.saasuser.activity;

import com.small.saasuser.utils.ImmersionUtils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


 

/**
 * Created by Administrator on 2016/9/28.
 */
public class AppSettingActivity extends BaseActivity implements View.OnClickListener{
    private TextView software_Upgrade;
    private TextView password_Modify;
   
    private ImageButton btn_left;
    
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_setting);
        new ImmersionUtils(this).setTranslucentStatus(R.color.title_color);
       
        software_Upgrade= (TextView) findViewById(R.id.software_upgrade);
        password_Modify= (TextView) findViewById(R.id.password_modify);
     
        btn_left= (ImageButton) findViewById(R.id.btn_left);
      
       
        software_Upgrade.setOnClickListener(this);
        password_Modify.setOnClickListener(this);
     
        btn_left.setOnClickListener(this);
    



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.software_upgrade:
            	Toast.makeText(this, "已是最新版", Toast.LENGTH_SHORT).show();
               
                break;
            case R.id.password_modify:
            	 Intent intent=new Intent(AppSettingActivity.this,ModifyPasswardActivity.class);
                 startActivity(intent);


                break;
               
             
            case R.id.btn_left:
               finish();            	
            	
            	break;
           

        }
    }
 
    

}
