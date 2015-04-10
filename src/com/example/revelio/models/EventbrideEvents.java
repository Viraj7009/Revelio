package com.example.revelio.models;

public class EventbrideEvents {
		String event_id;
		String event_name;
		String event_date = new String();
		String start_time = new String();
		String event_url = new String();
		String event_desc = new String();
		String longitude;
		int building_no;
		public int getBuilding_no() {
			return building_no;
		}
		public void setBuilding_no(int building_no) {
			this.building_no = building_no;
		}
		String latitude;
		
		
		public String getLongitude() {
			return longitude;
		}
		public void setLongitude(String longitude) {
			this.longitude = longitude;
		}
		public String getLatitude() {
			return latitude;
		}
		public void setLatitude(String latitude) {
			this.latitude = latitude;
		}
		public String getEvent_desc() {
			return event_desc;
		}
		public void setEvent_desc(String event_desc) {
			this.event_desc = event_desc;
		}
		public String getEvent_url() {
			return event_url;
		}
		public void setEvent_url(String event_url) {
			this.event_url = event_url;
		}
		public String getEvent_id() {
			return event_id;
		}
		public void setEvent_id(String event_id) {
			this.event_id = event_id;
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

		
		
		
}
