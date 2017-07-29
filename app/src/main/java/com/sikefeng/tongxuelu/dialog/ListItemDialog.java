package com.sikefeng.tongxuelu.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.sikefeng.tongxuelu.R;

/**
 * Created by sikefeng on 2016/9/27.
 */
public class ListItemDialog extends AlertDialog.Builder{
    public ListItemDialog(Context context) {
        super(context, R.style.loading_dialog_style);
    }

    public ListItemDialog(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, R.style.loading_dialog_style);
    }

    public ListItemDialog(Context context, int themeResId) {
        super(context, R.style.loading_dialog_style);
    }


}
