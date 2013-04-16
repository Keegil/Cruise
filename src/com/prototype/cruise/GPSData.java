package com.prototype.cruise;

public class GPSData {
	private long id;
	private long drivingStatsId;
	private String time;
	private float latitude;
	private float longitude;

	// Empty constructor
	public GPSData() {

	}

	// Constructor
	public GPSData(long id, long drivingStatsId, String time,
			float latitude, float longitude) {
		this.id = id;
		this.drivingStatsId = drivingStatsId;
		this.time = time;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public long getDrivingStatsId() {
		return drivingStatsId;
	}

	public void setDrivingStatsId(long drivingStatsId) {
		this.drivingStatsId = drivingStatsId;
	}
	
	public String getTime() {
		return time;
	}
	
	public void setTime(String time) {
		this.time = time;
	}
	
	public float getLatitude() {
		return latitude;
	}
	
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	
	public float getNumBrakeEvent() {
		return longitude;
	}
	
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
}
