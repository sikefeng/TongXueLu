package com.sikefeng.tongxuelu.manager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.sikefeng.tongxuelu.R;
import com.sikefeng.tongxuelu.entity.IndexInfo;

import cn.bmob.v3.listener.SaveListener;

public class AddActivity extends Activity {
    private EditText edit_title, edit_account, edit_passwd,edit_content;
    private Button btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        edit_title = (EditText) findViewById(R.id.edit_title);
        edit_account = (EditText) findViewById(R.id.edit_account);
        edit_passwd = (EditText) findViewById(R.id.edit_passwd);
        edit_content = (EditText) findViewById(R.id.edit_content);
        btn_save=(Button)findViewById(R.id.btn_save);


    }

    public void OnSave(View view){
        String title=edit_title.getText().toString();
        String content=edit_content.getText().toString();
        String account=edit_account.getText().toString();
        String passwd=edit_passwd.getText().toString();
        IndexInfo indexInfo=new IndexInfo(title,content,account,passwd);
        indexInfo.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
                ManagerActivity.isRefresh=true;
                AddActivity.this.finish();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(getApplicationContext(), "保存失败" + s, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
