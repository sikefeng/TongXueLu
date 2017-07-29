package com.sikefeng.tongxuelu.activity;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sikefeng.tongxuelu.R;
import com.sikefeng.tongxuelu.activity.fragment.ContactsFragment;
import com.sikefeng.tongxuelu.activity.user.User;
import com.sikefeng.tongxuelu.adapter.ImagesAdapter;
import com.sikefeng.tongxuelu.clipimage.ClipActivity;
import com.sikefeng.tongxuelu.dialog.DateDialog;
import com.sikefeng.tongxuelu.dialog.LoadingDialog;
import com.sikefeng.tongxuelu.diray.DataPersonImpl;
import com.sikefeng.tongxuelu.entity.Person;
import com.sikefeng.tongxuelu.multipleimages.ChildAdapter;
import com.sikefeng.tongxuelu.multipleimages.startSelectActivity;
import com.sikefeng.tongxuelu.sortlistview.SortListViewActivity;
import com.sikefeng.tongxuelu.utils.NetworkUtils;
import com.sikefeng.tongxuelu.utils.ToastUtils;
import com.sikefeng.tongxuelu.widgets.MyGridView;
import com.sikefeng.tongxuelu.widgets.RoundImageView;
import com.tandong.swichlayout.SwichLayoutInterFace;
import com.tandong.swichlayout.SwitchLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;


//Bitmap aBitmap;
//		try {
//		aBitmap = BitmapFactory.decodeStream(getAssets().open("tou.png"));
public class RegisterActivity extends Activity implements SwichLayoutInterFace {
	private MyGridView mGridView;
	private ImagesAdapter adapter;
	private ArrayList<String> imageList=new ArrayList<String>();
    private ArrayList<String> pathList=null;
	private LoadingDialog loadingDialog;
	private LinearLayout linearlayout_category;
    private TextView tv_contact;
	private EditText subjectEditText, nameEditText, ageEditText,
			addressEditText, phonoEditText, wordsEditText, hobbyEditText,
			qqEditText, emailEditText, constellationEditText, bloodEditText, nicknameEditText, dreamEditText,
			bestcolorEditText, beststarEditText, bestsportEditText;
	private RelativeLayout subject_layout;
	private String id, subject, name, age, sex, address, phonoNumber, hoddy,
			qq, e_mail, constellation, birthday, booldType, nickname, words,
			dream, best_Color, best_Star, best_Sport, images1, images2,
			images3, images4, images5, images6, images7, images8, images9;
	private String[] imagespath=new String[9];
	private RadioGroup radioGroup;
	private RadioGroup radioGroup_category;
	private Button savebButton;
	private TextView tv_birthday;
	private TextView add_picture;
 	private String flag;

	private RoundImageView head;
	private PopupWindow popWindow;
	private LayoutInflater layoutInflater;
	private TextView photograph, albums;
	private LinearLayout cancel;
	public static final int PHOTOZOOM = 0;
	public static final int PHOTOTAKE = 1;
	public static final int IMAGE_COMPLETE = 2;
	public static final int CROPREQCODE = 3;
	private String photoSavePath;
	private String photoSaveName;
	private String path;
	private String temppath=null;
	private DataPersonImpl dataPersonImpl;
	private User localUser=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register_layout);
		dataPersonImpl=new DataPersonImpl(RegisterActivity.this);
		localUser=  BmobUser.getCurrentUser(RegisterActivity.this, User.class);
		setEnterSwichLayout();
		Intent intent = getIntent();
		flag = intent.getStringExtra("flag");
		initView();
		layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		File file = new File(Environment.getExternalStorageDirectory(),
				"TongXueLu/HeadImages");
		if (!file.exists()) {
			file.mkdirs();
		}

		photoSavePath = Environment.getExternalStorageDirectory()
				+ "/TongXueLu/HeadImages/";
		photoSaveName = "headview" + ".png";
		head = (RoundImageView) findViewById(R.id.headview);
		head.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				showPopupWindow(head);
			}
		});
		 TextView tv_back=(TextView)findViewById(R.id.tv_back);
		 tv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			setExitSwichLayout();	
			}
		});
		loadingDialog=LoadingDialog.getInstance();
	}// **********************************************************************************************
	//初始化二维码数据
	     private void initCodeResult(){
			 Intent intent=this.getIntent();
			 if (intent!=null){
				 String data=intent.getStringExtra("data");
				 if (!NetworkUtils.isNetworkConnected(RegisterActivity.this)){
					 ToastUtils.showShort(RegisterActivity.this,"网络未连接");
					 return;
				 }
				 if (data!=null && !TextUtils.isEmpty(data)){
					 BmobQuery<User> query = new BmobQuery<User>();
					 query.addWhereEqualTo("objectId", data);
					 query.findObjects(RegisterActivity.this, new FindListener<User>() {
						 @Override
						 public void onSuccess(List<User> list) {
							 if (list.size()> 0){
								 User user =list.get(0);
								 linearlayout_category=(LinearLayout)findViewById(R.id.linearlayout_category);
								 linearlayout_category.setVisibility(View.VISIBLE);
								 radioGroup_category=(RadioGroup)findViewById(R.id.radioGroup_category);
								 flag = "classmate";
								 nameEditText.setText(user.getTruename());
								 ageEditText.setText(user.getAge());
								 phonoEditText.setText(user.getPhoneno());
								 qqEditText.setText(user.getQQ());
								 emailEditText.setText(user.getEmail());
								 tv_birthday.setText(user.getBirthday());
								 constellationEditText.setText(user.getXingzuo());
								 radioGroup_category.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
									 @Override
									 public void onCheckedChanged(RadioGroup group, int checkedId) {
										 switch (checkedId){
											 case R.id.radio_teacher:
												 flag = "teacher";
												 if (!"teacher".equals(flag)) {
													 subject_layout.setVisibility(View.GONE);
												 }else{
													 subject_layout.setVisibility(View.VISIBLE);
												 }
												 break;
											 case R.id.radio_classmate:
												 flag = "classmate";
												 if (!"teacher".equals(flag)) {
													 subject_layout.setVisibility(View.GONE);
												 }else{
													 subject_layout.setVisibility(View.VISIBLE);
												 }
												 break;
											 case R.id.radio_friends:
												 flag = "friends";
												 if (!"teacher".equals(flag)) {
													 subject_layout.setVisibility(View.GONE);
												 }else{
													 subject_layout.setVisibility(View.VISIBLE);
												 }
												 break;
										 }

									 }
								 });
							 }

						 }

						 @Override
						 public void onError(int i, String s) {
							 System.out.println(i+s);
						 }
					 });


				 }
			 }

		 }
		 private void SavaDatabase() {
            //显示加载进度对话框
			 loadingDialog.showDialogForLoading(RegisterActivity.this,true,"正在保存数据...");
			  if (TextUtils.isEmpty(nameEditText.getText().toString())) {
					Toast.makeText(getApplicationContext(), "姓名不能为空!", Toast.LENGTH_SHORT).show();
                    savebButton.setEnabled(true);
				    loadingDialog.hideDialogForLoading();
				}else {
					for (int i = 0; i < radioGroup.getChildCount(); i++) {
						RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
						if (radioButton.isChecked()) {
							sex = radioButton.getText().toString();
						}
					}
					subject = subjectEditText.getText().toString().trim();
					name = nameEditText.getText().toString().trim();
					age = ageEditText.getText().toString().trim();
					address = addressEditText.getText().toString().trim();
					phonoNumber = phonoEditText.getText().toString().trim();
					words = wordsEditText.getText().toString().trim();
					hoddy = hobbyEditText.getText().toString().trim();
					qq = qqEditText.getText().toString().trim();
					e_mail = emailEditText.getText().toString().trim();
					constellation = constellationEditText.getText().toString().trim();
					birthday = tv_birthday.getText().toString().trim();
					booldType = bloodEditText.getText().toString().trim();
					nickname = nicknameEditText.getText().toString().trim();
					dream = dreamEditText.getText().toString().trim();
					best_Color = bestcolorEditText.getText().toString().trim();
					best_Sport = bestsportEditText.getText().toString().trim();
					best_Star = beststarEditText.getText().toString().trim();
	                if (imageList!=null&&imageList.size()>0) {

	                	if (imageList.size()>9) {
							for (int i = 0; i < 9; i++) {
								imagespath[i]=imageList.get(i);
							}
						}else {
							for (int i = 0; i < imageList.size(); i++) {
		                    	imagespath[i]=imageList.get(i);
		    				}
						}
					}

				  if (temppath!=null){
					  final BmobFile bmobFile = new BmobFile(new File(temppath));
					  bmobFile.uploadblock(RegisterActivity.this, new UploadFileListener() {
						  @Override
						  public void onSuccess() {
                              setType(bmobFile.getFileUrl(RegisterActivity.this));
							  updateUserMsg();
							  loadingDialog.hideDialogForLoading();
						  }
						  @Override
						  public void onFailure(int i, String s) {
							  savebButton.setEnabled(true);
							  loadingDialog.hideDialogForLoading();
						  }
					  });

				  }else {
					  setType("");
					  updateUserMsg();
				  }


				}
			 }


	          private void setType(String icon_url){
				  if (!"teacher".equals(flag)) {
					  Person person = new Person(flag, name, age, words, sex,
							  phonoNumber, address, hoddy, qq, e_mail,
							  constellation, birthday, booldType, nickname,
							  dream, best_Color, best_Sport, best_Star,icon_url,imagespath[0],
							  imagespath[1], imagespath[2], imagespath[3], imagespath[4], imagespath[5],
							  imagespath[6], imagespath[7], imagespath[8]);
					  dataPersonImpl.savePerson(person);

				  } else {
					  Person person = new Person(flag, subject, name, age, words,
							  sex, phonoNumber, address, hoddy, qq, e_mail,
							  constellation, birthday, booldType, nickname,
							  dream, best_Color, best_Sport, best_Star,icon_url,imagespath[0],
							  imagespath[1], imagespath[2], imagespath[3], imagespath[4], imagespath[5],
							  imagespath[6], imagespath[7], imagespath[8]);
					  dataPersonImpl.savePerson(person);
					  ContactsFragment.personl=person;

				  }
			  }

	private void updateUserMsg(){
		System.out.println("111");
		Gson gson=new Gson();
		List<Person> personList=dataPersonImpl.findAllPersons();
		localUser.setPersonJsonData(gson.toJson(personList));
		localUser.update(RegisterActivity.this, new UpdateListener() {
			@Override
			public void onSuccess() {
				System.out.println("222");
				loadingDialog.hideDialogForLoading();
				ContactsFragment.isReLoad=true;
				Toast.makeText(RegisterActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
				RegisterActivity.this.finish();
			}
			@Override
			public void onFailure(int i, String s) {
				System.out.println("----------"+i+s);
				loadingDialog.hideDialogForLoading();
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode==115) {
			String phone=data.getStringExtra("phone");
			if (!TextUtils.isEmpty(phone)&&phone!=null){
				phonoEditText.setText(phone);
			}
		}
		if (resultCode==111) {
			
					pathList= ChildAdapter.getArrayList();
					for (int i = 0; i < pathList.size(); i++) {
						imageList.add(pathList.get(i));
					}
					ArrayList<String> path=null;
					if (imageList.size()>9) {
						path=new ArrayList<String>();
						for (int i = 0; i < 9; i++) {
							path.add(imageList.get(i));
						}
						adapter = new ImagesAdapter(this, path, mGridView);
						mGridView.setAdapter(adapter);
						Toast.makeText(getApplicationContext(), "最多添加9张图片", Toast.LENGTH_SHORT).show();
					}else {
						adapter = new ImagesAdapter(RegisterActivity.this, imageList, mGridView);
						mGridView.setAdapter(adapter);
					}

		}
		if (resultCode != RESULT_OK) {
			return;
		}
		Uri uri = null;
		switch (requestCode) {
		case PHOTOZOOM:// 锟斤拷锟�
			if (data == null) {
				return;
			}
			uri = data.getData();
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = managedQuery(uri, proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			path = cursor.getString(column_index);// 图片锟节碉拷路锟斤拷
			Intent intent3 = new Intent(this, ClipActivity.class);
			intent3.putExtra("path", path);
			startActivityForResult(intent3, IMAGE_COMPLETE);
			break;
		case PHOTOTAKE:// 锟斤拷锟斤拷
			path = photoSavePath + photoSaveName;
			uri = Uri.fromFile(new File(path));
			Intent intent2 = new Intent(this, ClipActivity.class);
			intent2.putExtra("path", path);
			startActivityForResult(intent2, IMAGE_COMPLETE);
			break;
		case IMAGE_COMPLETE:
			temppath = data.getStringExtra("path");
			try {
				FileInputStream fis=new FileInputStream(temppath);
				head.setImageBitmap(BitmapFactory.decodeStream(fis));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}


	@SuppressWarnings("deprecation")
	private void showPopupWindow(View parent) {
		if (popWindow == null) {
			View view = layoutInflater.inflate(R.layout.pop_select_photo, null);
			popWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT, true);
			initPop(view);
		}
		popWindow.setAnimationStyle(android.R.style.Animation_InputMethod);
		popWindow.setFocusable(true);
		popWindow.setOutsideTouchable(true);
		popWindow.setBackgroundDrawable(new BitmapDrawable());
		popWindow
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
	}

	public void initPop(View view) {
		photograph = (TextView) view.findViewById(R.id.photograph);// 锟斤拷锟斤拷********
		albums = (TextView) view.findViewById(R.id.albums);// 锟斤拷锟� ********
		cancel = (LinearLayout) view.findViewById(R.id.cancel);// 取锟斤拷
		photograph.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				popWindow.dismiss();
				photoSaveName = "headview" + ".png";
				Uri imageUri = null;
				// 通锟斤拷锟斤拷锟秸伙拷取图片
				Intent openCameraIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				imageUri = Uri.fromFile(new File(photoSavePath, photoSaveName));
				openCameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION,0);
				openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(openCameraIntent, PHOTOTAKE);
			}
		});
		albums.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				popWindow.dismiss();
				// 通锟斤拷图锟斤拷锟饺⊥计�
				Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
				openAlbumIntent
						.setDataAndType(
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
								"image/*");
				startActivityForResult(openAlbumIntent, PHOTOZOOM);
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				popWindow.dismiss();
			}
		});
	}


	private void initView() {
		mGridView = (MyGridView) findViewById(R.id.child_grid);
        savebButton=(Button)findViewById(R.id.savamess);
		radioGroup = (RadioGroup) findViewById(R.id.SexradioGroup);
		add_picture = (TextView) findViewById(R.id.add_picture);
		subject_layout = (RelativeLayout) findViewById(R.id.subject_layout);
        tv_contact=(TextView)findViewById(R.id.name_tv11);
		subjectEditText = (EditText) findViewById(R.id.subject_edit);
		nameEditText = (EditText) findViewById(R.id.name_edit);
		ageEditText = (EditText) findViewById(R.id.age_edit);
		addressEditText = (EditText) findViewById(R.id.address_edit);
		phonoEditText = (EditText) findViewById(R.id.phono_edit);
		wordsEditText = (EditText) findViewById(R.id.motto_edit);
		hobbyEditText = (EditText) findViewById(R.id.hobby_edit);
		qqEditText = (EditText) findViewById(R.id.qq_edit);
		emailEditText = (EditText) findViewById(R.id.email_edit);
		constellationEditText = (EditText) findViewById(R.id.constallation_edit);
		tv_birthday = (TextView) findViewById(R.id.tv_birthday);
		bloodEditText = (EditText) findViewById(R.id.bloodtype_edit);
		nicknameEditText = (EditText) findViewById(R.id.nickname_edit);
		dreamEditText = (EditText) findViewById(R.id.dream_edit);
		bestcolorEditText = (EditText) findViewById(R.id.best_color_edit);
		beststarEditText = (EditText) findViewById(R.id.best_star_edit);
		bestsportEditText = (EditText) findViewById(R.id.best_sport_edit);
		initCodeResult();//初始化二维码数据
		tv_birthday.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				DateDialog dialog = new DateDialog(RegisterActivity.this);
				dialog.setDate(tv_birthday);
			}
		});
		add_picture.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(RegisterActivity.this,startSelectActivity.class);
				startActivityForResult(intent, 111);
				
			}
		});
		if (!"teacher".equals(flag)) {
			subject_layout.setVisibility(View.GONE);
		}
		//执行保存操作
        savebButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub		
				    ((Button) view).setEnabled(false);
					SavaDatabase();

			}
		});
		tv_contact.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(RegisterActivity.this, SortListViewActivity.class);
				startActivityForResult(intent,115);
			}
		});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK&& event.getAction() == KeyEvent.ACTION_DOWN) {
			setExitSwichLayout();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	//设置Activity进入动画
	@Override
	public void setEnterSwichLayout() {
		// TODO Auto-generated method stub
		SwitchLayout.getFadingIn(this);
	}
	//设置Activity退出动画
	@Override
	public void setExitSwichLayout() {
		// TODO Auto-generated method stub
		SwitchLayout.getFadingOut(this, true);
	}

}
