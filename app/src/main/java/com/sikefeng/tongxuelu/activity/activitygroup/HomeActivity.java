package com.sikefeng.tongxuelu.activity.activitygroup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.sikefeng.tongxuelu.R;
import com.sikefeng.tongxuelu.activity.MainActivity;


@SuppressLint("NewApi") public class HomeActivity extends Activity {

	
	private TextView tv_title;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home_layout);
        init();

	}// ********************************************************************



	private void init() {

		tv_title = (TextView) findViewById(R.id.tv_title);

		tv_title.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (MainActivity.isOpenedmenu) {
					
					sendBroadcast(new Intent("actionkkk"));
					
				} else {
					
					MainActivity.isOpenedmenu = true;
					sendBroadcast(new Intent("actionkkk"));
					
				}
			}
		});

	}

	// 退出应用程序代码
	private long exittime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (MainActivity.isOpenedmenu) {
				MainActivity.isOpenedmenu = false;
				sendBroadcast(new Intent("actionkkk"));
				return false;
			}
			if ((System.currentTimeMillis() - exittime) > 2000) {
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				exittime = System.currentTimeMillis();
			} else {
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.addCategory(Intent.CATEGORY_HOME);
				startActivity(intent);
			}
			
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
