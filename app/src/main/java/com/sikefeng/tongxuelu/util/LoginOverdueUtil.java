package com.sikefeng.tongxuelu.util;//package com.tongxuelu.util;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.DialogInterface.OnKeyListener;
//import android.content.Intent;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//
//import com.xxxxtech.yaochufang.DefaultConfig;
//import com.xxxxtech.yaochufang.R;
//import com.xxxxtech.yaochufang.api.UserAPI;
//import com.xxxxtech.yaochufang.entity.Bean;
//import com.xxxxtech.yaochufang.function.dialog.CommonPromptDialog;
//import com.xxxxtech.yaochufang.function.patient.PrescriptionMainActivity;
//
//import java.util.ArrayList;
//
///**
// * @ClassName: LoginOverdueUtil
// * @Description: TODO(登录过期提示框)
// * @Author: JOE
// * @Date: 2015-11-5 上午12:22:03
// */
//
//public class LoginOverdueUtil {
//
//	private Context context;
//	private CommonPromptDialog dialog;
//	private UserAPI api;
//	public LoginOverdueUtil(Context mcontext,CommonPromptDialog mdialog){
//		this.context = mcontext;
//		this.dialog = mdialog;
//		api = new UserAPI();
//		showLoginOverdueDialog();
//	}
//	/**
//	 * @Title: showLoginOverdueDialog
//	 * @Description: TODO(登录过期提示框)
//	 */
//	public void showLoginOverdueDialog() {
//		api.PostDataExit(context,new ArrayList<Bean>(), DefaultConfig.INTERFACE_USER_EXIT);
//		new ExitUtil(context,Integer.valueOf(SPUtils.get(context, DefaultConfig.UTYPE, DefaultConfig.UTYPE_PATIENT+"")+""),false).clearData();
//		if (dialog.isShowing()) {
//			dialog.dismiss();
//		}
//		dialog.addListener(dialogListener);
//		try {
//			new Thread().sleep(3000);
//			dialog.show();
//			dialog.setTitle(context.getResources().getString(R.string.prompt));
//			dialog.setContent(context.getResources().getString(R.string.login_overdue));
//			dialog.setBtnText(context.getResources().getString(R.string.know));
//			dialog.setOnKeyListener(keylistener);
//			dialog.setCancelable(false);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//
//	public OnClickListener dialogListener = new OnClickListener() {
//		@Override
//		public void onClick(View v) {
//			dialog.dismiss();
//			Intent intent = new Intent(context,PrescriptionMainActivity.class);
//			context.startActivity(intent);
//			try {
//				((Activity)context).finish();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	};
//
//	OnKeyListener keylistener = new OnKeyListener(){
//        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//				return true;
//			} else {
//				return false;
//			}
//        }
//
//    } ;
//
//}
