package com.example.revelio.models;

import java.util.Date;

public class Event {
	int event_id, building_no,rsvp;
	String event_name;
	String event_date = new String();
	String start_time = new String();
	String end_time = new String();
	String event_desc = new String();
	
	public String getEvent_desc() {
		return event_desc;
	}
	public void setEvent_desc(String event_desc) {
		this.event_desc = event_desc;
	}
	public int getEvent_id() {
		return event_id;
	}
	public void setEvent_id(int event_id) {
		this.event_id = event_id;
	}
	public int getBuilding_no() {
		return building_no;
	}
	public void setBuilding_no(int building_no) {
		this.building_no = building_no;
	}
	public int getRsvp() {
		return rsvp;
	}
	public void setRsvp(int rsvp) {
		this.rsvp = rsvp;
	}
	public String getEvent_name() {
		return event_name;
	}
	public void setEvent_name(String event_name) {
		this.event_name = event_name;
	}
	public String getEvent_date() {
		return event_date;
	}
	public void setEvent_date(String event_date) {
		this.event_date = event_date;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	
	
	
}
