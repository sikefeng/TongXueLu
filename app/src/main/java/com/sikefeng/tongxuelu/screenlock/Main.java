package com.sikefeng.tongxuelu.screenlock;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class Main extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getSupportFragmentManager()
				.beginTransaction()
				.replace(android.R.id.content, PasswordFragment.newInstance(PasswordFragment.TYPE_SETTING))
				.commit();

	}

}
