package com.small.saasuser.activity;

import android.os.Handler;
import android.os.Message;

public class yanzhengmathread extends Thread{
	private int time;
	private Handler timehandler;
	private boolean obtain;
	yanzhengmathread(int time,Handler timehandler){
		this.time=time;
		this.timehandler=timehandler;
		obtain=true;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (obtain) {
			for (; time >= 0; time--) {
				Message m = timehandler.obtainMessage();
				m.what=1;
				m.arg1 = time;
				timehandler.sendMessage(m);
				if(time==0){
					obtain=false;
					Message m2 = timehandler.obtainMessage();
					m2.what=2;
					
					timehandler.sendMessage(m2);
				}
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
