package com.sikefeng.tongxuelu.activity.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sikefeng.tongxuelu.R;
import com.sikefeng.tongxuelu.activity.MainActivity;
import com.sikefeng.tongxuelu.baseactivity.BaseActivity;
import com.sikefeng.tongxuelu.bmobutils.BmobUtils;
import com.sikefeng.tongxuelu.diray.Book;
import com.sikefeng.tongxuelu.diray.DataImpl;
import com.sikefeng.tongxuelu.diray.DataPersonImpl;
import com.sikefeng.tongxuelu.entity.Person;
import com.sikefeng.tongxuelu.screenlock.Welcome;
import com.sikefeng.tongxuelu.util.AESUtil;
import com.sikefeng.tongxuelu.version.AppMsg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends BaseActivity {
    private EditText usernameEdittext,passwordEdittext;
	private Button loginButton;
    private User loginUser = null;
	private String sdcardDir = "/storage/sdcard0/TongXueLu/Data/";
	private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        initView();
		String password="0123456789";
		String key="123456";
		String pwd;
		try {
			pwd= AESUtil.encrypt(key,password);
			System.out.println("1加密结果："+ pwd);
			pwd=AESUtil.decrypt(key,pwd);
			System.out.println("1解密结果："+ pwd);
			System.out.println("---------------------------------------------");
			pwd=AESUtil.encrypt(key,password);
			key="654321";
			pwd=AESUtil.decrypt(key,pwd);
			System.out.println("2解密结果："+ pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}




    }

	private void initData(){
		Gson gson = new Gson();
		DataImpl dataImpl=new DataImpl(LoginActivity.this);
		dataImpl.deleteBookAll();
		String diary=loginUser.getDirayJsonString();
		if (diary!=null&&!TextUtils.isEmpty(diary)){
		try {
			JSONObject jsonObject=new JSONObject(diary);
			JSONArray  jsonArray=jsonObject.getJSONArray("booksData");
			for (int i = 0; i < jsonArray.length(); i++) {
				Book book = gson.fromJson(jsonArray.optString(i), Book.class);
				System.out.println(book.toString());
				dataImpl.saveBook(book);
			}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		DataPersonImpl dataPersonImpl=new DataPersonImpl(LoginActivity.this);
		dataPersonImpl.deletePersonAll();
		String persons=loginUser.getPersonJsonData();
		try {
			if (persons!=null&&!TextUtils.isEmpty(persons)){
				JSONArray  jsonArray=new JSONArray(persons);
				for (int i = 0; i < jsonArray.length(); i++) {
					Person person = gson.fromJson(jsonArray.optString(i), Person.class);
					dataPersonImpl.savePerson(person);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}


		        SharedPreferences sp=LoginActivity.this.getSharedPreferences("lock",MODE_PRIVATE);
				boolean isLocked=sp.getBoolean("isLock",false);
				String psswd=sp.getString("password","");
				if (isLocked && psswd!=null&&!TextUtils.isEmpty(psswd)) {
					Intent intent = new Intent(getApplicationContext(),Welcome.class);
				    startActivity(intent);
				    LoginActivity.this.finish();
				}else {
					startActivity(new Intent(LoginActivity.this,MainActivity.class));
					LoginActivity.this.finish();
				}

	}

    public void initView(){
		progressBar = (ProgressBar) findViewById(R.id.login_progress);
		loginButton=(Button)findViewById(R.id.login_btn);
    	usernameEdittext=(EditText)findViewById(R.id.username);
        passwordEdittext=(EditText)findViewById(R.id.password);
    	TextView regTextView=(TextView)findViewById(R.id.reg_tv);
    	TextView forgetTextView=(TextView)findViewById(R.id.forget_tv);
		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loginUser();
				((Button)v).setEnabled(false);
			}
		});
    	regTextView.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(LoginActivity.this,RegActivity.class);
		        startActivity(intent);
			}
		});
    	forgetTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 Intent intent=new Intent(LoginActivity.this,ForgetPasswdActicity.class);
				 intent.putExtra("index",1);
                 startActivity(intent);
			}
		});
    }

    public void loginUser(){
		    progressBar.setVisibility(View.VISIBLE);
			String username=usernameEdittext.getText().toString();
	        String password=passwordEdittext.getText().toString();
			final BmobUser bmobUser=new BmobUser();
	        if (username!=null&&password!=null&&!TextUtils.isEmpty(username)&&!TextUtils.isEmpty(password)) {
	        	bmobUser.setUsername(username);
	            bmobUser.setPassword(password);
	            bmobUser.login(this, new SaveListener() {
					@Override
	                public void onSuccess() {
	                    if (bmobUser.getEmailVerified()) { //判断邮箱是否已经验证
	                        Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
							loginUser = BmobUser.getCurrentUser(LoginActivity.this, User.class); // 获取已登录对象和信息
							initAppVersion();
							initData();

	                    } else {
							loginButton.setEnabled(true);
							BmobUser.logOut(getApplicationContext());//退出登录
	                        Toast.makeText(getApplicationContext(), "邮箱未验证", Toast.LENGTH_SHORT).show();
	                    }
						progressBar.setVisibility(View.INVISIBLE);
	                }
	                @Override
	                public void onFailure(int i, String s) {
						progressBar.setVisibility(View.INVISIBLE);
						loginButton.setEnabled(true);
	                    Toast.makeText(getApplicationContext(), BmobUtils.showErrorMsg(i), Toast.LENGTH_SHORT).show();
	                }
	            });
			}else {
				loginButton.setEnabled(true);
				Toast.makeText(getApplicationContext(), "用户名或密码不能为空" , Toast.LENGTH_SHORT).show();
			}
        
    }
	private void initAppVersion(){
		BmobQuery<AppMsg> query=new BmobQuery<AppMsg>();
		query.findObjects(this, new FindListener<AppMsg>() {
			@Override
			public void onSuccess(List<AppMsg> list) {
				if (list!=null&&list.size()>0){
					SharedPreferences sp=LoginActivity.this.getSharedPreferences("appmsg",MODE_PRIVATE);
					SharedPreferences.Editor editor=sp.edit();
					editor.putInt("appVersion",list.get(0).getAppVersion());
					editor.putString("appUrl",list.get(0).getAppUrl());
					editor.commit();
				}else{
					AppMsg appVersion=new AppMsg(1,"http://www.sssss.com");
					appVersion.save(LoginActivity.this, new SaveListener() {
						@Override
						public void onSuccess() {
							SharedPreferences sp=LoginActivity.this.getSharedPreferences("appmsg",MODE_PRIVATE);
							SharedPreferences.Editor editor=sp.edit();
							editor.putInt("appVersion",1);
							editor.putString("appUrl","http://www.ssss.com");
							editor.commit();
						}
						@Override
						public void onFailure(int i, String s) {

						}
					});
				}
			}
			@Override
			public void onError(int i, String s) {
				Toast.makeText(LoginActivity.this, BmobUtils.showErrorMsg(i), Toast.LENGTH_SHORT).show();;
			}
		});
	}


}