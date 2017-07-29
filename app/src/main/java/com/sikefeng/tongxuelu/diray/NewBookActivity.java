package com.sikefeng.tongxuelu.diray;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.sikefeng.tongxuelu.dialog.DialogView;

import java.text.SimpleDateFormat;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;


public class NewBookActivity extends Activity {
    private EditText edit_title;
    private EditText edit_content;
    private TextView tv_save,tv_back;
    private DataImpl data;
    private User loginUser;
    private DialogView dialogView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_new_book);
        initView();
        data = new DataImpl(NewBookActivity.this);
        loginUser= BmobUser.getCurrentUser(this, User.class);  //获取已登录对象和信息
        dialogView=new DialogView(NewBookActivity.this);
        dialogView.setmOnDialogClickLister(new DialogView.OnDialogClickLister() {
            @Override
            public void onOk() {
                dialogView.hideDialogForLoading();
            }

            @Override
            public void onCancal() {
                dialogView.hideDialogForLoading();
            }
        });

    }

    private void initView() {
        edit_content = (EditText) findViewById(R.id.edit_content);
        edit_title = (EditText) findViewById(R.id.edit_title);
        tv_save = (TextView) findViewById(R.id.tv_save);
        tv_back=(TextView)findViewById(R.id.tv_back);
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBook();
            }
        });
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void saveBook() {
        if (isNetworkConnected(NewBookActivity.this)){
            String title = edit_title.getText().toString();
            if (!TextUtils.isEmpty(title)) {
                String content = edit_content.getText().toString();
                if (!TextUtils.isEmpty(content)) {
                    Book book = new Book(title, content, getCurrentTime());
                    data.saveBook(book);
                    updateUserMsg();
                } else {
                    Toast.makeText(NewBookActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(NewBookActivity.this, "标题不能为空", Toast.LENGTH_SHORT).show();
            }
        }else{
//            dialogView.showDialog(NewBookActivity.this,true,BitmapFactory.decodeResource(getResources(),R.mipmap.app_icon),"温馨提示:","您的网络尚未连接！","确定","离线保存");
            Toast.makeText(NewBookActivity.this, "网络未连接", Toast.LENGTH_SHORT).show();
        }

    }


    private void updateUserMsg(){
        Gson gson=new Gson();
        List<Book> bookList=data.findAllBooks();
        Result result=new Result(bookList.size(),bookList);
        loginUser.setDirayJsonString(gson.toJson(result));
        loginUser.update(NewBookActivity.this, new UpdateListener() {
            @Override
            public void onSuccess() {
                BookActivity.isReload = true;
                Toast.makeText(NewBookActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                finish();
            }
            @Override
            public void onFailure(int i, String s) {


            }
        });
    }
    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd hh:mm");
        String time = sdf.format(System.currentTimeMillis());
        return time;
    }

    // 判断是否有网络连接
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }



}


