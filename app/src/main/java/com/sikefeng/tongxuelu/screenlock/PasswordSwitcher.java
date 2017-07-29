package com.sikefeng.tongxuelu.screenlock;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.sikefeng.tongxuelu.R;


public class PasswordSwitcher extends Activity {
    private ToggleButton toggleButton;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.psswdswitcher);
        toggleButton = (ToggleButton) findViewById(R.id.tb1);
        sp = PasswordSwitcher.this.getSharedPreferences("lock", MODE_PRIVATE);
        boolean isLocked = sp.getBoolean("isLock", false);
        String psswd = sp.getString("password", "");
        if (isLocked && psswd != null) {
            toggleButton.setChecked(true);
        } else {
            toggleButton.setChecked(false);
        }
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    Intent intent = new Intent(getApplicationContext(), Welcome.class);
                    startActivity(intent);
                    PasswordSwitcher.this.finish();
                } else {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("isLock", false);
                    editor.putString("password", "");
                    editor.commit();
                }
            }
        });

    }
}


