package com.CafeTownSend;
/**
 * Class to map json objects from the API response and extract and validate in each create,update and delete calls.
 * @author Kumar_Aarjab
 * @version 1.0
 */
public class Person {
	public int id = 0;
	public String first_name;
	public String last_name;
	public String start_date;
	public String email;
	
	public Person(int id, String first_name, String last_name, String start_date, String email) {
		
		this.id = id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.start_date = start_date;
		this.email = email;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	}
