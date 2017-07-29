package com.sikefeng.tongxuelu.activity;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sikefeng.tongxuelu.R;
import com.sikefeng.tongxuelu.adapter.ImagesAdapter;
import com.sikefeng.tongxuelu.baseactivity.BaseActivity;
import com.sikefeng.tongxuelu.diray.DataPersonImpl;
import com.sikefeng.tongxuelu.entity.Person;
import com.sikefeng.tongxuelu.widgets.MyScrollView;
import com.sikefeng.tongxuelu.widgets.RoundImageView;
import com.tandong.swichlayout.SwichLayoutInterFace;
import com.tandong.swichlayout.SwitchLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

@SuppressLint({ "ResourceAsColor", "NewApi" })
public class MessageActivity extends BaseActivity implements
		MyScrollView.OnScrollListener, SwichLayoutInterFace {
	private GridView mGridView;
	private ImagesAdapter adapter;

	private TextView touname, name_tv, sex_tv, age_tv, address_tv, phono_tv,
			words_tv, hobby_tv, qq_tv, email_tv, constellation_tv, birthday_tv,
			booldtype_tv, nickname_tv, dream_tv, best_color_tv, bestsport_tv,
			beststar_tv, subject_tv;
	private RelativeLayout title_bar;
	private TextView tv_title, tv_return;
	private MyScrollView mScrollView;
	private Intent intent;
	private Person person;
	private int id;
	private RoundImageView head;
	private LayoutInflater layoutInflater;
	private TextView photograph, albums;
	private LinearLayout cancel;
	ArrayList<String> pathList;
	ArrayList<String> pictures = null;

	private ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.message_layout);

		initView();// 初始化视图
		GetData(); //初始化数据
		layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		File file = new File(Environment.getExternalStorageDirectory(),
				"TongXueLu/HeadImages");
		if (!file.exists()) {
			file.mkdirs();
		}

		

		tv_return.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				setExitSwichLayout();
			}
		});
		Button btn_editButton=(Button)findViewById(R.id.btn_edit);
		btn_editButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent editiIntent = new Intent(MessageActivity.this,EditPersonActivity.class);
				editiIntent.putExtra("id",person.getId());
				startActivity(editiIntent);
			}
		});
		mScrollView = (MyScrollView) findViewById(R.id.mScrollView);
		mScrollView.setOnScrollListener(this);
		
		
		setEnterSwichLayout();
	}// *******************************************************************************

	private   void copy(String content, Context context) {
		// 得到剪贴板管理器 
		ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
		clip.setText(content);
	}

	 //实现粘贴功能
	@SuppressWarnings("deprecation")
	private  String paste(Context context) {
		// 得到剪贴板管理器 
		ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
		return clip.getText().toString().trim();
	}

	

	// 初始化视图
	public void initView() {
		head = (RoundImageView) findViewById(R.id.head);
		subject_tv = (TextView) findViewById(R.id.msg_subject);
		touname = (TextView) findViewById(R.id.touname);
		name_tv = (TextView) findViewById(R.id.msg_name);
		sex_tv = (TextView) findViewById(R.id.msg_sex);
		age_tv = (TextView) findViewById(R.id.msg_age);
		address_tv = (TextView) findViewById(R.id.msg_address);
		phono_tv = (TextView) findViewById(R.id.msg_phonoNumber);
		words_tv = (TextView) findViewById(R.id.msg_motto);
		hobby_tv = (TextView) findViewById(R.id.msg_hobby);
		qq_tv = (TextView) findViewById(R.id.msg_qq);
		email_tv = (TextView) findViewById(R.id.msg_email);
		constellation_tv = (TextView) findViewById(R.id.msg_constallation);
		birthday_tv = (TextView) findViewById(R.id.msg_birthday);
		booldtype_tv = (TextView) findViewById(R.id.msg_bloodtype);
		nickname_tv = (TextView) findViewById(R.id.msg_nickname);
		dream_tv = (TextView) findViewById(R.id.msg_dream);
		best_color_tv = (TextView) findViewById(R.id.msg_bestColor);
		bestsport_tv = (TextView) findViewById(R.id.msg_bestSports);
		beststar_tv = (TextView) findViewById(R.id.msg_beststar);
		title_bar = (RelativeLayout) findViewById(R.id.title_bar);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_return = (TextView) findViewById(R.id.tv_return);
	}
@Override
protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	
	GetData();
	
	
}
	private void GetData() {
		intent = getIntent();
		id=intent.getIntExtra("id", 0);
		person=new DataPersonImpl(MessageActivity.this).findPerson(id);
		pathList = new ArrayList<String>();
		pathList.add(person.getImages1());
		pathList.add(person.getImages2());
		pathList.add(person.getImages3());
		pathList.add(person.getImages4());
		pathList.add(person.getImages5());
		pathList.add(person.getImages6());
		pathList.add(person.getImages7());
		pathList.add(person.getImages8());
		pathList.add(person.getImages9());

		pictures = new ArrayList<String>();
		for (int i = 0; i < pathList.size(); i++) {
			if (pathList.get(i) != null) {
				pictures.add(pathList.get(i));
			}
		}
		String path=person.getTouxiang();
		if (!TextUtils.isEmpty(path)){
			ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(MessageActivity.this));
			ImageLoader.getInstance().displayImage(path,head);
		}else{
			ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(MessageActivity.this));
			ImageLoader.getInstance().displayImage("http://bmob-cdn-5625.b0.upaiyun.com/2016/08/15/f369c47b2162497faa83b7f92784ac17.jpg",head);
		}


		mGridView = (GridView) findViewById(R.id.child_grid);
		adapter = new ImagesAdapter(this, pictures, mGridView);
		mGridView.setAdapter(adapter);
		
		if (person.getSubject() != null) {
			subject_tv.setText("教学科目："+person.getSubject());
		} else {
			subject_tv.setVisibility(View.GONE);
		}
		touname.setText(person.getName());
		name_tv.setText("姓名："+person.getName());
		sex_tv.setText("性别："+person.getSex());
		age_tv.setText("年龄："+person.getAge());
		address_tv.setText("地址："+person.getAddress());
		phono_tv.setText("手机："+person.getPhonoNumber());
		words_tv.setText("个性签名："+person.getWords());
		hobby_tv.setText("爱好："+person.getHoddy());
		qq_tv.setText("QQ："+person.getQQ());
		email_tv.setText("E_mail："+person.getE_mail());
		booldtype_tv.setText("血型："+person.getBooldType());
		birthday_tv.setText("生日："+person.getBirthday());
		constellation_tv.setText("星座："+person.getConstellation());
		nickname_tv.setText("绰号："+person.getNickname());

		dream_tv.setText("梦想："+person.getDream());
		best_color_tv.setText("最喜欢的颜色："+person.getBest_Color());
		bestsport_tv.setText("最喜欢的运动："+person.getBest_sports());
		beststar_tv.setText("最喜欢的明星："+person.getBest_star());
		//长按把手机号码复制到粘贴板上
		phono_tv.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View arg0) {
				// TODO Auto-generated method stub
				copy(person.getPhonoNumber(), MessageActivity.this);
				return true;
			}
		});
		

	}

	
	public static Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void onScroll(int scrollY) {
		if (scrollY < 30) {
			tv_title.setVisibility(View.GONE);
			title_bar.setBackgroundColor(Color.parseColor("#00000000"));
		} else {
			tv_title.setVisibility(View.VISIBLE);
			// title_bar.setBackgroundColor(Color.parseColor("#18B4ED"));
			title_bar.setBackgroundColor(getResources().getColor(R.color.whiteGray));
		}
	}

	
	public boolean onKeyDown(int keyCode, KeyEvent event) {// 按返回键时退出Activity的Activity特效动画

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			setExitSwichLayout();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// 设置Activity进入动画
	@Override
	public void setEnterSwichLayout() {
		// TODO Auto-generated method stub
		SwitchLayout.ScaleToBigVerticalIn(this, false, null);
	}

	// 设置Activity退出动画
	@Override
	public void setExitSwichLayout() {
		// TODO Auto-generated method stub
		SwitchLayout.ScaleToBigVerticalOut(this, true, null);
	}
}
