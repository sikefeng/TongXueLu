package com.sikefeng.tongxuelu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.sikefeng.tongxuelu.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ShareAppListActivity extends Activity {

	GridView mGridView = null;
	List<ResolveInfo> mAllApps = new ArrayList<ResolveInfo>();
	List<PackageInfo>mAllPackages=new ArrayList<PackageInfo>();
	PackageManager packageManager = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("选择应用分享");
		setContentView(R.layout.application_layout);
		showView();
		TextView tv_back=(TextView)findViewById(R.id.tv_back);
		tv_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	public void showView() {
		packageManager = getPackageManager();
		mGridView = (GridView) this.findViewById(R.id.gridView1);
		setMyAllApps();

		mGridView.setAdapter(new MyAdapter(mAllApps, this));
		

		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				ResolveInfo resolveInfo=mAllApps.get(position);
				String packageName=resolveInfo.activityInfo.packageName;
				for(Iterator<PackageInfo>iterator=mAllPackages.iterator();iterator.hasNext();){
					PackageInfo packageInfo=iterator.next();
					if(packageInfo.applicationInfo.packageName.equals(packageName)){
						Log.i("six grade", "source dir:"+packageInfo.applicationInfo.sourceDir);
						File sourceFile=new File(packageInfo.applicationInfo.sourceDir);
						//调用android系统的分享窗口
						Intent intent=new Intent();
						intent.setAction(Intent.ACTION_SEND);
						intent.setType("*/*");
						intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(sourceFile));
						startActivity(intent);
					}
				}
				
			}
		});
	}

	public void setMyAllApps() {
		// 查找所有首先显示的activity
		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		mAllApps = packageManager.queryIntentActivities(intent, 0);
		mAllPackages=packageManager.getInstalledPackages(0);
		// 按照名字排序
		Collections.sort(mAllApps, new ResolveInfo.DisplayNameComparator(
				packageManager));
	}

	

	class MyAdapter extends BaseAdapter {

		List<ResolveInfo> appList;
		Context mContext;

		public MyAdapter(List<ResolveInfo> appList, Context context) {
			this.appList = appList;
			mContext = context;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return appList.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.application_item, null);
			ImageView imageView = (ImageView) convertView
					.findViewById(R.id.imageView1);
			TextView textView = (TextView) convertView
					.findViewById(R.id.textView1);
			ResolveInfo resolveInfo = appList.get(position);
			textView.setText(resolveInfo.loadLabel(packageManager));
			imageView.setBackgroundColor(Color.TRANSPARENT);
			
			Drawable drawable=resolveInfo.loadIcon(packageManager);
			imageView.setImageDrawable(drawable);
			
//			Bitmap iconBitmap = ImageUtils.drawableToBitmap(resolveInfo
//					.loadIcon(packageManager));
//			imageView.setImageBitmap(ImageUtils.getRoundedCornerBitmap(
//					ImageUtils.zoomBitmap(iconBitmap, 60, 60), 10));
			
			return convertView;
		}

	}
}
