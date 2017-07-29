package com.sikefeng.tongxuelu.activity.fragment;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sikefeng.tongxuelu.R;
import com.sikefeng.tongxuelu.activity.MainActivity;
import com.sikefeng.tongxuelu.activity.activitygroup.SettingActivity;
import com.sikefeng.tongxuelu.activity.user.User;
import com.sikefeng.tongxuelu.activity.user.UserMsgActivity;
import com.sikefeng.tongxuelu.multipleimages.startSelectActivity;
import com.sikefeng.tongxuelu.music.MusicActivity;
import com.sikefeng.tongxuelu.scan.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by sikefeng on 2016/9/4.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {
    private Activity activity;
    private ImageView imageView;
    private RelativeLayout relativelayout_code, relativeLayout, relativeLayout_ablum, relativeLayout_settings, relativeLayout_music, relativelayout_down;
    private TextView tv_username;
    private User loginUser;
    public ImageView img_codebar = null;
    public Bitmap mBitmap = null;
    private PopupWindow popWindow;
    private ImageView img_code, img_icon;
    private Gson gson;
    private ImageView roundImageView;
    public static boolean isReLoad = false;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mine, container, false);
        initView(view);
        initData();
        return view;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        roundImageView = (ImageView) view.findViewById(R.id.roundImageView);
        imageView = (ImageView) view.findViewById(R.id.user_image);
        img_codebar = (ImageView) view.findViewById(R.id.img_codebar);
        tv_username = (TextView) view.findViewById(R.id.username);
        relativelayout_code = (RelativeLayout) view.findViewById(R.id.relativelayout_code);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.relativelayout);
        relativelayout_down = (RelativeLayout) view.findViewById(R.id.relativelayout_down);
        relativeLayout_music = (RelativeLayout) view.findViewById(R.id.relativelayout_music);
        relativeLayout_ablum = (RelativeLayout) view.findViewById(R.id.relativelayout_ablum);
        relativeLayout_settings = (RelativeLayout) view.findViewById(R.id.relativelayout_settings);
        relativelayout_down.setOnClickListener(this);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, UserMsgActivity.class));
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, UserMsgActivity.class));
            }
        });
        relativeLayout_ablum.setOnClickListener(this);
        relativeLayout_settings.setOnClickListener(this);
        relativeLayout_music.setOnClickListener(this);
        relativelayout_code.setOnClickListener(this);
        roundImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (MainActivity.isOpenedmenu) {
                    activity.sendBroadcast(new Intent("actionkkk"));
                } else {
                    MainActivity.isOpenedmenu = true;
                    activity.sendBroadcast(new Intent("actionkkk"));
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relativelayout_down:
                startActivity(new Intent(activity, MusicActivity.class));
                break;
            case R.id.relativelayout_ablum:
//                   startActivity(new Intent(activity, AlbumActivity.class));
                startActivity(new Intent(activity, startSelectActivity.class));
                break;
            case R.id.relativelayout_music:
                startActivity(new Intent(activity, CaptureActivity.class));
                break;
            case R.id.relativelayout_settings:
                startActivity(new Intent(activity, SettingActivity.class));
                break;
            case R.id.relativelayout_code:
                showPopupWindow(view);
                break;
            default:
                break;
        }
    }

    private void showPopupWindow(View parent) {
        if (popWindow == null) {
            View view = activity.getLayoutInflater().inflate(R.layout.code_layout, null);
            popWindow = new PopupWindow(view, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, true);
            img_code = (ImageView) view.findViewById(R.id.img_code);
            img_icon = (ImageView) view.findViewById(R.id.img_icon);
            TextView tv_back = (TextView) view.findViewById(R.id.tv_back);
            tv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popWindow.dismiss();
                }
            });
        }
        popWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(true);
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        popWindow.setSoftInputMode(WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG);
        popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);

//        final BmobFile bmobFile=loginUser.getIcon();
//        if (bmobFile!=null) {
//            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(activity));
//            ImageLoader.getInstance().displayImage(bmobFile.getFileUrl(activity),img_icon);
//        }
        BitmapDrawable mDrawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap mBitmap = mDrawable.getBitmap();
        mBitmap = CodeUtils.createImage(loginUser.getObjectId(), 300, 300, mBitmap);
        img_code.setImageBitmap(mBitmap);

    }

    @Override
    public void initData() {
        super.initData();
        loginUser = BmobUser.getCurrentUser(activity, User.class);
        tv_username.setText(loginUser.getTruename()+"");
        final BmobFile bmobFile = loginUser.getIcon();
        if (bmobFile != null) {
            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(activity));
            ImageLoader.getInstance().displayImage(bmobFile.getFileUrl(activity), imageView);
        }
        BitmapDrawable mDrawable = (BitmapDrawable) imageView.getDrawable();
        mBitmap = CodeUtils.createImage(loginUser.getObjectId(), 90, 90, mDrawable.getBitmap());
        img_codebar.setImageBitmap(mBitmap);

//        if (bmobFile!=null && NetworkUtils.isNetworkConnected(activity)){//生成带Logo的二维码图片
//              new AsyncTask<String, Boolean, Bitmap>(){
//                  @Override
//                  protected Bitmap doInBackground(String... params) {
//                      String imgUri = params[0];
//                      try {
//                          URL url=new URL(imgUri);
//                          HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                          conn.setDoInput(true);
//                          conn.connect();
//                          InputStream is = conn.getInputStream();
//                          mBitmap = CodeUtils.createImage(msg, 90, 90, BitmapFactory.decodeStream(is));
//                          System.out.println(msg);
//                          is.close();
//                      } catch (IOException e) {
//                          e.printStackTrace();
//                      }
//                      return mBitmap;
//                  }
//
//                  @Override
//                  protected void onPostExecute(Bitmap bitmap) {
//                      super.onPostExecute(bitmap);
//                      if (bitmap!=null){
//                          img_codebar.setImageBitmap(bitmap);
//                      }
//                  }
//              }.execute(bmobFile.getFileUrl(activity));


//        }else{
//
//        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (isReLoad){
            isReLoad=false;
            loginUser = BmobUser.getCurrentUser(activity, User.class);
            final BmobFile bmobFile = loginUser.getIcon();
            if (bmobFile != null) {
                ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(activity));
                ImageLoader.getInstance().displayImage(bmobFile.getFileUrl(activity), imageView);
            }
        }
    }
}
