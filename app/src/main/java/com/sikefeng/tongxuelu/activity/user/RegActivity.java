package com.sikefeng.tongxuelu.activity.user;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.sikefeng.tongxuelu.R;
import com.sikefeng.tongxuelu.baseactivity.BaseActivity;
import com.sikefeng.tongxuelu.bmobutils.BmobUtils;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

public class RegActivity extends BaseActivity {
    private EditText passwordEdittext,emailEdittext;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg_layout);
        passwordEdittext=(EditText)findViewById(R.id.password);
        emailEdittext=(EditText)findViewById(R.id.emaileditText);
        progressBar = (ProgressBar) findViewById(R.id.login_progress);

    }
    public void regClick(View view){
        progressBar.setVisibility(View.VISIBLE);
        final  String password=passwordEdittext.getText().toString();
        final String email=emailEdittext.getText().toString();
       
        final User user=new User();
        user.setUsername(email);
        user.setPassword(password);
        user.setEmail(email);
        user.setTruename("");
        user.setAge("");
        user.setSex("");
        user.setBirthday("");
        user.setXingzuo("");
        user.setQQ("");
        user.setWork("");
        user.setCompany("");
        user.setSchool("");
        user.setPhoneno("");
        user.signUp(RegActivity.this, new SaveListener() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.INVISIBLE);
            	BmobUser.logOut(getApplicationContext());//退出登录
                Toast.makeText(getApplicationContext(),"注册成功,验证信息已发送至邮箱！", Toast.LENGTH_SHORT).show();
                RegActivity.this.finish();
            }

            @Override
            public void onFailure(int i, String s) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),i+"注册失败"+ BmobUtils.showErrorMsg(i), Toast.LENGTH_SHORT).show();
            }
        });
        

    }
}
