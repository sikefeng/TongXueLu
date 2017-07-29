package com.sikefeng.tongxuelu.activity.activitygroup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sikefeng.tongxuelu.R;
import com.sikefeng.tongxuelu.activity.MainActivity;
import com.sikefeng.tongxuelu.activity.user.User;
import com.sikefeng.tongxuelu.activity.user.UserMsgActivity;
import com.sikefeng.tongxuelu.download.DownloadListActivity;
import com.sikefeng.tongxuelu.music.MusicActivity;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class MineActivity extends Activity {
    private ImageView imageView;
    private RelativeLayout relativeLayout,relativeLayout_ablum,relativeLayout_settings,relativeLayout_music
            ,relativelayout_down;
    private TextView tv_username;
    private User loginUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        initView();//初始化视图
        loginUser= BmobUser.getCurrentUser(getApplicationContext(), User.class);
        tv_username.setText(loginUser.getUsername());
        BmobFile bmobFile=loginUser.getIcon();
        if (bmobFile!=null) {
            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
            ImageLoader.getInstance().displayImage(bmobFile.getFileUrl(getApplicationContext()),imageView);
        }

    }
    private void initView(){
        imageView=(ImageView)findViewById(R.id.user_image);
        tv_username=(TextView)findViewById(R.id.username);
        relativeLayout=(RelativeLayout)findViewById(R.id.relativelayout);
        relativelayout_down=(RelativeLayout)findViewById(R.id.relativelayout_down);
        relativeLayout_music=(RelativeLayout)findViewById(R.id.relativelayout_music);
        relativeLayout_ablum=(RelativeLayout)findViewById(R.id.relativelayout_ablum);
        relativeLayout_settings=(RelativeLayout)findViewById(R.id.relativelayout_settings);
        relativelayout_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                startActivity(new Intent(MineActivity.this,DownloadListActivity.class));
            }
        });
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                startActivity(new Intent(MineActivity.this, UserMsgActivity.class));
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                startActivity(new Intent(MineActivity.this, UserMsgActivity.class));
            }
        });
        relativeLayout_ablum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                startActivity(new Intent(MineActivity.this, AlbumActivity.class));
            }
        });
        relativeLayout_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                startActivity(new Intent(MineActivity.this, SettingActivity.class));
            }
        });
        relativeLayout_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                startActivity(new Intent(MineActivity.this, MusicActivity.class));
            }
        });

    }
    // 退出应用程序代码
    private long exittime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (MainActivity.isOpenedmenu) {
                MainActivity.isOpenedmenu = false;
                sendBroadcast(new Intent("actionkkk"));
                return false;
            }
            if ((System.currentTimeMillis() - exittime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exittime = System.currentTimeMillis();

            } else {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
