package com.prototype.cruise;

public class EventData {
	private long id;
	private long drivingStatsId;
	private String date;
	private float latitude;
	private float longitude;
	private int accEvent;
	private int speedEvent;
	private int brakeEvent;

	// Empty constructor
	public EventData() {

	}

	// Constructor
	public EventData(long id, long drivingStatsId, String date, float latitude,
			float longitude, int accEvent, int speedEvent, int brakeEvent) {
		this.id = id;
		this.drivingStatsId = drivingStatsId;
		this.date = date;
		this.latitude = latitude;
		this.longitude = longitude;
		this.accEvent = accEvent;
		this.speedEvent = speedEvent;
		this.brakeEvent = brakeEvent;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public int getAccEvent() {
		return accEvent;
	}

	public void setAccEvent(int accEvent) {
		this.accEvent = accEvent;
	}

	public int getSpeedEvent() {
		return speedEvent;
	}

	public void setSpeedEvent(int speedEvent) {
		this.speedEvent = speedEvent;
	}

	public int getBrakeEvent() {
		return brakeEvent;
	}

	public void setBrakeEvent(int brakeEvent) {
		this.brakeEvent = brakeEvent;
	}

	public String toString() {
		return date;
	}

}
