//package com.sikefeng.tongxuelu.activity.activitygroup;
//
//import android.annotation.SuppressLint;
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.SharedPreferences;
//import android.media.MediaPlayer;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentStatePagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.view.animation.Animation;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.RelativeLayout;
//import android.widget.TabHost;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.mytongxuelu.R;
//import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
//import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnCloseListener;
//import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener;
//import com.lidroid.xutils.HttpUtils;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.HttpHandler;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.sikefeng.tongxuelu.activity.BaiduActivity;
//import com.sikefeng.tongxuelu.activity.CloseActivityClass;
//import com.sikefeng.tongxuelu.activity.ShareAppListActivity;
//import com.tongxuelu.activity.fragment.ContactsFragment;
//import com.tongxuelu.activity.fragment.HomeFragment;
//import com.tongxuelu.activity.fragment.MineFragment;
//import com.tongxuelu.activity.user.User;
//import com.tongxuelu.activity.user.UserMsgActivity;
//import com.tongxuelu.bmobutils.BmobUtils;
//import com.tongxuelu.dialog.CommonDialogView;
//import com.tongxuelu.diray.BookActivity;
//import com.tongxuelu.utils.AppUtils;
//import com.tongxuelu.utils.NetworkUtils;
//import com.tongxuelu.utils.ScreenUtils;
//
//import java.io.File;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.List;
//
//import cn.bmob.v3.BmobUser;
//import cn.bmob.v3.datatype.BmobFile;
//
//@SuppressWarnings("deprecation")
//@SuppressLint({ "NewApi", "ResourceAsColor" })
//public class MainActivity extends FragmentActivity implements OnClickListener,RadioGroup.OnCheckedChangeListener {
//	private TabHost tabHost;
//	private SlidingMenu menu;
//	public static int indexTab = 0;
//	public static boolean isOpenedmenu = false;
//	private MediaPlayer player;
//	private RelativeLayout relativeLayout = null;
//	private float startX;
//	private int current = 0;
//	private User loginUser=null;
//	private Animation left_in, left_out;
//	private Animation right_in, right_out;
//	private RadioGroup radioGroup;
//	private RadioButton radioButton = null;
//	private List<Fragment> fragmentList;
//	private ViewPager mViewPager;
//	private int currenttab = -1;
//	private ProgressDialog progressDialog;
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.activity_main_layout);
//		CloseActivityClass.activities.add(this);
//		radioGroup = (RadioGroup) findViewById(R.id.main_tab_group);
//		radioGroup.setOnCheckedChangeListener(this);
//		mViewPager = (ViewPager) findViewById(R.id.viewpager);
//		initFragment();
//		relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
//		current = AppUtils.getCurrentVersion(MainActivity.this);
//		MySlidingMenu(); // 左边侧滑菜单
//		IntentFilter intentFilter = new IntentFilter("actionkkk");
//		registerReceiver(broadcastReceiver, intentFilter);
//		getLocalUser();
///* * 下载进度条的设置 */
//		progressDialog = new ProgressDialog(this);
//		progressDialog.setMax(100);
//		progressDialog.setTitle("提示");
//		progressDialog.setCancelable(true);
//		progressDialog.setCanceledOnTouchOutside(false);
//		progressDialog.setMessage("正在下载，请耐心等候！");
//		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//	}// *********************************************************************
//
//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		getLocalUser();
//	}
//
//	private void getLocalUser() {
//
//		loginUser= BmobUser.getCurrentUser(getApplicationContext(),User.class);
//		TextView tv_username = (TextView) findViewById(R.id.tv_username);
//		ImageView imageView = (ImageView) findViewById(R.id.imageView);
//		tv_username.setText(loginUser.getUsername());
//
//		BmobFile bmobFile=loginUser.getIcon();
//		if (bmobFile!=null) {
//			String url=bmobFile.getFileUrl(getApplicationContext());
//			if (url!=null) {
//				ImageLoader.getInstance().displayImage(url, imageView);
//				imageView.setBackgroundResource(R.drawable.bg_img);
//			}
//		}else {
//
//		}
//		imageView.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				startActivity(new Intent(MainActivity.this,UserMsgActivity.class));
//			}
//		});
//	}
//
//	private void initFragment() {
//		fragmentList = new ArrayList<Fragment>();
//		HomeFragment homeFragment = new HomeFragment();
//		ContactsFragment contactsFragment = new ContactsFragment();
//		MineFragment mineFragment = new MineFragment();
//		fragmentList.add(homeFragment);
//		fragmentList.add(contactsFragment);
//		fragmentList.add(mineFragment);
//		mViewPager.setAdapter(new MyFrageStatePagerAdapter(getSupportFragmentManager()));
//	}
//
//	@Override
//	public void onCheckedChanged(RadioGroup group, int checkedId) {
//		switch (checkedId) {
//			case R.id.main_tab_addExam:
//				changeView(0);
//				break;
//			case R.id.main_tab_myExam:
//				changeView(1);
//				break;
//			case R.id.main_tab_settings:
//				changeView(2);
//				break;
//
//		}
//	}
//
//	/**
//	 * 定义自己的ViewPager适配器。
//	 * 也可以使用FragmentPagerAdapter。关于这两者之间的区别，可以自己去搜一下。
//	 */
//	class MyFrageStatePagerAdapter extends FragmentStatePagerAdapter {
//
//		public MyFrageStatePagerAdapter(FragmentManager fm) {
//			super(fm);
//		}
//
//		@Override
//		public Fragment getItem(int position) {
//			return fragmentList.get(position);
//		}
//
//		@Override
//		public int getCount() {
//			return fragmentList.size();
//		}
//
//		/**
//		 * 每次更新完成ViewPager的内容后，调用该接口，此处复写主要是为了让导航按钮上层的覆盖层能够动态的移动
//		 */
//		@Override
//		public void finishUpdate(ViewGroup container) {
//			super.finishUpdate(container);//这句话要放在最前面，否则会报错
//			//获取当前的视图是位于ViewGroup的第几个位置，用来更新对应的覆盖层所在的位置
//			int currentItem = mViewPager.getCurrentItem();
//			if (currentItem == currenttab) {
//				return;
//			}
//			currenttab = mViewPager.getCurrentItem();
//			radioButton = (RadioButton) radioGroup.getChildAt(currenttab);
//			radioButton.setChecked(true);
//		}
//
//	}
//
//	//手动设置ViewPager要显示的视图
//	private void changeView(int desTab) {
//		mViewPager.setCurrentItem(desTab, true);
//	}
//
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		switch (v.getId()) {
//			case R.id.bt_diray:
//				startActivity(new Intent(this, BookActivity.class));
//				break;
//			case R.id.bt_baidu:
//				startActivity(new Intent(this, BaiduActivity.class));
//				break;
//			case R.id.bt_share:
//				startActivity(new Intent(this, ShareAppListActivity.class));
//				break;
//			case R.id.bt_refresh:
//				if (NetworkUtils.isNetworkConnected(MainActivity.this)){
//					SharedPreferences sp=MainActivity.this.getSharedPreferences("appmsg",MODE_PRIVATE);
//					int version=sp.getInt("appVersion",1);
//					String url=sp.getString("appUrl","");
//					if (version> AppUtils.getCurrentVersion(MainActivity.this)){
//						showNoticeDialog(url);
//					}else {
//						Toast.makeText(MainActivity.this, "当前已是最新版本！", Toast.LENGTH_SHORT).show();
//					}
//				}else {
//					Toast.makeText(MainActivity.this, "网络未连接", Toast.LENGTH_SHORT).show();
//				}
//				break;
//
//			case R.id.bt_exit:
//				ExitDialog();
//				break;
//
//			default:
//				break;
//		}
//	}
//
//
//
//	private void MySlidingMenu() {
//
//		View view = this.getLayoutInflater().inflate(R.layout.left_menu_layout,
//				null);
//
//		menu = new SlidingMenu(this);
//		menu.setMode(SlidingMenu.LEFT);// 设置左滑菜单 
//		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
//		menu.setShadowDrawable(R.drawable.shadow);// 设置阴影图片 
//		menu.setShadowWidthRes(R.dimen.shadow_width);// 设置阴影图片的宽度 
//		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);// SlidingMenu划出时主页面显示的剩余宽度
//		int screenwidth=ScreenUtils.getScreenWidth(MainActivity.this);
//		menu.setBehindWidth(screenwidth - screenwidth / 4);// 设置SlidingMenu菜单的宽度 
//		menu.setFadeDegree(0.35f);// SlidingMenu滑动时的渐变程度 
//		menu.attachToActivity(MainActivity.this, SlidingMenu.SLIDING_CONTENT);// 使SlidingMenu附加在Activity上 
//		menu.setMenu(view);// 设置menu的布局文件 
//		// menu.toggle();//动态判断自动关闭或开启SlidingMenu 
//		menu.setOnOpenedListener(new OnOpenedListener() {
//			@Override
//			public void onOpened() {
//				// TODO Auto-generated method stub
//				isOpenedmenu = true;
//				relativeLayout.setAlpha(0.5f);
//			}
//		});
//		// menu.setOnClosedListener(new OnClosedListener() {
//		// @Override
//		// public void onClosed() {
//		// // TODO Auto-generated method stub
//		// linearLayout.setAlpha(1f);
//		// }
//		// });
//		menu.setOnCloseListener(new OnCloseListener() {
//
//			@Override
//			public void onClose() {
//				// TODO Auto-generated method stub
//				relativeLayout.setAlpha(1f);
//			}
//		});
//		Button bt_diray = (Button) view.findViewById(R.id.bt_diray);
//		Button bt_refresh = (Button) view.findViewById(R.id.bt_refresh);
//		Button bt_share = (Button) view.findViewById(R.id.bt_share);
//		Button bt_exit = (Button) view.findViewById(R.id.bt_exit);
//		Button bt_baidu = (Button) view.findViewById(R.id.bt_baidu);
//
//		bt_share.setOnClickListener(this);
//		bt_exit.setOnClickListener(this);
//		bt_refresh.setOnClickListener(this);
//		bt_baidu.setOnClickListener(this);
//		bt_diray.setOnClickListener(this);
//
//	}
//
//
//
//	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			// TODO Auto-generated method stub
//			String action = intent.getAction();
//			if ("actionkkk".equals(action)) {
//				if (isOpenedmenu) {
//					menu.showMenu();
//				} else {
//					menu.showContent();
//				}
//			}
//		}
//	};
//
//	@Override
//	protected void onDestroy() {
//		// TODO Auto-generated method stub
//		super.onDestroy();
//		unregisterReceiver(broadcastReceiver);
//	}
//
//	private void showNoticeDialog(final String url){
//		AlertDialog alertDialog=new AlertDialog.Builder(MainActivity.this).setTitle("软件更新提示：")
//				.setMessage("软件已更新至最新版本，请下载更新！").setPositiveButton("下载",new DialogInterface.OnClickListener(){
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//						downloadFile(url);
//					}
//				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//					}
//				}).show();
//	}
//	//退出提示对话框
//	private void ExitDialog(){
//		CommonDialogView commonDialogView=new CommonDialogView(MainActivity.this);
//		String time=new SimpleDateFormat("MM-dd hh:mm").format(System.currentTimeMillis());
//		commonDialogView.showDialog(MainActivity.this,true,null,"提示:",time,"是否退出当前账号?","确定","取消");
//		commonDialogView.setmOnDialogClickLister(new CommonDialogView.OnDialogClickLister() {
//			@Override
//			public void onOk() {
//				BmobUtils.exitLoginUser(MainActivity.this);
//			}
//			@Override
//			public void onCancal() {
//
//			}
//		});
//	}
//	public void downloadFile(String downUrl){
//		final String sdcardDir = Environment.getExternalStorageDirectory() + "/TongXueLu/Download/";
//		File dir = new File(sdcardDir);
//		if (!dir.exists()) {
//			dir.mkdir();
//		}
//		HttpUtils http = new HttpUtils();
//		HttpHandler handler = http.download(downUrl,
//				sdcardDir+"同学录.apk",
//				true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
//				true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
//				new RequestCallBack<File>() {
//					@Override
//					public void onStart() {
//						progressDialog.show();
//					}
//
//					@Override
//					public void onLoading(long total, long current, boolean isUploading) {
//						progressDialog.setProgress((int)(current*100/total));
//						System.out.println("progress="+ current*100/total);
//					}
//
//					@Override
//					public void onSuccess(ResponseInfo<File> responseInfo) {
//						System.out.println("downloaded:" + responseInfo.result.getPath());
//						progressDialog.dismiss();
//						Toast.makeText(MainActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
//						installApk(responseInfo.result.getPath());
//					}
//
//
//					@Override
//					public void onFailure(HttpException error, String msg) {
//						System.out.println("fail msg="+msg);
//						progressDialog.dismiss();
//						if (msg.contains("maybe the file has downloaded completely")){
//							installApk(sdcardDir+"同学录.apk");
//						}
//					}
//				});
//	}
//	//更新完成后安装新版app
//	private void installApk(String path){
//		File apkfile = new File(path);
//		if (!apkfile.exists()) {
//			return;
//		}
//		Intent intent = new Intent(Intent.ACTION_VIEW);
//		intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
//		startActivity(intent);
//	}
//
//	// 退出应用程序代码
//	private long exittime = 0;
//
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		// TODO Auto-generated method stub
//		if (keyCode == KeyEvent.KEYCODE_BACK
//				&& event.getAction() == KeyEvent.ACTION_DOWN) {
//			if (MainActivity.isOpenedmenu) {
//				MainActivity.isOpenedmenu = false;
//				sendBroadcast(new Intent("actionkkk"));
//				return false;
//			}
//			if ((System.currentTimeMillis() - exittime) > 2000) {
//				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
//				exittime = System.currentTimeMillis();
//			} else {
//				Intent intent = new Intent(Intent.ACTION_MAIN);
//				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				intent.addCategory(Intent.CATEGORY_HOME);
//				startActivity(intent);
//			}
//
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}
//
//
//}
////import android.annotation.SuppressLint;
////import android.app.AlertDialog;
////import android.app.TabActivity;
////import android.content.BroadcastReceiver;
////import android.content.Context;
////import android.content.DialogInterface;
////import android.content.Intent;
////import android.content.IntentFilter;
////import android.content.SharedPreferences;
////import android.media.MediaPlayer;
////import android.os.Bundle;
////import android.os.Environment;
////import android.view.KeyEvent;
////import android.view.View;
////import android.view.View.OnClickListener;
////import android.view.Window;
////import android.view.animation.Animation;
////import android.view.animation.AnimationUtils;
////import android.widget.Button;
////import android.widget.ImageView;
////import android.widget.LinearLayout;
////import android.widget.RadioButton;
////import android.widget.RadioGroup;
////import android.widget.RadioGroup.OnCheckedChangeListener;
////import android.widget.TabHost;
////import android.widget.TextView;
////import android.widget.Toast;
////
////import com.example.mytongxuelu.R;
////import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
////import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnCloseListener;
////import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener;
////import com.lidroid.xutils.exception.DbException;
////import com.lidroid.xutils.util.LogUtils;
////import com.nostra13.universalimageloader.core.ImageLoader;
////import com.tongxuelu.activity.activitygroup.ContactsActivity;
////import com.tongxuelu.activity.activitygroup.HomeActivity;
////import com.tongxuelu.activity.activitygroup.MineActivity;
////import com.tongxuelu.activity.user.User;
////import com.tongxuelu.activity.user.UserMsgActivity;
////import com.tongxuelu.bmobutils.BmobUtils;
////import com.tongxuelu.diray.BookActivity;
////import com.tongxuelu.download.DownloadManager;
////import com.tongxuelu.download.DownloadService;
////import com.tongxuelu.utils.AppUtils;
////import com.tongxuelu.utils.NetworkUtils;
////import com.tongxuelu.utils.ScreenUtils;
////
////import java.io.File;
////
////import cn.bmob.v3.BmobUser;
////import cn.bmob.v3.datatype.BmobFile;
////
////@SuppressWarnings("deprecation")
////@SuppressLint({ "NewApi", "ResourceAsColor" })
////public class MainActivity extends TabActivity implements OnClickListener {
////	private TabHost tabHost;
////	private RadioGroup radioGroup;
////	private SlidingMenu menu;
////	public static int indexTab = 0;
////	public static boolean isOpenedmenu = false;
////	private MediaPlayer player;
////	private RadioButton radioButton = null;
////	private LinearLayout linearLayout = null;
////	private float startX;
////	private int current = 0;
////	private User loginUser=null;
////	private Animation left_in, left_out;
////	private Animation right_in, right_out;
////	private void prepareAnim() {
////		left_in = AnimationUtils.loadAnimation(this, R.anim.left_in);
////		left_out = AnimationUtils.loadAnimation(this, R.anim.left_out);
////		right_in = AnimationUtils.loadAnimation(this, R.anim.right_in);
////		right_out = AnimationUtils.loadAnimation(this, R.anim.right_out);
////	}
////	int mCurTabId = R.id.main_tab_addExam;
////	@Override
////	protected void onCreate(Bundle savedInstanceState) {
////		// TODO Auto-generated method stub
////		super.onCreate(savedInstanceState);
////		requestWindowFeature(Window.FEATURE_NO_TITLE);
////		setContentView(R.layout.activity_main);
////		CloseActivityClass.activities.add(this);
////		linearLayout = (LinearLayout) findViewById(R.id.linearlayout);
////		radioGroup = (RadioGroup) findViewById(R.id.main_tab_group);
////		prepareAnim();
////		current = AppUtils.getCurrentVersion(MainActivity.this);
////		MySlidingMenu(); // 左边侧滑菜单
////		MyTabHost();
////		IntentFilter intentFilter = new IntentFilter("actionkkk");
////		registerReceiver(broadcastReceiver, intentFilter);
////        getLocalUser();
////
////	}// *********************************************************************
////	@Override
////	protected void onResume() {
////		// TODO Auto-generated method stub
////		super.onResume();
////		getLocalUser();
////	}
////
////	private void getLocalUser() {
////
////		loginUser= BmobUser.getCurrentUser(getApplicationContext(),User.class);
////		TextView tv_username = (TextView) findViewById(R.id.tv_username);
////		ImageView imageView = (ImageView) findViewById(R.id.imageView);
////		tv_username.setText(loginUser.getUsername());
////
////		BmobFile bmobFile=loginUser.getIcon();
////		if (bmobFile!=null) {
////			String url=bmobFile.getFileUrl(getApplicationContext());
////			if (url!=null) {
////				ImageLoader.getInstance().displayImage(url, imageView);
////				imageView.setBackgroundResource(R.drawable.bg_img);
////			}
////		}else {
////
////		}
////		imageView.setOnClickListener(new OnClickListener() {
////			@Override
////			public void onClick(View arg0) {
////				// TODO Auto-generated method stub
////				startActivity(new Intent(MainActivity.this,UserMsgActivity.class));
////			}
////		});
////	}
////
////
////	private void MyTabHost() {
////
////		tabHost = this.getTabHost();
////
////		tabHost.addTab(tabHost.newTabSpec("主页").setIndicator("主页")
////				.setContent(new Intent(this, HomeActivity.class)));
////		tabHost.addTab(tabHost.newTabSpec("联系人").setIndicator("联系人")
////				.setContent(new Intent(this, ContactsActivity.class)));
////		tabHost.addTab(tabHost.newTabSpec("我的").setIndicator("我的")
////				.setContent(new Intent(this,MineActivity.class)));
////		// tabHost.setCurrentTab(int index)
////		// tabHost.setCurrentTabByTag(String tag)
////		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
////
////			@Override
////			public void onCheckedChanged(RadioGroup arg0, int checkedId) {
////				// TODO Auto-generated method stub
////				if (mCurTabId == checkedId) {
////					return;
////				}
////				int checkedId2 = checkedId;
////				final boolean o;
////				if (mCurTabId < checkedId2)
////					o = true;
////				else
////					o = false;
////				if (o)
////					tabHost.getCurrentView().startAnimation(left_out);
////				else
////					tabHost.getCurrentView().startAnimation(right_out);
////				switch (checkedId) {
////				case R.id.main_tab_addExam:
////					tabHost.setCurrentTabByTag("主页");
////					break;
////				case R.id.main_tab_myExam:
////					tabHost.setCurrentTabByTag("联系人");
////					break;
////				case R.id.main_tab_settings:
////					tabHost.setCurrentTabByTag("我的");
////					break;
////
////
////				}
////				if (o)
////					tabHost.getCurrentView().startAnimation(left_in);
////				else
////					tabHost.getCurrentView().startAnimation(right_in);
////				mCurTabId = checkedId2;
////			}
////		});
////
////	}
////
////
////	@Override
////	public void onClick(View v) {
////		// TODO Auto-generated method stub
////		switch (v.getId()) {
////		case R.id.bt_diray:
////			startActivity(new Intent(this, BookActivity.class));
////			break;
////		case R.id.bt_baidu:
////			startActivity(new Intent(this, BaiduActivity.class));
////			break;
////		case R.id.bt_share:
////			startActivity(new Intent(this, ShareAppListActivity.class));
////			break;
////		case R.id.bt_refresh:
////			if (NetworkUtils.isNetworkConnected(MainActivity.this)){
////				SharedPreferences sp=MainActivity.this.getSharedPreferences("appmsg",MODE_PRIVATE);
////				int version=sp.getInt("appVersion",1);
////				String url=sp.getString("appUrl","");
////				if (version> AppUtils.getCurrentVersion(MainActivity.this)){
////					showNoticeDialog(url);
////				}else {
////					Toast.makeText(MainActivity.this, "当前已是最新版本！", Toast.LENGTH_SHORT).show();
////				}
////			}else {
////				Toast.makeText(MainActivity.this, "网络未连接", Toast.LENGTH_SHORT).show();
////			}
////			break;
////
////		case R.id.bt_exit:
////			BmobUtils.exitLoginUser(MainActivity.this);
//////			BmobIM.getInstance().disConnect();
////			break;
////
////		default:
////			break;
////		}
////	}
////
////
////
////	private void MySlidingMenu() {
////
////		View view = this.getLayoutInflater().inflate(R.layout.left_menu_layout,
////				null);
////
////		menu = new SlidingMenu(this);
////		menu.setMode(SlidingMenu.LEFT);// 设置左滑菜单 
////		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
////		menu.setShadowDrawable(R.drawable.shadow);// 设置阴影图片 
////		menu.setShadowWidthRes(R.dimen.shadow_width);// 设置阴影图片的宽度 
////		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);// SlidingMenu划出时主页面显示的剩余宽度
////        int screenwidth=ScreenUtils.getScreenWidth(MainActivity.this);
////		menu.setBehindWidth(screenwidth - screenwidth / 4);// 设置SlidingMenu菜单的宽度 
////		menu.setFadeDegree(0.35f);// SlidingMenu滑动时的渐变程度 
////		menu.attachToActivity(MainActivity.this, SlidingMenu.SLIDING_CONTENT);// 使SlidingMenu附加在Activity上 
////		menu.setMenu(view);// 设置menu的布局文件 
////		// menu.toggle();//动态判断自动关闭或开启SlidingMenu 
////		menu.setOnOpenedListener(new OnOpenedListener() {
////			@Override
////			public void onOpened() {
////				// TODO Auto-generated method stub
////				isOpenedmenu = true;
////				// WindowManager.LayoutParams lp=getWindow().getAttributes();
////				// lp.alpha=0.3f;
////				// getWindow().setAttributes(lp);
////
////				linearLayout.setAlpha(0.5f);
////			}
////		});
////		// menu.setOnClosedListener(new OnClosedListener() {
////		// @Override
////		// public void onClosed() {
////		// // TODO Auto-generated method stub
////		// linearLayout.setAlpha(1f);
////		// }
////		// });
////		menu.setOnCloseListener(new OnCloseListener() {
////
////			@Override
////			public void onClose() {
////				// TODO Auto-generated method stub
////				linearLayout.setAlpha(1f);
////			}
////		});
////		Button bt_diray = (Button) view.findViewById(R.id.bt_diray);
////		Button bt_refresh = (Button) view.findViewById(R.id.bt_refresh);
////		Button bt_share = (Button) view.findViewById(R.id.bt_share);
////		Button bt_exit = (Button) view.findViewById(R.id.bt_exit);
////		Button bt_baidu = (Button) view.findViewById(R.id.bt_baidu);
////
////		bt_share.setOnClickListener(this);
////		bt_exit.setOnClickListener(this);
////		bt_refresh.setOnClickListener(this);
////		bt_baidu.setOnClickListener(this);
////		bt_diray.setOnClickListener(this);
////
////	}
////
////
////
////	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
////
////		@Override
////		public void onReceive(Context context, Intent intent) {
////			// TODO Auto-generated method stub
////			String action = intent.getAction();
////			if ("actionkkk".equals(action)) {
////				if (isOpenedmenu) {
////					menu.showMenu();
////				} else {
////					menu.showContent();
////				}
////			}
////		}
////	};
////
////	@Override
////	protected void onDestroy() {
////		// TODO Auto-generated method stub
////		super.onDestroy();
////		unregisterReceiver(broadcastReceiver);
////	}
////
////	@Override
////	public boolean onKeyDown(int keyCode, KeyEvent event) {
////		// TODO Auto-generated method stub
////		if (keyCode == KeyEvent.KEYCODE_BACK
////				&& event.getAction() == KeyEvent.ACTION_DOWN) {
////			if (isOpenedmenu) {
////			} else {
////				menu.showContent();
////				return false;
////			}
////
////		}
////		return super.onKeyDown(keyCode, event);
////	}
////	private void showNoticeDialog(final String url){
////		AlertDialog alertDialog=new AlertDialog.Builder(MainActivity.this).setTitle("软件更新提示：")
////				.setMessage("软件已更新至最新版本，请下载更新！").setPositiveButton("下载",new DialogInterface.OnClickListener(){
////					@Override
////					public void onClick(DialogInterface dialog, int which) {
////						dialog.dismiss();
////						download(url);
////					}
////				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
////					@Override
////					public void onClick(DialogInterface dialog, int which) {
////						dialog.dismiss();
////					}
////				}).show();
////	}
////	private void download(String url){
////		String sdcardDir = Environment.getExternalStorageDirectory()
////				+ "/TongXueLu/Download/";
////		File dir=new File(sdcardDir);
////		if (!dir.exists()){
////			dir.mkdir();
////		}
////		DownloadManager downloadManager = DownloadService.getDownloadManager(MainActivity.this);
////		try {
////			downloadManager.addNewDownload(url,
////					"同学录.apk",
////					sdcardDir+"同学录.apk",
////					true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
////					false, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
////					null);
////		} catch (DbException e) {
////			LogUtils.e(e.getMessage(), e);
////		}
////	}
////
////
////}
