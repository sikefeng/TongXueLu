package com.sikefeng.tongxuelu.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.sikefeng.tongxuelu.R;
import com.sikefeng.tongxuelu.baseactivity.BaseActivity;
import com.sikefeng.tongxuelu.bmobutils.BmobUtils;

import cn.bmob.v3.listener.ResetPasswordByEmailListener;
import cn.bmob.v3.listener.UpdateListener;

public class ForgetPasswdActicity extends BaseActivity {
    private TextView tv_back;
    private ProgressBar progressBar;
    private EditText old_password,new_password;
    private EditText email_edit;
    private int index=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=this.getIntent();
        index=intent.getIntExtra("index",0);
        if (index==0){
            setContentView(R.layout.activity_forget_passwd);
            old_password=(EditText)findViewById(R.id.old_password);
            new_password=(EditText)findViewById(R.id.new_password);
        }else{
            setContentView(R.layout.find_passwe_from);
            email_edit=(EditText)findViewById(R.id.email_edit);
        }
        tv_back=(TextView)findViewById(R.id.tv_back);
        progressBar = (ProgressBar) findViewById(R.id.login_progress);

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgetPasswdActicity.this.finish();
            }
        });

    }



    public void updatePassword(View view){
        if (index==0){
            findPasswdByOldPasswd();
        }else {
            findPasswdByEmail();
        }

    }

    public void findPasswdByOldPasswd(){
        progressBar.setVisibility(View.VISIBLE);
        String old_psswd=old_password.getText().toString().trim();
        String new_psswd=new_password.getText().toString().trim();
        if (TextUtils.isEmpty(old_psswd)||TextUtils.isEmpty(new_psswd)){
            toast("旧密码或新密码不能为空");
        }else{
            User loginUser=new UserUtils(ForgetPasswdActicity.this).getLoginUser();
            loginUser.updateCurrentUserPassword(ForgetPasswdActicity.this, old_psswd, new_psswd, new UpdateListener() {
                @Override
                public void onSuccess() {
                    progressBar.setVisibility(View.INVISIBLE);
                    toast("密码修改成功，可以用新密码进行登录啦");
                    BmobUtils.exitLoginUser(ForgetPasswdActicity.this);
                }
                @Override
                public void onFailure(int i, String s) {
                    toast(s);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
    }
    public void findPasswdByEmail(){
        progressBar.setVisibility(View.VISIBLE);
        final String email=email_edit.getText().toString().trim();
        User loginUser=new UserUtils(ForgetPasswdActicity.this).getLoginUser();
        loginUser.resetPasswordByEmail(ForgetPasswdActicity.this, email, new ResetPasswordByEmailListener() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),"重置密码请求成功，请到" + email + "邮箱进行重置密码",Toast.LENGTH_SHORT).show();
                ForgetPasswdActicity.this.finish();
            }

            @Override
            public void onFailure(int i, String s) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),"密码修改失败"+s,Toast.LENGTH_SHORT).show();
            }
        });
    }

}
