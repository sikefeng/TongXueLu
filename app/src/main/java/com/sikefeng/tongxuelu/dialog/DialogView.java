package com.sikefeng.tongxuelu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sikefeng.tongxuelu.R;

import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2016/7/5.
 */
public class DialogView implements View.OnClickListener {

        /** 加载数据对话框 */
        private Dialog dialog;
        private ImageView imageView;
        private TextView tv_title,tv_content,tv_time,tv_ok,tv_cancal;
        public DialogView(Context context){
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_view, null);
            imageView=(ImageView)view.findViewById(R.id.iv_appicon);
            tv_title=(TextView)view.findViewById(R.id.tv_title);
            tv_time=(TextView)view.findViewById(R.id.tv_time);
            tv_ok=(TextView)view.findViewById(R.id.tv_ok);
            tv_cancal=(TextView)view.findViewById(R.id.tv_cancal);
            tv_content=(TextView)view.findViewById(R.id.tv_content);
            tv_ok.setOnClickListener(this);
            tv_cancal.setOnClickListener(this);
            dialog = new Dialog(context, R.style.loading_dialog_style);
//            dialog.setContentView(view,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            dialog.setContentView(view);
	     	dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
        }

    public  void showDialog(final Context context, boolean cancelable, Bitmap bitmap, String title, String content,String ok,String  cancal) {

            if (bitmap!=null){
                imageView.setImageBitmap(bitmap);
            }else{
                imageView.setVisibility(View.GONE);
            }
            SimpleDateFormat dateFormat=new SimpleDateFormat("MM-dd hh:mm");
            tv_time.setText(dateFormat.format(System.currentTimeMillis()));
            tv_title.setText(title);
            tv_content.setText(content);
            tv_ok.setText(ok);
            tv_cancal.setText(cancal);
            dialog.setCancelable(cancelable);
            dialog.show();
    }

    /**
     * 关闭加载对话框
     */
    public  void hideDialogForLoading() {
        if(dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_ok:
                if (mOnDialogClickLister!=null){
                    mOnDialogClickLister.onOk();
                }
                hideDialogForLoading();
                break;
            case R.id.tv_cancal:
                if (mOnDialogClickLister!=null){
                    mOnDialogClickLister.onCancal();
                }
                hideDialogForLoading();
                break;
        }
    }


    private OnDialogClickLister mOnDialogClickLister;

    public OnDialogClickLister getmOnDialogClickLister() {
        return mOnDialogClickLister;
    }

    public void setmOnDialogClickLister(OnDialogClickLister mOnDialogClickLister) {
        this.mOnDialogClickLister = mOnDialogClickLister;
    }

    public static interface OnDialogClickLister{
        void onOk();
        void onCancal();
    }

}
