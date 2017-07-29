package com.sikefeng.tongxuelu.manager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.sikefeng.tongxuelu.R;
import com.sikefeng.tongxuelu.entity.IndexInfo;

import java.util.List;

import cn.bmob.v3.listener.DeleteListener;

/**
 * Created by Administrator on 2016/7/24.
 */
public class BookAdapter extends BaseAdapter {

    private Context context;
    private List<IndexInfo> bookList;
    private LayoutInflater layoutInflater;
    private int index=0;

    public BookAdapter(Context context, List<IndexInfo> bookList) {
        this.context = context;
        this.bookList = bookList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return bookList.size();
    }

    @Override
    public Object getItem(int position) {
        return bookList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder vh = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item, null);
            vh = new ViewHolder();
            vh.tv_title = (TextView) convertView.findViewById(R.id.title_tv);
            vh.tv_account = (TextView) convertView.findViewById(R.id.account_tv);
            vh.tv_passwd = (TextView) convertView.findViewById(R.id.password_tv);
            vh.img_delete = (TextView) convertView.findViewById(R.id.delete_img);
            vh.btn_edit = (TextView) convertView.findViewById(R.id.btn_edit);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.tv_title.setText(bookList.get(position).getContent());
        vh.tv_account.setText(bookList.get(position).getImgUrl());
        vh.tv_passwd.setText(bookList.get(position).getWebsite());
        vh.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,UpdateActivity.class);
                intent.putExtra("book",(IndexInfo)bookList.get(position));
                context.startActivity(intent);
            }
        });

        vh.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index=position;
                deleteDialog();
            }
        });
        return convertView;
    }

    public static class ViewHolder {
        TextView tv_title;
        TextView tv_account;
        TextView tv_passwd;
        TextView img_delete;
        TextView btn_edit;
    }
    public void deleteDialog(){
            new AlertDialog.Builder(context).setTitle("删除提示：").setMessage("是否删除该条记录？")
                    .setPositiveButton("确定",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final IndexInfo book = bookList.get(index);
                            book.delete(context, book.getObjectId().toString(), new DeleteListener() {
                                @Override
                                public void onSuccess() {
                                    bookList.remove(index);
                                    notifyDataSetChanged();
                                    notifyDataSetInvalidated();

                                    Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    Toast.makeText(context, "删除失败" + s, Toast.LENGTH_SHORT).show();
                                }
                            });
                            dialog.dismiss();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
    }

}
