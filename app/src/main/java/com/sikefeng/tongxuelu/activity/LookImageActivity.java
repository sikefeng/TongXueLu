package com.sikefeng.tongxuelu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.sikefeng.tongxuelu.R;
import com.sikefeng.tongxuelu.multipleimages.NativeImageLoader;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;


public class LookImageActivity extends Activity {
	private PhotoView photoView;
	private ViewPager viewPager;
	private List<String> list;
	private int index = 0;
	private MyPagerAdapter myPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.lookimage);
		viewPager=(ViewPager)findViewById(R.id.viewpager);
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		index = bundle.getInt("position");
		list = bundle.getStringArrayList("path");
		myPagerAdapter=new MyPagerAdapter(LookImageActivity.this,list,viewPager);
		viewPager.setAdapter(myPagerAdapter);
		viewPager.setCurrentItem(index);
//		viewPager.setAdapter(pagerAdapter);
//		viewPager.setCurrentItem(index);
//		viewPager.setCurrentItem(index,false);

	}// ********************** *********************************


	class MyPagerAdapter extends PagerAdapter {
		private Context mcontext;
		private List<String> imagesPath;
		private ViewPager viewPager;
		private List<View> imageViews;
		private Point mPoint = new Point(0, 0);//用来封装ImageView的宽和高的对象
		public MyPagerAdapter(final Context mcontext, List<String> imagesPath, final ViewPager viewPager) {
			this.mcontext = mcontext;
			this.imagesPath = imagesPath;
			this.viewPager=viewPager;
			imageViews=new ArrayList<View>();
			for (String path:imagesPath){
				PhotoView photoView=new PhotoView(mcontext);
				photoView.setTag(path);
				Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(path, mPoint, new NativeImageLoader.NativeImageCallBack() {
					@Override
					public void onImageLoader(Bitmap bitmap, String path) {
						PhotoView photoView2=(PhotoView) viewPager.findViewWithTag(path);
						if(bitmap != null && photoView2 != null){
							photoView2.setImageBitmap(bitmap);
						}
					}
				});
				if(bitmap != null){
					photoView.setImageBitmap(bitmap);
				}else{
					photoView.setImageResource(R.drawable.friends_sends_pictures_no);
				}
				imageViews.add(photoView);
			}
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return null;
			// return title_list.get(position);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {

			return view == imageViews.get((int) Integer.parseInt(object
					.toString()));
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// super.destroyItem(container, position, object);
			container.removeView(imageViews.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			container.addView(imageViews.get(position));
			return position;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return imageViews.size();
		}
	};

}












