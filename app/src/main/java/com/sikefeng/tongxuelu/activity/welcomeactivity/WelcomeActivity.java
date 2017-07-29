package com.sikefeng.tongxuelu.activity.welcomeactivity;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;

import com.sikefeng.tongxuelu.R;
import com.sikefeng.tongxuelu.activity.MainActivity;
import com.sikefeng.tongxuelu.activity.user.LoginActivity;
import com.sikefeng.tongxuelu.screenlock.Welcome;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class WelcomeActivity extends Activity implements Animation.AnimationListener {

	private SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		sharedPreferences = getSharedPreferences("count", MODE_WORLD_READABLE);
		int count = sharedPreferences.getInt("start_count", 0);
		// 初始化BmobSDK
		Bmob.initialize(this, "5bb7a072faff350dc9d8d7647cbf3bc2");
		if (count == 0) {
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putInt("start_count", ++count);
			editor.commit();
			Intent intent = new Intent(WelcomeActivity.this,GuideActivity.class);
			startActivity(intent);
			WelcomeActivity.this.finish();

		} else {
			GuidePageView();
		}
	}// **********************

	private void GuidePageView(){
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setLayoutParams(params);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setBackgroundResource(R.drawable.guide1);
		ScaleAnimation animation = new ScaleAnimation(1.0f, 1.05f, 1.0f, 1.05f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		animation.setDuration(3000);
		animation.setFillAfter(true);
		linearLayout.setAnimation(animation);
		animation.setAnimationListener(this);
//			Animation alpha_rotate_scale = AnimationUtils.loadAnimation(this, R.anim.alpha_rotate_scale);
//			linearLayout.setAnimation(alpha_rotate_scale);
		setContentView(linearLayout);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onAnimationStart(Animation animation) {

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		BmobUser bmobUser= BmobUser.getCurrentUser(getApplicationContext());
		if (bmobUser!=null){
			SharedPreferences sp=WelcomeActivity.this.getSharedPreferences("lock",MODE_PRIVATE);
			boolean isLocked=sp.getBoolean("isLock",false);
			String psswd=sp.getString("password","");
			if (isLocked && psswd!=null) {
				Intent intent = new Intent(getApplicationContext(),Welcome.class);
				startActivity(intent);
				WelcomeActivity.this.finish();
			}else {
				startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
				WelcomeActivity.this.finish();
			}
		}else {
			startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
			WelcomeActivity.this.finish();
		}
	}

	@Override
	public void onAnimationRepeat(Animation animation) {

	}
}



