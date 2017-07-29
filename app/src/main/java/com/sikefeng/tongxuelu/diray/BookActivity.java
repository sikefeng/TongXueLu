package com.sikefeng.tongxuelu.diray;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sikefeng.tongxuelu.R;
import com.sikefeng.tongxuelu.activity.user.User;
import com.sikefeng.tongxuelu.dialog.CommonDialogView;
import com.sikefeng.tongxuelu.utils.NetworkUtils;

import java.text.SimpleDateFormat;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

public class BookActivity extends Activity {
    private ListView listView;
    private TextView tv_back;
    private BookAdapter bookAdapter;
    private ImageButton btn_addbook;
    private DataImpl dataImpl;
    public static boolean isReload = false;
    private User loginUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_book);
        loginUser= BmobUser.getCurrentUser(this, User.class);  //获取已登录对象和信息
        dataImpl = new DataImpl(BookActivity.this);
        initView();//初始化视图
        initDate();//初始化数据
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.listview);
        tv_back=(TextView)findViewById(R.id.tv_back);
        btn_addbook = (ImageButton) findViewById(R.id.btn_addbook);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookActivity.this.finish();
            }
        });
        btn_addbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookActivity.this, NewBookActivity.class));
            }
        });
        DisplayMetrics dm = getResources().getDisplayMetrics();
        final int screenWidth = dm.widthPixels;
        final int screenHeight = dm.heightPixels-50;
        btn_addbook.setOnTouchListener(new View.OnTouchListener() {
            int lastX, lastY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                int ea = event.getAction();
                Log.i("TAG", "Touch:" + ea);
                switch (ea) {
                    //ACTION_DOWN和ACTION_UP就是单点触摸屏幕，按下去和放开的操作；
                    //ACTION_POINTER_DOWN和ACTION_POINTER_UP就是多点触摸屏幕，当有一只手指按下去的时候，另一只手指按下和放开的动作捕捉；
                    //ACTION_MOVE就是手指在屏幕上移动的操作；
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();// 获取触摸事件触摸位置的原始X坐标
                        lastY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                        int l = v.getLeft() + dx;
                        int b = v.getBottom() + dy;
                        int r = v.getRight() + dx;
                        int t = v.getTop() + dy;
                        // 下面判断移动是否超出屏幕
                        if (l < 0) {
                            l = 0;
                            r = l + v.getWidth();
                        }
                        if (t < 0) {
                            t = 0;
                            b = t + v.getHeight();
                        }
                        if (r > screenWidth) {
                            r = screenWidth;
                            l = r - v.getWidth();
                        }
                        if (b > screenHeight) {
                            b = screenHeight;
                            t = b - v.getHeight();
                        }
                        v.layout(l, t, r, b);
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
//                        Toast.makeText(ContactsActivity.this,"当前位置：" + l + "," + t + "," + r + "," + b,Toast.LENGTH_SHORT).show();
                        v.postInvalidate();
                        break;
                    case MotionEvent.ACTION_UP:

                        break;
                }
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(BookActivity.this,BookMsgActivity.class);
                Book book=(Book)bookAdapter.getItem(position);
                intent.putExtra("id",book.getId());
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Book book=(Book)bookAdapter.getItem(position);
                showNoticeDialog(book.getId());
                return true;
            }
        });
    }
    private void showNoticeDialog(final int id){
        CommonDialogView commonDialogView=new CommonDialogView(BookActivity.this);
        String time=new SimpleDateFormat("MM-dd hh:mm").format(System.currentTimeMillis());
        commonDialogView.showDialog(BookActivity.this,true,null,"删除提示:",time,"是否删除？","确定","取消");
        commonDialogView.setmOnDialogClickLister(new CommonDialogView.OnDialogClickLister() {
            @Override
            public void onOk() {
                if (NetworkUtils.isNetworkConnected(BookActivity.this)){
                    dataImpl.deleteBook(id);
                    updateUserMsg();
                }else {
                    Toast.makeText(BookActivity.this, "网络未连接", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancal() {

            }
        });
    }

    private void initDate() {
            List<Book> books=dataImpl.findAllBooks();
            if (books!=null){
                    bookAdapter = new BookAdapter(BookActivity.this, books);
                    listView.setAdapter(bookAdapter);
            }
    }
    private void updateUserMsg(){
        Gson gson=new Gson();
        List<Book> bookList=dataImpl.findAllBooks();
        Result result=new Result(bookList.size(),bookList);
        loginUser.setDirayJsonString(gson.toJson(result));
        loginUser.update(BookActivity.this, new UpdateListener() {
            @Override
            public void onSuccess() {
                initDate();
                Toast.makeText(BookActivity.this, "已移除！", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(int i, String s) {


            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isReload) {
            initDate();
            isReload=false;
        }
    }
}
