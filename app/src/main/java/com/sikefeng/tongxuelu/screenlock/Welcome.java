package com.sikefeng.tongxuelu.screenlock;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;


public class Welcome extends FragmentActivity {

	@Override
	protected void onCreate(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onCreate(bundle);
		SharedPreferences sp=Welcome.this.getSharedPreferences("lock",MODE_PRIVATE);
		boolean isLocked=sp.getBoolean("isLock",false);
		String psswd=sp.getString("password","");
		if (isLocked && psswd!=null&&!TextUtils.isEmpty(psswd)) {//密码解锁
			getSupportFragmentManager().beginTransaction().replace(android.R.id.content, PasswordFragment.newInstance(PasswordFragment.TYPE_CHECK)).commit();
		}else{//没有设置密码
			startActivity(new Intent(Welcome.this,Main.class));
			finish();
		}

	}
	

	
}
