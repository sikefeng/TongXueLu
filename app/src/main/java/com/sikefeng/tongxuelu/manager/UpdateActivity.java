package com.sikefeng.tongxuelu.manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sikefeng.tongxuelu.R;
import com.sikefeng.tongxuelu.entity.IndexInfo;

import cn.bmob.v3.listener.UpdateListener;

public class UpdateActivity extends Activity {
    private EditText edit_title, edit_account, edit_passwd,edit_content;
    private Button btn_save;
    private IndexInfo book;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        edit_title = (EditText) findViewById(R.id.edit_title);
        edit_account = (EditText) findViewById(R.id.edit_account);
        edit_passwd = (EditText) findViewById(R.id.edit_passwd);
        edit_content = (EditText) findViewById(R.id.edit_content);
        btn_save = (Button) findViewById(R.id.btn_save);
        Intent intent=this.getIntent();
        book=(IndexInfo)intent.getSerializableExtra("book");
        edit_title.setText(book.getContent());
        edit_content.setText(book.getTitle());
        edit_account.setText(book.getImgUrl());
        edit_passwd.setText(book.getWebsite());
    }

    public void OnSave(View view) {
        String title = edit_title.getText().toString();
        String account = edit_account.getText().toString();
        String content=edit_content.getText().toString();
        String passwd = edit_passwd.getText().toString();
        IndexInfo updatebook = new IndexInfo(title,content, account, passwd);
        updatebook.update(UpdateActivity.this, book.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
                ManagerActivity.isRefresh = true;
                UpdateActivity.this.finish();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(getApplicationContext(), "修改失败" + s, Toast.LENGTH_SHORT).show();
            }
        });

    }


}