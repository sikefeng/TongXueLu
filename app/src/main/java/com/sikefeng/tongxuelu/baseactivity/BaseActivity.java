package com.sikefeng.tongxuelu.baseactivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sikefeng.tongxuelu.R;
import com.tandong.swichlayout.BaseEffects;
import com.tandong.swichlayout.SwichLayoutInterFace;
import com.tandong.swichlayout.SwitchLayout;


public class BaseActivity extends Activity implements SwichLayoutInterFace {//实现SwichLayoutInterFace接口

    public TextView title_txt;
	public LinearLayout title_left;
	public LinearLayout title_right;
	public TextView button_left;
	public TextView button_right;
	public ImageView img_left;
	public ImageView img_right;

    private Toast mToast;
    protected int key = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

    }

    public void displayLeft() {
        title_left.setVisibility(View.INVISIBLE);
    }

    public void displayRight() {
        title_right.setVisibility(View.INVISIBLE);
    }

    public void showLeftImg() {
        button_left.setVisibility(View.GONE);
        img_left.setVisibility(View.VISIBLE);
    }

    public void showRightImg() {
        button_right.setVisibility(View.GONE);
        img_right.setVisibility(View.VISIBLE);
    }


    public void setLeftText(String text) {
        button_left.setText(text);
    }
    public void setLeftBG(int id) {
        button_left.setBackgroundResource(id);
    }
    public void setLeftImgBG(int id) {
        img_left.setBackgroundResource(id);
    }
    public void setRightText(String text) {
        button_right.setText(text);
    }
    public void setRightBG(int id) {
        button_right.setBackgroundResource(id);
    }
    public void setRightImgBG(int id) {
        img_right.setBackgroundResource(id);
    }
    public void setTitle(String text) {
        title_txt.setText(text);
    }


    public void initView() {
        // TODO Auto-generated method stub
        title_txt = (TextView) findViewById(R.id.tvTitle);
        title_left = (LinearLayout) findViewById(R.id.top_left_linear);
        title_right = (LinearLayout) findViewById(R.id.top_right_linear);
        button_left = (TextView) findViewById(R.id.title_left);
        button_right = (TextView) findViewById(R.id.title_right);
        img_left = (ImageView) findViewById(R.id.title_left_img);
        img_right = (ImageView) findViewById(R.id.title_right_img);
    }
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    public void showToast(String text) {
        if (!TextUtils.isEmpty(text)) {
            if (mToast == null) {
                mToast = Toast.makeText(getApplicationContext(), text,Toast.LENGTH_SHORT);
            } else {
                mToast.setText(text);
            }
            mToast.show();
        }
    }

    public void showToast(int resId) {
        if (mToast == null) {
            mToast = Toast.makeText(getApplicationContext(), resId, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(resId);
        }
        mToast.show();
    }

    public static void showLog(String msg) {
        Log.i("smile", msg);
    }

    @Override
    public void setEnterSwichLayout() {
        switch (key) {
            case 0:   //3D翻转
                SwitchLayout.get3DRotateFromLeft(this, false, null);
                // 三个参数分别为（Activity/View，是否关闭Activity，特效（可为空））
                break;
            case 1:   //底部滑入
                SwitchLayout.getSlideFromBottom(this, false, BaseEffects.getMoreSlowEffect());
                break;
            case 2:   //顶部滑入
                SwitchLayout.getSlideFromTop(this, false, BaseEffects.getReScrollEffect());
                break;
            case 3:   //左侧滑入
                SwitchLayout.getSlideFromLeft(this, false, BaseEffects.getLinearInterEffect());
                break;
            case 4:   //右侧滑入
                SwitchLayout.getSlideFromRight(this, false, null);
                break;
            case 5:   //淡入淡出
                SwitchLayout.getFadingIn(this);
                break;
            case 6:   //中心缩放
                SwitchLayout.ScaleBig(this, false, null);
                break;
            case 7:   //上下翻转
                SwitchLayout.FlipUpDown(this, false, BaseEffects.getQuickToSlowEffect());
                break;
            case 8:   //左上角缩放
                SwitchLayout.ScaleBigLeftTop(this, false, null);
                break;
            case 9:   //震动模式
                SwitchLayout.getShakeMode(this, false, null);
                break;
            case 10:   //左侧中心旋转
                SwitchLayout.RotateLeftCenterIn(this, false, null);
                break;
            case 11:   //左上角旋转
                SwitchLayout.RotateLeftTopIn(this, false, null);
                break;
            case 12:   //中心旋转
                SwitchLayout.RotateCenterIn(this, false, null);
                break;
            case 13:   //横向展开
                SwitchLayout.ScaleToBigHorizontalIn(this, false, null);
                break;
            case 14:   //横向展开
                SwitchLayout.ScaleToBigVerticalIn(this, false, null);
                break;
            default:
                //淡入淡出
                SwitchLayout.getFadingIn(this);
                break;
        }

    }

    @Override
    public void setExitSwichLayout() {
        switch (key) {
            case 0:
                SwitchLayout.get3DRotateFromRight(this, true, null);
                break;
            case 1:
                SwitchLayout.getSlideToBottom(this, true, BaseEffects.getMoreSlowEffect());
                break;
            case 2:
                SwitchLayout.getSlideToTop(this, true, BaseEffects.getReScrollEffect());
                break;
            case 3:
                SwitchLayout.getSlideToLeft(this, true, BaseEffects.getLinearInterEffect());
                break;
            case 4:
                SwitchLayout.getSlideToRight(this, true, null);
                break;
            case 5:
                SwitchLayout.getFadingOut(this, true);
                break;
            case 6:
                SwitchLayout.ScaleSmall(this, true, null);
                break;
            case 7:
                SwitchLayout.FlipUpDown(this, true, BaseEffects.getQuickToSlowEffect());
                break;
            case 8:
                SwitchLayout.ScaleSmallLeftTop(this, true, null);
                break;
            case 9:
                SwitchLayout.getShakeMode(this, true, null);
                break;
            case 10:
                SwitchLayout.RotateLeftCenterOut(this, true, null);
                break;
            case 11:
                SwitchLayout.RotateLeftTopOut(this, true, null);
                break;
            case 12:
                SwitchLayout.RotateCenterOut(this, true, null);
                break;
            case 13:
                SwitchLayout.ScaleToBigHorizontalOut(this, true, null);
                break;
            case 14:
                SwitchLayout.ScaleToBigVerticalOut(this, true, null);
                break;
            default:
                break;
        }

    }
}
