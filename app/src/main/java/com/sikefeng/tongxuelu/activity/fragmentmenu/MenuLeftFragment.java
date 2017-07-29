package com.sikefeng.tongxuelu.activity.fragmentmenu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sikefeng.tongxuelu.R;
import com.sikefeng.tongxuelu.activity.BaiduActivity;
import com.sikefeng.tongxuelu.activity.ShareAppListActivity;
import com.sikefeng.tongxuelu.activity.user.User;
import com.sikefeng.tongxuelu.activity.user.UserMsgActivity;
import com.sikefeng.tongxuelu.bmobutils.BmobUtils;
import com.sikefeng.tongxuelu.dialog.CommonDialogView;
import com.sikefeng.tongxuelu.diray.BookActivity;
import com.sikefeng.tongxuelu.utils.AppUtils;
import com.sikefeng.tongxuelu.utils.NetworkUtils;

import java.io.File;
import java.text.SimpleDateFormat;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

import static android.content.Context.MODE_PRIVATE;

public class MenuLeftFragment extends Fragment implements View.OnClickListener {
    private Activity activity;
    private ProgressDialog progressDialog;
    private User loginUser = null;
    private View view_layout;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        /* * 下载进度条的设置 */
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMax(100);
        progressDialog.setTitle("提示");
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("正在下载，请耐心等候！");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        getLocalUser(view_layout);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view_layout = inflater.inflate(R.layout.left_menu_layout, container, false);
        Button bt_diray = (Button) view_layout.findViewById(R.id.bt_diray);
        Button bt_refresh = (Button) view_layout.findViewById(R.id.bt_refresh);
        Button bt_share = (Button) view_layout.findViewById(R.id.bt_share);
        Button bt_exit = (Button) view_layout.findViewById(R.id.bt_exit);
        Button bt_baidu = (Button) view_layout.findViewById(R.id.bt_baidu);

        bt_share.setOnClickListener(this);
        bt_exit.setOnClickListener(this);
        bt_refresh.setOnClickListener(this);
        bt_baidu.setOnClickListener(this);
        bt_diray.setOnClickListener(this);
        getLocalUser(view_layout);
//        int screenwidth = ScreenUtils.getScreenWidth(getActivity());
//        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,screenwidth);
//        view.setLayoutParams(layoutParams);
        return view_layout;
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.bt_diray:
                startActivity(new Intent(activity, BookActivity.class));
                break;
            case R.id.bt_baidu:
                startActivity(new Intent(activity, BaiduActivity.class));
                break;
            case R.id.bt_share:
                startActivity(new Intent(activity, ShareAppListActivity.class));
                break;
            case R.id.bt_refresh:
                if (NetworkUtils.isNetworkConnected(activity)) {
                    SharedPreferences sp = activity.getSharedPreferences("appmsg", MODE_PRIVATE);
                    int version = sp.getInt("appVersion", 1);
                    String url = sp.getString("appUrl", "");
                    if (version > AppUtils.getCurrentVersion(activity)) {
                        showNoticeDialog(url);
                    } else {
                        Toast.makeText(activity, "当前已是最新版本！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity, "网络未连接", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.bt_exit:
                ExitDialog();
                break;
            default:
                break;
        }
    }
    private void getLocalUser(View view) {

        loginUser= BmobUser.getCurrentUser(activity,User.class);
        TextView tv_username = (TextView) view.findViewById(R.id.tv_username);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        tv_username.setText(loginUser.getTruename()+"");

        BmobFile bmobFile=loginUser.getIcon();
        if (bmobFile!=null) {
            String url=bmobFile.getFileUrl(activity);
            if (url!=null) {
                ImageLoader.getInstance().displayImage(url, imageView);
                imageView.setBackgroundResource(R.drawable.bg_img);
            }
        }else {

        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                startActivity(new Intent(activity,UserMsgActivity.class));
            }
        });
    }
    private void showNoticeDialog(final String url) {
        AlertDialog alertDialog = new AlertDialog.Builder(activity).setTitle("软件更新提示：")
                .setMessage("软件已更新至最新版本，请下载更新！").setPositiveButton("下载", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        downloadFile(url);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    //退出提示对话框
    private void ExitDialog() {
        CommonDialogView commonDialogView = new CommonDialogView(activity);
        String time = new SimpleDateFormat("MM-dd hh:mm").format(System.currentTimeMillis());
        commonDialogView.showDialog(activity, true, null, "提示:", time, "是否退出当前账号?", "确定", "取消");
        commonDialogView.setmOnDialogClickLister(new CommonDialogView.OnDialogClickLister() {
            @Override
            public void onOk() {
                BmobUtils.exitLoginUser(activity);
            }

            @Override
            public void onCancal() {

            }
        });
    }

    public void downloadFile(String downUrl) {
        final String sdcardDir = Environment.getExternalStorageDirectory() + "/TongXueLu/Download/";
        File dir = new File(sdcardDir);
        if (!dir.exists()) {
            dir.mkdir();
        }
        HttpUtils http = new HttpUtils();
        HttpHandler handler = http.download(downUrl,
                sdcardDir + "同学录.apk",
                true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
                true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
                new RequestCallBack<File>() {
                    @Override
                    public void onStart() {
                        progressDialog.show();
                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        progressDialog.setProgress((int) (current * 100 / total));
                        System.out.println("progress=" + current * 100 / total);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<File> responseInfo) {
                        System.out.println("downloaded:" + responseInfo.result.getPath());
                        progressDialog.dismiss();
                        Toast.makeText(activity, "下载完成", Toast.LENGTH_SHORT).show();
                        installApk(responseInfo.result.getPath());
                    }


                    @Override
                    public void onFailure(HttpException error, String msg) {
                        System.out.println("fail msg=" + msg);
                        progressDialog.dismiss();
                        if (msg.contains("maybe the file has downloaded completely")) {
                            installApk(sdcardDir + "同学录.apk");
                        }
                    }
                });
    }

    //更新完成后安装新版app
    private void installApk(String path) {
        File apkfile = new File(path);
        if (!apkfile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        startActivity(intent);
    }
}
