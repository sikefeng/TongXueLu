package com.sikefeng.tongxuelu.diray;

import android.content.Context;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

import java.util.List;

/**
 * Created by Administrator on 2016/6/24.
 */
public class DataImpl {


    private DbUtils dbUtils;
    private Context context;
    public DataImpl(Context context) {
//        dbUtils=DbUtils.create(context, Environment.getExternalStorageDirectory()+"/TongXueLu/Data/","book");
        this.context=context;
        dbUtils= DbUtils.create(context);
    }
    //保存单条记录
    public boolean saveBook(Book book){
        try {
            dbUtils.save(book);
            return true;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }
    //批量保存记录
    public boolean saveBookAll(List<Book> books){
        try {
            dbUtils.saveAll(books);
            return true;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }
    //修改单条记录
    public boolean updateBook(Book book){
        try {
            dbUtils.update(book, WhereBuilder.b("id","=",book.getId()),"title","content","saveDate");
//            dbUtils.update(book,WhereBuilder.b("id","=",book.getId()),"title","content","saveDate","textColor",  "background",  "textSize",  "textStyle");
            return true;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }
    //查询所有
    public List<Book> findAllBooks(){
        List<Book> books=null;
        try {
            books=dbUtils.findAll(Selector.from(Book.class));
            return books;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return books;
    }
    //查询单条记录
    public Book findBook(int id){
        Book book=null;
        try {
          book =dbUtils.findFirst(Selector.from(Book.class).where("id","=",id));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return book;
    }
    //删除单条记录
    public boolean deleteBook(int id){
        try {
            dbUtils.delete(Book.class, WhereBuilder.b("id", "=",id));
            return true;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }

    //删除所有记录
    public boolean deleteBookAll(){
        try {
            dbUtils.deleteAll(Book.class);
            return true;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }
}
