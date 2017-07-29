package com.sikefeng.tongxuelu.entity;

import java.io.Serializable;

public class Person implements Serializable {
	
	
	private int id;
	private String flag;
	private String subject;
	private String name;

	private String dream;
	private String age;
	private String words; 
	private String sex;
	private String phonoNumber;
	private String address;
	private String hoddy;
	private String QQ;
	private String e_mail;
	private String constellation;
	private String birthday;
	private String booldType;
	private String nickname;
	private String best_Color;
	private String best_sports;
	private String best_star;

	public String getTouxiang() {
		return touxiang;
	}

	public void setTouxiang(String touxiang) {
		this.touxiang = touxiang;
	}

	private String touxiang;
	
	private String images1;
	private String images2;
	private String images3;
	private String images4;
	private String images5;
	private String images6;
	private String images7;
	private String images8;
	private String images9;




	public String getImages1() {
		return images1;
	}

	public void setImages1(String images1) {
		this.images1 = images1;
	}

	public String getImages2() {
		return images2;
	}

	public void setImages2(String images2) {
		this.images2 = images2;
	}

	public String getImages3() {
		return images3;
	}

	public void setImages3(String images3) {
		this.images3 = images3;
	}

	public String getImages4() {
		return images4;
	}

	public void setImages4(String images4) {
		this.images4 = images4;
	}

	public String getImages5() {
		return images5;
	}

	public void setImages5(String images5) {
		this.images5 = images5;
	}

	public String getImages6() {
		return images6;
	}

	public void setImages6(String images6) {
		this.images6 = images6;
	}

	public String getImages7() {
		return images7;
	}

	public void setImages7(String images7) {
		this.images7 = images7;
	}

	public String getImages8() {
		return images8;
	}

	public void setImages8(String images8) {
		this.images8 = images8;
	}

	public String getImages9() {
		return images9;
	}

	public void setImages9(String images9) {
		this.images9 = images9;
	}

	

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	

	public String getDream() {
		return dream;
	}


	//一定要有无参构造方法************************************
	public Person(){

	}

public Person(int id, String name){
	this.id=id;
	this.name=name;
}
	public Person(int id, String name,String age){
		this.id=id;
		this.name=name;
		this.age=age;
	}
	public Person(String flag, String name, String age, String words,
				  String sex, String phonoNumber, String address, String hoddy,
				  String qQ, String e_mail, String constellation, String birthday,
				  String booldType, String nickname, String dream, String best_Color,
				  String best_sports, String best_star, String touxiang, String images1,
				  String images2, String images3, String images4, String images5,
				  String images6, String images7, String images8, String images9) {

		this.flag = flag;
		this.name = name;
		this.touxiang=touxiang;
		this.age = age;
		this.words = words;
		this.sex = sex;
		this.phonoNumber = phonoNumber;
		this.address = address;
		this.hoddy = hoddy;
		QQ = qQ;
		this.e_mail = e_mail;
		this.constellation = constellation;
		this.birthday = birthday;
		this.booldType = booldType;
		this.nickname = nickname;
		this.dream = dream;
		this.best_Color = best_Color;
		this.best_sports = best_sports;
		this.best_star = best_star;
		this.images1 = images1;
		this.images2 = images2;
		this.images3 = images3;
		this.images4 = images4;
		this.images5 = images5;
		this.images6 = images6;
		this.images7 = images7;
		this.images8 = images8;
		this.images9 = images9;
	}

	public Person(String flag, String subject, String name, String age,
				  String words, String sex, String phonoNumber, String address,
				  String hoddy, String qQ, String e_mail, String constellation,
				  String birthday, String booldType, String nickname, String dream,
				  String best_Color, String best_sports, String best_star, String touxiang,
				  String images1, String images2, String images3, String images4,
				  String images5, String images6, String images7, String images8,
				  String images9) {

		this.subject = subject;
		this.flag = flag;
		this.name = name;
		this.age = age;
		this.words = words;
		this.sex = sex;
		this.phonoNumber = phonoNumber;
		this.address = address;
		this.hoddy = hoddy;
		this.QQ = qQ;
		this.e_mail = e_mail;
		this.constellation = constellation;
		this.birthday = birthday;
		this.booldType = booldType;
		this.nickname = nickname;
		this.dream = dream;
		this.best_Color = best_Color;
		this.best_sports = best_sports;
		this.best_star = best_star;
		this.touxiang=touxiang;
		this.images1 = images1;
		this.images2 = images2;
		this.images3 = images3;
		this.images4 = images4;
		this.images5 = images5;
		this.images6 = images6;
		this.images7 = images7;
		this.images8 = images8;
		this.images9 = images9;
	}

	public Person(int id, String flag, String subject, String name, String age,
				  String words, String sex, String phonoNumber, String address,
				  String hoddy, String qQ, String e_mail, String constellation,
				  String birthday, String booldType, String nickname, String dream,
				  String best_Color, String best_sports, String best_star, String touxiang,
				  String images1, String images2, String images3, String images4,
				  String images5, String images6, String images7, String images8,
				  String images9) {
		this.id=id;
		this.subject = subject;
		this.flag = flag;
		this.name = name;
		this.age = age;
		this.words = words;
		this.sex = sex;
		this.phonoNumber = phonoNumber;
		this.address = address;
		this.hoddy = hoddy;
		this.QQ = qQ;
		this.e_mail = e_mail;
		this.constellation = constellation;
		this.birthday = birthday;
		this.booldType = booldType;
		this.nickname = nickname;
		this.dream = dream;
		this.best_Color = best_Color;
		this.best_sports = best_sports;
		this.best_star = best_star;
		this.touxiang=touxiang;
		this.images1 = images1;
		this.images2 = images2;
		this.images3 = images3;
		this.images4 = images4;
		this.images5 = images5;
		this.images6 = images6;
		this.images7 = images7;
		this.images8 = images8;
		this.images9 = images9;
	}
	public Person(String name, String age, String words, String sex,
				  String phonoNumber, String address, String hoddy, String qQ,
				  String e_mail, String constellation, String birthday,
				  String booldType, String nickname, String dream, String best_Color,
				  String best_sports, String best_star) {


		this.name = name;
		this.age = age;
		this.words = words;
		this.sex = sex;
		this.phonoNumber = phonoNumber;
		this.address = address;
		this.hoddy = hoddy;
		QQ = qQ;
		this.e_mail = e_mail;
		this.constellation = constellation;
		this.birthday = birthday;
		this.booldType = booldType;
		this.nickname = nickname;
		this.dream = dream;
		this.best_Color = best_Color;
		this.best_sports = best_sports;
		this.best_star = best_star;
	}
	public Person(int id, String name, String age, String words, String sex,
				  String phonoNumber, String address, String hoddy, String qQ,
				  String e_mail, String constellation, String birthday,
				  String booldType, String nickname, String dream, String best_Color,
				  String best_sports, String best_star) {

		this.id=id;
		this.name = name;
		this.age = age;
		this.words = words;
		this.sex = sex;
		this.phonoNumber = phonoNumber;
		this.address = address;
		this.hoddy = hoddy;
		this.QQ = qQ;
		this.e_mail = e_mail;
		this.constellation = constellation;
		this.birthday = birthday;
		this.booldType = booldType;
		this.nickname = nickname;
		this.dream = dream;
		this.best_Color = best_Color;
		this.best_sports = best_sports;
		this.best_star = best_star;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public void setDream(String dream) {
		this.dream = dream;
	}

	public String getBest_sports() {
		return best_sports;
	}

	public void setBest_sports(String best_sports) {
		this.best_sports = best_sports;
	}

	public String getBest_star() {
		return best_star;
	}

	public void setBest_star(String best_star) {
		this.best_star = best_star;
	}

	

	

	public Person(String name, String age, String words, String sex,
				  String phonoNumber, String address, String hoddy, String QQ,
				  String e_mail, String constellation, String birthday,
				  String booldType, String nickname) {

		this.name = name;
		this.age = age;
		this.words = words;
		this.sex = sex;
		this.phonoNumber = phonoNumber;
		this.address = address;
		this.hoddy = hoddy;
		this.QQ = QQ;
		this.e_mail = e_mail;
		this.constellation = constellation;
		this.birthday = birthday;
		this.booldType = booldType;
		this.nickname = nickname;
	}

	public Person(String name, String age, String sex, String phonoNumber,
				  String address) {
		this.name = name;
		this.age = age;
		this.sex = sex;
		this.phonoNumber = phonoNumber;
		this.address = address;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBest_Color() {
		return best_Color;
	}

	public void setBest_Color(String best_Color) {
		this.best_Color = best_Color;
	}

	public String getHoddy() {
		return hoddy;
	}

	public void setHoddy(String hoddy) {
		this.hoddy = hoddy;
	}

	public String getQQ() {
		return QQ;
	}

	public void setQQ(String qQ) {
		this.QQ = qQ;
	}

	public String getE_mail() {
		return e_mail;
	}

	public void setE_mail(String e_mail) {
		this.e_mail = e_mail;
	}

	public String getConstellation() {
		return constellation;
	}

	public void setConstellation(String constellation) {
		this.constellation = constellation;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getBooldType() {
		return booldType;
	}

	public void setBooldType(String booldType) {
		this.booldType = booldType;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getWords() {
		return words;
	}

	public void setWords(String words) {
		this.words = words;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPhonoNumber() {
		return phonoNumber;
	}

	public void setPhonoNumber(String phonoNumber) {
		this.phonoNumber = phonoNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", flag=" + flag + ", subject=" + subject
				+ ", dream=" + dream + ", name=" + name + ", age=" + age
				+ ", words=" + words + ", sex=" + sex + ", phonoNumber="
				+ phonoNumber + ", address=" + address + ", hoddy=" + hoddy
				+ ", QQ=" + QQ + ", e_mail=" + e_mail + ", constellation="
				+ constellation + ", birthday=" + birthday + ", booldType="
				+ booldType + ", nickname=" + nickname + ", best_Color="
				+ best_Color + ", best_sports=" + best_sports + ", best_star="
				+ best_star + ", images1=" + images1 + ", images2=" + images2
				+ ", images3=" + images3 + ", images4=" + images4
				+ ", images5=" + images5 + ", images6=" + images6
				+ ", images7=" + images7 + ", images8=" + images8
				+ ", images9=" + images9 + "]";
	}

	

	

}
