package com.sikefeng.tongxuelu.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.sikefeng.tongxuelu.R;
import com.sikefeng.tongxuelu.activity.EditPersonActivity;
import com.sikefeng.tongxuelu.activity.MessageActivity;
import com.sikefeng.tongxuelu.activity.RegisterActivity;
import com.sikefeng.tongxuelu.dialog.CommonDialogView;
import com.sikefeng.tongxuelu.dialog.ListDialog;
import com.sikefeng.tongxuelu.diray.DataPersonImpl;
import com.sikefeng.tongxuelu.entity.Person;
import com.sikefeng.tongxuelu.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DeleteBatchListener;

/**
 * Created by sikefeng on 2016/9/4.
 */
public class MyExpandableAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<String> group_list;
    private List<List<Person>> item_list;
    private int mHideGroupPos = -1;
    private LayoutInflater inflater;
    private DataPersonImpl dataPersonImpl;
    private BitmapUtils bitmapUtils;
    private String imagesPath = Environment.getExternalStorageDirectory() + "/TongXueLu/HeadImages/";
    public MyExpandableAdapter(Context mContext, List<String> group_list,
                               List<List<Person>> item_list) {
        super();
        this.mContext = mContext;
        this.group_list = group_list;
        this.item_list = item_list;
        bitmapUtils = new BitmapUtils(mContext, imagesPath);
        bitmapUtils.configDefaultBitmapMaxSize(100, 100);
        inflater=LayoutInflater.from(mContext);
        dataPersonImpl = new DataPersonImpl(mContext);
    }

    public Object getChild(int groupPosition, int childPosition) {
        return item_list.get(groupPosition).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public int getChildrenCount(int groupPosition) {
        return item_list.get(groupPosition).size();
    }

    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ChileViewHolder vHolderchild = null;
        if (convertView == null) {
            convertView = (View) inflater.inflate(
                    R.layout.list_item_view, null);
            vHolderchild = new ChileViewHolder();
            vHolderchild.imageView = (ImageView) convertView
                    .findViewById(R.id.icon);
            vHolderchild.tv_name = (TextView) convertView
                    .findViewById(R.id.contact_list_item_name);
            vHolderchild.tv_word = (TextView) convertView
                    .findViewById(R.id.cpntact_list_item_state);
            vHolderchild.relativelayout_item = (RelativeLayout) convertView.findViewById(R.id.relativelayout_item);
            convertView.setTag(vHolderchild);
        } else {
            vHolderchild = (ChileViewHolder) convertView.getTag();
        }
        vHolderchild.tv_name.setText(String.valueOf(item_list
                .get(groupPosition).get(childPosition).getName()));
        vHolderchild.tv_word.setText(String.valueOf(item_list
                .get(groupPosition).get(childPosition).getWords()));

        String path = item_list.get(groupPosition).get(childPosition).getTouxiang();
        if (path != null && !TextUtils.isEmpty(path)) {
            bitmapUtils.display(vHolderchild.imageView, String.valueOf(item_list.get(groupPosition).get(childPosition).getTouxiang()));
        } else {
            vHolderchild.imageView.setImageResource(R.drawable.touxiang);
        }
        vHolderchild.relativelayout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Person person = (Person) getChild(groupPosition, childPosition);
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("id", person.getId());
                mContext.startActivity(intent);
            }
        });
        vHolderchild.relativelayout_item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                initListDialog(item_list.get(groupPosition).get(childPosition).getFlag(), item_list.get(groupPosition).get(childPosition).getId());
                return false;
            }
        });

        return convertView;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflaterGroup = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflaterGroup.inflate(R.layout.list_group_view, null);
        }
        TextView title = (TextView) view.findViewById(R.id.group_name);
        TextView tv_counts = (TextView) view.findViewById(R.id.tv_counts);
        tv_counts.setText(getChildrenCount(groupPosition) + "/" + getChildrenCount(groupPosition));
        title.setText(getGroup(groupPosition).toString());
        ImageView image = (ImageView) view.findViewById(R.id.group_indicator);
        if (isExpanded) {
            image.setImageResource(R.drawable.indicator_expanded);
        } else {
            image.setImageResource(R.drawable.indicator_unexpanded);
        }
        return view;
    }

    class GroupViewHolder {
        TextView textView;
        ImageView image;
        TextView tv_counts;
    }

    class ChileViewHolder {
        ImageView imageView;
        TextView tv_name;
        TextView tv_word;
        RelativeLayout relativelayout_item;
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public Object getGroup(int groupPosition) {
        return group_list.get(groupPosition);
    }

    public int getGroupCount() {
        return group_list.size();

    }

    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }

    public void hideGroup(int groupPos) {
        mHideGroupPos = groupPos;
    }
    private void initListDialog(final String flag, final int index) {
        final ListDialog listDialog = new ListDialog(mContext);
        listDialog.show();
        final List<String> list = Arrays.asList(new String[]{"添加联系人", "编辑联系人", "删除联系人"});
        listDialog.initDialog(mContext, list, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        listDialog.cancel();
                        Intent intent1 = new Intent(mContext, RegisterActivity.class);
                        intent1.putExtra("flag", flag);
                        mContext.startActivity(intent1);
                        ToastUtils.showShort(mContext, flag);
                        break;
                    case 1:
                        listDialog.cancel();
                        Intent editiIntent = new Intent(mContext, EditPersonActivity.class);
                        editiIntent.putExtra("id", index);

                        break;
                    case 2:
                        listDialog.cancel();
                        DeleteDialog(index);
                        break;
                }
            }
        });

    }
    //删除联系人
    private void DeleteDialog(final int id){
        CommonDialogView commonDialogView=new CommonDialogView(mContext);
        String time=new SimpleDateFormat("MM-dd hh:mm").format(System.currentTimeMillis());
        commonDialogView.showDialog(mContext,true,null,"提示:",time,"是否删除该联系人?","确定","取消");
        commonDialogView.setmOnDialogClickLister(new CommonDialogView.OnDialogClickLister() {
            @Override
            public void onOk() {
                String url = dataPersonImpl.findPerson(id).getTouxiang();
                if (!TextUtils.isEmpty(url)) {
                    BmobFile.deleteBatch(mContext, new String[]{url}, new DeleteBatchListener() {
                        @Override
                        public void done(String[] strings, BmobException e) {

                        }
                    });
                }
                dataPersonImpl.deletePerson(id);
                reLoad();//重新加载数据
                Toast.makeText(mContext, "成功移除!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancal() {

            }
        });
    }
    //重新加载数据
    private void reLoad() {
        dataPersonImpl.findAllPersons();
        item_list.clear();
        item_list.add(dataPersonImpl.findTeacher());
        item_list.add(dataPersonImpl.findClassmate());
        item_list.add(dataPersonImpl.findFriends());
        notifyDataSetChanged();
    }
}