package com.sikefeng.tongxuelu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sikefeng.tongxuelu.R;


public class LoadingDialog{

	/** 加载数据对话框 */
	private Dialog dialog;
	private TextView textView;
	private static LoadingDialog loadingDialog;
	public static LoadingDialog getInstance(){
		if (loadingDialog==null) {
			loadingDialog=new LoadingDialog();
		}
		return loadingDialog;

	}

	public  void showDialogForLoading(final Context context, boolean cancelable, String text) {

		View view = LayoutInflater.from(context).inflate(R.layout.loadingdialog_layout, null);
		textView= (TextView)view.findViewById(R.id.id_tv_loading_dialog_text);
		textView.setText(text);
		dialog = new Dialog(context, R.style.loading_dialog_style);
		dialog.setCancelable(cancelable);
		dialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
//		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();

		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface arg0) {
				// TODO Auto-generated method stub
				if (mlistener!=null){
					mlistener.closeDialog();
				}
			}
		});

	}
	public  void setProgress(String progress){
		textView.setText(progress);
	}
	/**
	 * 关闭加载对话框
	 */
	public  void hideDialogForLoading() {
		if(dialog != null && dialog.isShowing()) {
			dialog.cancel();
		}
	}


	private OnDialogClosedListener mlistener;
	//定义对话框关闭时接口
	public interface OnDialogClosedListener{
		void closeDialog();
	}
	public void setOnDialogClosedListener(OnDialogClosedListener listener){
		mlistener=listener;
	}



}
