package com.sikefeng.tongxuelu.diray;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sikefeng.tongxuelu.R;

import java.util.List;


/**
 * Created by Administrator on 2016/6/24.
 */
public class BookAdapter extends BaseAdapter {

    private Context mcontext;
    private List<Book> books;
    private LayoutInflater inflater;

    public BookAdapter(Context mcontext, List<Book> books) {
        this.mcontext = mcontext;
        this.books = books;
        inflater=LayoutInflater.from(mcontext);
    }

    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public Object getItem(int position) {
        return books.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null){
            convertView=inflater.inflate(R.layout.book_item_layout,null);
            holder=new ViewHolder();
            holder.title=(TextView)convertView.findViewById(R.id.bookname);
            holder.content=(TextView)convertView.findViewById(R.id.bookcontent);
            holder.time=(TextView)convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.title.setText(books.get(position).getTitle());
        holder.content.setText(books.get(position).getContent());
        holder.time.setText(books.get(position).getSaveDate());
        return convertView;
    }
    static class ViewHolder{
         TextView title;
         TextView content;
        TextView time;
    }
}
