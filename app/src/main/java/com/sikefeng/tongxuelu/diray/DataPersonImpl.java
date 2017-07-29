package com.sikefeng.tongxuelu.diray;

import android.content.Context;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.sikefeng.tongxuelu.entity.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sikefeng on 2016/8/12.
 */
public class DataPersonImpl {

    private DbUtils dbUtils;
    private Context context;
    private List<Person> teacherList = null;
    private List<Person> classmateList = null;
    private List<Person> friendsList = null;
    public DataPersonImpl(Context context) {
//        dbUtils=DbUtils.create(context, Environment.getExternalStorageDirectory()+"/TongXueLu/Data/","person");
        this.context=context;
        dbUtils= DbUtils.create(context);

    }
    public List<Person> findTeacher() {
        return teacherList;
    }

    public List<Person> findClassmate() {
        return classmateList;
    }

    public List<Person> findFriends() {
        return friendsList;
    }
    //保存单条记录
    public boolean savePerson(Person person){
        try {
            dbUtils.save(person);
            return true;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }
    //批量保存记录
    public boolean savePersonAll(List<Person> persons){
        try {
            dbUtils.saveAll(persons);
            return true;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }
    //修改单条记录
    public boolean updatePerson(Person person){
        try {
            dbUtils.update(person, WhereBuilder.b("id","=",person.getId()));
//            dbUtils.update(person, WhereBuilder.b("id","=",person.getId()),"title","content","saveDate");
//            dbUtils.update(person,WhereBuilder.b("id","=",person.getId()),"title","content","saveDate","textColor",  "background",  "textSize",  "textStyle");
            return true;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }
    //查询所有
    public List<Person> findAllPersons(){
        teacherList = new ArrayList<Person>();
        classmateList = new ArrayList<Person>();
        friendsList = new ArrayList<Person>();
        List<Person> persons=null;
        String flag="";
        try {
            persons=dbUtils.findAll(Selector.from(Person.class));
            if (persons!=null&&persons.size()>0){
                for (int i = 0; i < persons.size(); i++) {
                    Person person=persons.get(i);
                    flag=person.getFlag();
                    if ("teacher".equals(flag)) {
                        teacherList.add(person);
                    } else if ("classmate".equals(flag)) {
                        classmateList.add(person);
                    } else if ("friends".equals(flag)) {
                        friendsList.add(person);
                    }
                }
            }
            return persons;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return persons;
    }

    //查询单条记录
    public Person findPerson(int id){
        Person person=null;
        try {
            person =dbUtils.findFirst(Selector.from(Person.class).where("id","=",id));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return person;
    }
    //删除单条记录
    public boolean deletePerson(int id){
        try {
            dbUtils.delete(Person.class, WhereBuilder.b("id", "=",id));
            return true;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }

    //删除所有记录
    public boolean deletePersonAll(){
        try {
            dbUtils.deleteAll(Person.class);
            return true;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }


}
