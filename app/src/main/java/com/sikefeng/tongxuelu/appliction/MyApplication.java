package com.sikefeng.tongxuelu.appliction;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.IBinder;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.sikefeng.tongxuelu.music.MusicService;

import java.util.List;

import cn.bmob.v3.Bmob;

public class MyApplication extends Application{
	private MusicService mService;
	public static boolean isFirst=true;

	@Override
	public void onCreate() {
		super.onCreate();

		// 初始化BmobSDK
		Bmob.initialize(this, "5bb7a072faff350dc9d8d7647cbf3bc12343gf55kkk");
		initImageLoader();
		Intent intent = new Intent(this, MusicService.class);
		startService(intent);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}

	public MusicService getmService() {
		return mService;
	}

	public void setmService(MusicService mService) {
		this.mService = mService;
	}

	public static boolean isServiceRunning(Context context) {
		boolean isRunning = false;

		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager
				.getRunningServices(Integer.MAX_VALUE);

		if (serviceList == null || serviceList.size() == 0) {
			return false;
		}

		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(
					MusicService.class.getName())) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}
	private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			mService = ((MusicService.LocalBinder) service).getService();// 用绑定方法启动service，就是从这里绑定并得到service，然后就可以操作service了
			System.out.println("1null?" + (null == mService));
			mService.setContext(getApplicationContext());

		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {

		}
	};

	private void initImageLoader(){
		DisplayImageOptions options=new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisk(true)
//      .displayer(new RoundedBitmapDisplayer(20))
				.displayer(new BitmapDisplayer() {

					@Override
					public void display(Bitmap bit, ImageAware arg1, LoadedFrom arg2) {
						// TODO Auto-generated method stub
						Bitmap bitmap=Bitmap.createBitmap(bit.getWidth(),bit.getHeight(),Bitmap.Config.ARGB_8888);
						Canvas canvas=new Canvas(bitmap);
						BitmapShader shader=new BitmapShader(bit, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
						Paint paint=new Paint();
						paint.setAntiAlias(true);
						paint.setShader(shader);
//				canvas.drawCircle(bit.getWidth()/2, bit.getHeight()/2, bit.getWidth()/2, paint);
						float height=bit.getHeight();
						float width=bit.getWidth();
						if (height>width) {
							canvas.drawCircle(bit.getWidth()/2, bit.getHeight()/2,width/2, paint);
						}else {
							canvas.drawCircle(bit.getWidth()/2, bit.getHeight()/2,height/2, paint);
						}
						arg1.setImageBitmap(bitmap);
					}
				})
				.build();


		ImageLoaderConfiguration configuration=new ImageLoaderConfiguration.Builder(this)
				.memoryCacheSizePercentage(20) //设置占用总内存的百分比
				.diskCacheFileCount(100)
				.discCacheSize(5*1024*1024)
//      .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.defaultDisplayImageOptions(options)
				.build();

//      ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
		ImageLoader.getInstance().init(configuration);
	}

}
