package com.sikefeng.tongxuelu.activity.welcomeactivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.sikefeng.tongxuelu.R;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends Activity {
	private ViewPager viewpager;
	private View view1, view2, view3;
	private List<View> view_list;
	private Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 隐藏状态栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.guide_layout);
		viewpager = (ViewPager) findViewById(R.id.viewpager);

		LayoutInflater inflater = getLayoutInflater();
		view1 = inflater.inflate(R.layout.layout1, null);
		view2 = inflater.inflate(R.layout.layout2, null);
		view3 = inflater.inflate(R.layout.layout3, null);
		button= (Button) view3.findViewById(R.id.button1);
		view_list = new ArrayList<View>();
		view_list.add(view1);
		view_list.add(view2);
		view_list.add(view3);

		viewpager.setAdapter(pagerAdapter);
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int i, float v, int i1) {

			}

			@Override
			public void onPageSelected(int i) {
				    if (i==view_list.size()-1){
						setAnimation(button);
						handler.sendEmptyMessageDelayed(1,1500);
					}
			}

			@Override
			public void onPageScrollStateChanged(int i) {

			}
		});

		button.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//						LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
//				LinearLayout linearLayout = new LinearLayout(GuideActivity.this);
//				linearLayout.setLayoutParams(params);
//				linearLayout.setOrientation(LinearLayout.VERTICAL);
//				linearLayout.setBackgroundResource(R.drawable.guide1);
//
//				setContentView(linearLayout);
//
//				new Handler().postDelayed(new Runnable() {
//					@Override
//					public void run() {
//						Intent intent = new Intent(GuideActivity.this,LoginActivity.class);
//						startActivity(intent);
//						GuideActivity.this.finish();
//					}
//				}, 3000);
				Intent intent = new Intent(GuideActivity.this,WelcomeActivity.class);
				startActivity(intent);
				GuideActivity.this.finish();
			}
		});
		//初始化应用锁
		SharedPreferences sp=GuideActivity.this.getSharedPreferences("lock",MODE_PRIVATE);
		SharedPreferences.Editor editor=sp.edit();
		editor.putBoolean("isLock",false);
		editor.putString("password","");
		editor.commit();
	} // ******************************
	Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				setAnimation(button);
				handler.sendEmptyMessageDelayed(1,1500);
			}
		}
	};
	PagerAdapter pagerAdapter = new PagerAdapter() {
		@Override
		public CharSequence getPageTitle(int position) {
			return null;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {

			return view == view_list.get((int) Integer.parseInt(object
					.toString()));
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// super.destroyItem(container, position, object);
			container.removeView(view_list.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			container.addView(view_list.get(position));
			return position;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return view_list.size();
		}
	};

	private void setAnimation(View view) {
		float[] vaules = new float[]{0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f, 0.9f, 0.8f, 0.7f, 0.6f, 0.5f};
		final AnimatorSet set = new AnimatorSet();
		set.playTogether(ObjectAnimator.ofFloat(view, "scaleX", vaules), ObjectAnimator.ofFloat(view, "scaleY", vaules));
		set.setDuration(1500);
		set.start();
	}

}
