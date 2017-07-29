package com.sikefeng.tongxuelu.activity.user;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;


public class User extends BmobUser {
    private BmobFile icon;
    private String sex;
    private String age;
    private BmobFile backBmobFile;
	private String birthday;
	private String xingzuo;
	private String truename;
	private String QQ;
	private String work;
	private String company;
	private String school;
	private String jsonString;
	private String dirayJsonString;
	private String personJsonData;
	private String LockPassword;
	private String phoneno;
	private String colletions;


	public User() {
	}

	public User(BmobFile icon, String sex, String age, BmobFile backBmobFile, String birthday, String xingzuo, String QQ, String work, String company, String school, String jsonString, String dirayJsonString, String personJsonData, String lockPassword) {
		this.icon = icon;
		this.sex = sex;
		this.age = age;
		this.backBmobFile = backBmobFile;
		this.birthday = birthday;
		this.xingzuo = xingzuo;
		this.QQ = QQ;
		this.work = work;
		this.company = company;
		this.school = school;
		this.jsonString = jsonString;
		this.dirayJsonString = dirayJsonString;
		this.personJsonData = personJsonData;
		LockPassword = lockPassword;
	}

	public String getColletions() {
		return colletions;
	}

	public void setColletions(String colletions) {
		this.colletions = colletions;
	}

	public String getPhoneno() {
		return phoneno;
	}

	public void setPhoneno(String phoneno) {
		this.phoneno = phoneno;
	}

	public String getTruename() {
		return truename;
	}

	public void setTruename(String truename) {
		this.truename = truename;
	}
	public String getLockPassword() {
		return LockPassword;
	}

	public void setLockPassword(String lockPassword) {
		LockPassword = lockPassword;
	}

	public String getPersonJsonData() {
		return personJsonData;
	}

	public void setPersonJsonData(String personJsonData) {
		this.personJsonData = personJsonData;
	}

	public String getDirayJsonString() {
		return dirayJsonString;
	}

	public void setDirayJsonString(String dirayJsonString) {
		this.dirayJsonString = dirayJsonString;
	}

	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getXingzuo() {
		return xingzuo;
	}

	public void setXingzuo(String xingzuo) {
		this.xingzuo = xingzuo;
	}

	public String getQQ() {
		return QQ;
	}

	public void setQQ(String QQ) {
		this.QQ = QQ;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}



	public BmobFile getBackBmobFile() {
		return backBmobFile;
	}

	public void setBackBmobFile(BmobFile backBmobFile) {
		this.backBmobFile = backBmobFile;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	

    public BmobFile getIcon() {
        return icon;
    }

    public void setIcon(BmobFile icon) {
        this.icon = icon;
    }

	@Override
	public String toString() {
		return "User{" +
				"icon=" + icon +
				", sex='" + sex + '\'' +
				", age='" + age + '\'' +
				", backBmobFile=" + backBmobFile +
				", birthday='" + birthday + '\'' +
				", xingzuo='" + xingzuo + '\'' +
				", truename='" + truename + '\'' +
				", QQ='" + QQ + '\'' +
				", work='" + work + '\'' +
				", company='" + company + '\'' +
				", school='" + school + '\'' +
				", jsonString='" + jsonString + '\'' +
				", dirayJsonString='" + dirayJsonString + '\'' +
				", personJsonData='" + personJsonData + '\'' +
				", LockPassword='" + LockPassword + '\'' +
				'}';
	}
}
