package com.sikefeng.tongxuelu.sortlistview;

public class PhoneUtils {
	private String contactName;
	private String phoneNumber;
	
	public PhoneUtils(String contactName, String phoneNumber) {
		this.contactName = contactName;
		this.phoneNumber = phoneNumber;
	}


	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

}
