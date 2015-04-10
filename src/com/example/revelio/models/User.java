package com.example.revelio.models;

public class User {
	String net_id;
	String user_name;
	String password;
	String email_id;
	int ph_number;
	
	public String getNet_id() {
		return net_id;
	}
	public void setNet_id(String net_id) {
		this.net_id = net_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail_id() {
		return email_id;
	}
	public void setEmail_id(String email_id) {
		this.email_id = email_id;
	}
	public int getPh_number() {
		return ph_number;
	}
	public void setPh_number(int ph_number) {
		this.ph_number = ph_number;
	}
}
