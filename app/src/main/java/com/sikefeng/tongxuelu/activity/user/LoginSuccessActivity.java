package com.sikefeng.tongxuelu.activity.user;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sikefeng.tongxuelu.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class LoginSuccessActivity extends Activity {

	private User loginUser = null;
	private SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_success_layout);

		loginUser = BmobUser.getCurrentUser(this, User.class); // 获取已登录对象和信息
		getLocalUser();// 显示当前已登录用户信息
		// 必须先创建数据才可以还原数据库***************************************

	}// ***********************************************************************************

	private void getLocalUser() {
		// BmobUser bmobUser=BmobUser.getCurrentUser(this); //获取已登录对象和信息
		TextView tv_username, tv_email;
		ImageView imageView;
		tv_username = (TextView) findViewById(R.id.tv_username);
		tv_email = (TextView) findViewById(R.id.tv_email);
		imageView = (ImageView) findViewById(R.id.imageView);
		tv_username.append(loginUser.getUsername());
		tv_email.append(loginUser.getEmail());
		BmobFile bmobFile = loginUser.getIcon();// 获取用户头像图标

	}

//	Handler handler = new Handler() {
//		public void handleMessage(Message msg) {
//			if (msg.what == 1) {
//
//				Toast.makeText(getApplicationContext(), "数据还原", Toast.LENGTH_SHORT).show();
//				if (Boolean.valueOf(isLocked) && psswd!=null) {
//					Intent intent = new Intent(LoginSuccessActivity.this,Welcome.class);
//				    startActivity(intent);
//				    LoginSuccessActivity.this.finish();
//				}else {
//					startActivity(new Intent(LoginSuccessActivity.this,MainActivity.class));
//					LoginSuccessActivity.this.finish();
//				}
//			}
//		};
//	};

	public void updateClick(View view) {
		startActivity(new Intent(LoginSuccessActivity.this,UpdateUserActivity.class));
	}

}
