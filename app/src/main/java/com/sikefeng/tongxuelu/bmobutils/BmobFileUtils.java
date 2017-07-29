package com.sikefeng.tongxuelu.bmobutils;

import android.content.Context;
import android.widget.Toast;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DeleteBatchListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by Administrator on 2016/6/8.
 */
public class BmobFileUtils {
    private Context mcontext;
    public BmobFileUtils(Context context) {
        mcontext = context;
    }
    public void uploadFile(String picPath) {
        File file = new File(picPath);
        if (file.exists()) {
            final BmobFile bmobFile = new BmobFile(file);
            bmobFile.uploadblock(mcontext, new UploadFileListener() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onProgress(Integer value) {
                    // 返回的上传进度（百分比）
                }

                @Override
                public void onFailure(int code, String msg) {
                    toast("上传文件失败：" + msg);
                }
            });
        } else {
            toast("文件不存在");
        }

    }



    public  void deletefile(String url) {
        //此url必须是上传文件成功之后通过bmobFile.getUrl()方法获取的。
        String[] urls = new String[]{url};
        BmobFile.deleteBatch(mcontext, urls, new DeleteBatchListener() {
            @Override
            public void done(String[] strings, BmobException e) {
                if (e == null) {
                    //toast("全部删除成功");
                } else {
                    if (strings != null) {
                        //toast("删除失败个数：" + strings.length + "," + e.toString());
                    } else {
                        //toast("全部文件删除失败：" + e.getErrorCode() + "," + e.toString());
                    }
                }
            }
        });
    }

    private void toast(String msg) {
        Toast.makeText(mcontext, msg, Toast.LENGTH_SHORT).show();
    }
}
