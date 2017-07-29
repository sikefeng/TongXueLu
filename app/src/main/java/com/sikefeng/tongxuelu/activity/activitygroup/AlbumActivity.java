package com.sikefeng.tongxuelu.activity.activitygroup;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sikefeng.tongxuelu.R;
import com.sikefeng.tongxuelu.activity.LookImageActivity;
import com.sikefeng.tongxuelu.adapter.ImagesAdapter;
import com.sikefeng.tongxuelu.baseactivity.BaseActivity;
import com.sikefeng.tongxuelu.dialog.LoadingDialog;
import com.sikefeng.tongxuelu.multipleimages.ChildAdapter;
import com.sikefeng.tongxuelu.multipleimages.startSelectActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DeleteBatchListener;
import cn.bmob.v3.listener.UploadFileListener;

public class AlbumActivity extends BaseActivity implements View.OnClickListener{
    private GridView mGridView;
    private ImagesAdapter adapter;
    private List<String> pathList;
    private PopupWindow popupWindow;
    private ImageButton imageButton;
    private TextView tv_title;
    private LoadingDialog loadingDialog;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_album);
        this.key=4;
        setEnterSwichLayout();
        mGridView = (GridView) findViewById(R.id.child_grid);
        if (pathList!=null) {
            System.out.println("----"+pathList.size());
            adapter = new ImagesAdapter(this, pathList, mGridView);
            mGridView.setAdapter(adapter);
            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    // TODO Auto-generated method stub
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("path", (ArrayList<String>) pathList);
                    bundle.putInt("position", position);
                    Intent intent = new Intent(AlbumActivity.this,LookImageActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

        imageButton = (ImageButton) findViewById(R.id.settings);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                openPopupWindow(view);
            }
        });
        tv_title = (TextView) findViewById(R.id.tv_title);

        tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                 finish();
            }
        });
        loadingDialog=LoadingDialog.getInstance();
    }// *********************************************************

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 111) {
            pathList = ChildAdapter.getArrayList();
            adapter = new ImagesAdapter(this, pathList, mGridView);
            mGridView.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.addimage:
                popupWindow.dismiss();
                Intent intent = new Intent(AlbumActivity.this,startSelectActivity.class);
                startActivityForResult(intent, 111);
                break;
        }

    }

    private void openPopupWindow(View v) {

        View view = getLayoutInflater().inflate(R.layout.popupwindow_addimage, null);

        popupWindow = new PopupWindow(view, ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.update();// ˢ��״̬

        popupWindow.setBackgroundDrawable(new ColorDrawable(0000000000));
        popupWindow.showAsDropDown(v);

        Button addimage = (Button) view.findViewById(R.id.addimage);
        addimage.setOnClickListener(this);

    }
//
//    public List<String> GetAllImages() {
//        List<String> list = null;
//        Set<String> set=null;
//        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
//        Cursor cursor = db.query("PHOTO", null, null, null, null, null, null);
//        if (cursor != null) {
//            list = new ArrayList<String>();
//            set=new HashSet<String>();
//            while (cursor.moveToNext()) {
//                String path = cursor.getString(cursor.getColumnIndex("PATH"));
//                if (!TextUtils.isEmpty(path)&&path!=null&&new File(path).exists()){
//                    set.add(path);
//                }else {//图片文件不存在
////                    System.out.println("-------------------------------"+path);
//                }
//
//            }
//        }
//        cursor.close();
//        Iterator<String> iterator=set.iterator();
//        while(iterator.hasNext()){
//            list.add(iterator.next());
//        }
//
//        return list;
//    }

    public void uploadFile(String picPath) {


        File file = new File(picPath);
        if (file.exists()) {
            final BmobFile bmobFile = new BmobFile(file);
            bmobFile.uploadblock(AlbumActivity.this, new UploadFileListener() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onProgress(Integer value) {
                    // 返回的上传进度（百分比）
                    //显示加载对话框加载进度
                    loadingDialog.setProgress(String.valueOf(value)+"%");

                }

                @Override
                public void onFailure(int code, String msg) {
//					toast("上传文件失败：" + msg);
                    loadingDialog.hideDialogForLoading();
                    ContactsActivity.isReLoad=true;
                    setResult(100);
                    setExitSwichLayout();
                }
            });
        } else {
            loadingDialog.hideDialogForLoading();
        }

    }

    public void deletefile(String url) {
        //此url必须是上传文件成功之后通过bmobFile.getUrl()方法获取的。
        String[] urls = new String[]{url};
        BmobFile.deleteBatch(AlbumActivity.this, urls, new DeleteBatchListener() {
            @Override
            public void done(String[] strings, BmobException e) {
                if (e == null) {
//					toast("全部删除成功");
                } else {
                    if (strings != null) {
//						toast("删除失败个数：" + strings.length + "," + e.toString());
                    } else {
//						toast("全部文件删除失败：" + e.getErrorCode() + "," + e.toString());
                    }
                }
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            setExitSwichLayout();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}// *****************************
