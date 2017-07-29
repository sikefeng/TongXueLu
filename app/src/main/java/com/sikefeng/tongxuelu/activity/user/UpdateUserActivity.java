package com.sikefeng.tongxuelu.activity.user;


import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sikefeng.tongxuelu.R;
import com.sikefeng.tongxuelu.baseactivity.BaseActivity;
import com.sikefeng.tongxuelu.dialog.DateDialog;
import com.sikefeng.tongxuelu.widgets.ClearEditText;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

public class UpdateUserActivity extends BaseActivity {
	private RadioGroup radioGroup;
	private User loginUser;
	private EditText tv_nickname;
	private ClearEditText tv_truename,tv_phone,tv_age,tv_sex,tv_xingzuo,tv_qq,tv_email,tv_work,tv_company,tv_school;
	private TextView tv_back,tv_birthday;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.updateuser_msg_layout);
		initView();//初始化视图
	    loginUser= BmobUser.getCurrentUser(this, User.class);  //获取已登录对象和信息
    	tv_email.setText(loginUser.getEmail());
    	tv_nickname.setText(loginUser.getUsername());
		tv_truename.setText(loginUser.getTruename());
		tv_phone.setText(loginUser.getPhoneno());
    	tv_age.setText(loginUser.getAge());
		tv_birthday.setText(loginUser.getBirthday());
		tv_xingzuo.setText(loginUser.getXingzuo());
		tv_qq.setText(loginUser.getQQ());
		tv_work.setText(loginUser.getWork());
		tv_company.setText(loginUser.getCompany());
		tv_school.setText(loginUser.getSchool());
		String sex=loginUser.getSex();
		if (sex!=null&&sex.equals("男")){
			RadioButton radioButton = (RadioButton) radioGroup.getChildAt(0);
			radioButton.setChecked(true);
		}else {
			RadioButton radioButton = (RadioButton) radioGroup.getChildAt(1);
			radioButton.setChecked(true);
		}

		Button updateButton=(Button)findViewById(R.id.save_btn);
		updateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String sex="男";
				for (int i = 0; i < radioGroup.getChildCount(); i++) {
					RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
					if (radioButton.isChecked()) {
						sex = radioButton.getText().toString();
					}
				}
				String username=tv_nickname.getText().toString();
				String truename=tv_truename.getText().toString();
				String phone=tv_phone.getText().toString();
				String age=tv_age.getText().toString();
				String birth=tv_birthday.getText().toString();
				String xingzuo=tv_xingzuo.getText().toString();
				String qq=tv_qq.getText().toString();
				String work=tv_work.getText().toString();
				String company=tv_company.getText().toString();
				String school=tv_school.getText().toString();
		    	if (username!=null) {

					loginUser.setUsername(username);
					loginUser.setTruename(truename);
					loginUser.setPhoneno(phone);
					loginUser.setSex(sex);
					loginUser.setAge(age);
					loginUser.setBirthday(birth);
					loginUser.setXingzuo(xingzuo);
					loginUser.setCompany(company);
					loginUser.setQQ(qq);
					loginUser.setWork(work);
					loginUser.setSchool(school);


					loginUser.update(UpdateUserActivity.this, new UpdateListener() {
						@Override
						public void onSuccess() {
							Toast.makeText(getApplicationContext(), "用户信息修改成功", Toast.LENGTH_SHORT).show();
							UpdateUserActivity.this.finish();
						}
						@Override
						public void onFailure(int i, String s) {
							Toast.makeText(getApplicationContext(), "用户信息修改失败" + s, Toast.LENGTH_SHORT).show();
							UpdateUserActivity.this.finish();
						}
					});

				}else {
					Toast.makeText(getApplicationContext(), "用户名不能为空",Toast.LENGTH_SHORT).show();
				}
			}
		});
		
	}//****************************************************

	public void initView(){
		tv_back=(TextView)findViewById(R.id.tv_back);
		radioGroup = (RadioGroup) findViewById(R.id.SexradioGroup);
		tv_nickname=(EditText)findViewById(R.id.tv_nickname);
		tv_truename=(ClearEditText)findViewById(R.id.tv_truename);
		tv_phone=(ClearEditText)findViewById(R.id.tv_phone);
		tv_age=(ClearEditText)findViewById(R.id.tv_age);
		tv_birthday=(TextView)findViewById(R.id.tv_birthday);
		tv_xingzuo=(ClearEditText)findViewById(R.id.tv_collection);
		tv_qq=(ClearEditText)findViewById(R.id.tv_qq);
		tv_email=(ClearEditText)findViewById(R.id.tv_email);
		tv_work=(ClearEditText)findViewById(R.id.tv_work);
		tv_company=(ClearEditText)findViewById(R.id.tv_company);
		tv_company=(ClearEditText)findViewById(R.id.tv_company);
		tv_school=(ClearEditText)findViewById(R.id.tv_school);
		tv_birthday.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				DateDialog dialog = new DateDialog(UpdateUserActivity.this);
				dialog.setDate(tv_birthday);
			}
		});
		tv_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				UpdateUserActivity.this.finish();
			}
		});
	}

}
