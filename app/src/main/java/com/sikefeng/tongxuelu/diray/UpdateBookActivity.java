package com.sikefeng.tongxuelu.diray;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sikefeng.tongxuelu.R;
import com.sikefeng.tongxuelu.activity.user.User;
import com.sikefeng.tongxuelu.dialog.LoadingDialog;
import com.sikefeng.tongxuelu.utils.NetworkUtils;
import com.sikefeng.tongxuelu.utils.TimeUtils;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;


public class UpdateBookActivity extends Activity {
    private EditText edit_title, edit_content;
    private TextView tv_save,tv_back;
    private DataImpl data;
    private Book book;
    private int Id=0;
    private int position=0;
    private User loginUser;
    private LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_update_book);
        data=new DataImpl(UpdateBookActivity.this);
        loginUser= BmobUser.getCurrentUser(this, User.class);  //获取已登录对象和信息
        initView();

    }
    private void initView(){
        edit_content=(LineEditText)findViewById(R.id.edit_content);
        edit_title=(EditText)findViewById(R.id.edit_title);
        tv_save=(TextView)findViewById(R.id.tv_save);
        tv_back=(TextView)findViewById(R.id.tv_back);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent=this.getIntent();
        Id=intent.getIntExtra("id",0);
        book=data.findBook(Id);
        edit_title.setText(book.getTitle());
        edit_content.setText(book.getContent());
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBook();
            }
        });
    }
    public void updateBook() {
        String title=edit_title.getText().toString();
        if (!TextUtils.isEmpty(title)){
            String content=edit_content.getText().toString();
            if (!TextUtils.isEmpty(content)){
               if (NetworkUtils.isNetworkConnected(UpdateBookActivity.this)){
                   Book book=new Book(Id,title,content, TimeUtils.getCurrentTime());
                   data.updateBook(book);
                   updateUserMsg();
               }else {
                   Toast.makeText(UpdateBookActivity.this, "网络未连接", Toast.LENGTH_SHORT).show();
               }
            }else {
                Toast.makeText(UpdateBookActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(UpdateBookActivity.this, "标题不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUserMsg(){
        loadingDialog = LoadingDialog.getInstance();
        loadingDialog.showDialogForLoading(UpdateBookActivity.this, true, "正在修改数据......");
        Gson gson=new Gson();
        List<Book> bookList=data.findAllBooks();
        Result result=new Result(bookList.size(),bookList);
        loginUser.setDirayJsonString(gson.toJson(result));
        loginUser.update(UpdateBookActivity.this, new UpdateListener() {
            @Override
            public void onSuccess() {
                BookActivity.isReload=true;
                BookMsgActivity.isReload=true;
                finish();
                Toast.makeText(UpdateBookActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(int i, String s) {
                loadingDialog.hideDialogForLoading();
            }
        });
    }


}
