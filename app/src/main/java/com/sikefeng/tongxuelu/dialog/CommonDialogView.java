package com.sikefeng.tongxuelu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sikefeng.tongxuelu.R;
import com.sikefeng.tongxuelu.utils.ToastUtils;

/**
 * Created by sikefeng on 2016/8/29.
 */
public class CommonDialogView extends Dialog implements View.OnClickListener {

    private ImageView imageView;
    private TextView tv_title, tv_content, tv_time, tv_ok, tv_cancal;

    public CommonDialogView(Context context) {
        super(context, R.style.loading_dialog_style);
    }
//    public CommonDialogView(Context context, int theme) {
//        super(context, theme);
//    }

//    protected CommonDialogView(Context context, boolean cancelable, OnCancelListener cancelListener) {
//        super(context, cancelable, cancelListener);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_view);
        initView();
    }

    private void initView() {
        imageView = (ImageView) findViewById(R.id.iv_appicon);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_ok = (TextView) findViewById(R.id.tv_ok);
        tv_cancal = (TextView) findViewById(R.id.tv_cancal);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_ok.setOnClickListener(this);
        tv_cancal.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ok:
                if (mOnDialogClickLister != null) {
                    mOnDialogClickLister.onOk();
                }
                cancel();//清除对话框
                break;
            case R.id.tv_cancal:
                if (mOnDialogClickLister != null) {
                    mOnDialogClickLister.onCancal();
                }
                cancel();//清除对话框
                break;
        }
    }

    public void showDialog(final Context context, boolean cancelable, Bitmap bitmap, String title, String rightText, String content, String ok, String cancal) {
        show();//显示对话框
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setVisibility(View.GONE);
        }
        tv_time.setText(rightText);
        tv_title.setText(title);
        tv_content.setText(content);
        tv_ok.setText(ok);
        tv_cancal.setText(cancal);
        setCanceledOnTouchOutside(false);
        setCancelable(cancelable);

    }

    public void setRightText(String rightText) {
        tv_time.setText(rightText);
    }

    public void setTitleIcon(int resId) {
        imageView.setImageResource(resId);
    }

    public void setTitleBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setVisibility(View.GONE);
        }
    }

    //设置对话框标题
    public void setTitleText(String title) {
        tv_title.setText(title);
    }

    //设置对话框提示内容
    public void setContentText(String content) {
        tv_content.setText(content);
    }

    public void setOkText(String ok) {
        tv_ok.setText(ok);
    }

    public void setCancalText(String cancalText) {
        tv_cancal.setText(cancalText);
    }


    private OnDialogClickLister mOnDialogClickLister;

    public OnDialogClickLister getmOnDialogClickLister() {
        return mOnDialogClickLister;
    }

    public void setmOnDialogClickLister(OnDialogClickLister mOnDialogClickLister) {
        this.mOnDialogClickLister = mOnDialogClickLister;
    }

    public static interface OnDialogClickLister {
        void onOk();

        void onCancal();
    }

    private void initDialog(final Context mcontext) {
//          CommonDialogView commonDialogView=new CommonDialogView(mcontext);
//          commonDialogView.showDialog(mcontext,cancelable,bitmap,title,rightText,content,okText,cancalText);
//          commonDialogView.setmOnDialogClickLister(new OnDialogClickLister() {
//            @Override
//            public void onOk() {
//                ToastUtils.showShort(mcontext, "OK");
//            }
//            @Override
//            public void onCancal() {
//                ToastUtils.showShort(mcontext, "Cancal");
//            }
//        });
        final CommonDialogView commonDialogView = new CommonDialogView(mcontext);
        commonDialogView.show();
        commonDialogView.setTitle("title");
        commonDialogView.setRightText("time");
        commonDialogView.setCancalText("content");
        commonDialogView.setOkText("OK");
        commonDialogView.setCancalText("Cancal");
        commonDialogView.setTitleIcon(R.mipmap.app_icon);
        commonDialogView.setmOnDialogClickLister(new OnDialogClickLister() {
            @Override
            public void onOk() {
                ToastUtils.showShort(mcontext, "OK");
                commonDialogView.cancel();//自动取消对话框
            }

            @Override
            public void onCancal() {
                ToastUtils.showShort(mcontext, "Cancal");
                commonDialogView.cancel();//自动取消对话框
            }
        });

    }
}
