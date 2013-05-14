package com.prototype.cruise;

public class DrivingStats {
	private long id;
	private String date;
	private int numAccEvent;
	private int numSpeedEvent;
	private int numBrakeEvent;
	private int numRouteEvent;
	private int driveDistance;
	private int rangeStart;
	private int rangeUsed;
	private int rangeEnd;
	private double rangeModifier;
	private double fuelSavings;

	// Empty constructor
	public DrivingStats() {

	}

	// Constructor
	public DrivingStats(long id, String date, int numAccEvent,
			int numSpeedEvent, int numBrakeEvent, int numRouteEvent, int driveDistance,
			int rangeStart, int rangeUsed, int rangeEnd, double rangeModifier,
			double fuelSavings) {
		this.id = id;
		this.date = date;
		this.numAccEvent = numAccEvent;
		this.numSpeedEvent = numSpeedEvent;
		this.numBrakeEvent = numBrakeEvent;
		this.numRouteEvent = numRouteEvent;
		this.driveDistance = driveDistance;
		this.rangeStart = rangeStart;
		this.rangeUsed = rangeUsed;
		this.rangeEnd = rangeEnd;
		this.rangeModifier = rangeModifier;
		this.fuelSavings = fuelSavings;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public int getNumAccEvent() {
		return numAccEvent;
	}
	
	public void setNumAccEvent(int numAccEvent) {
		this.numAccEvent = numAccEvent;
	}
	
	public int getNumSpeedEvent() {
		return numSpeedEvent;
	}
	
	public void setNumSpeedEvent(int numSpeedEvent) {
		this.numSpeedEvent = numSpeedEvent;
	}
	
	public int getNumBrakeEvent() {
		return numBrakeEvent;
	}
	
	public void setNumBrakeEvent(int numBrakeEvent) {
		this.numBrakeEvent = numBrakeEvent;
	}
	
	public int getNumRouteEvent() {
		return numRouteEvent;
	}
	
	public void setNumRouteEvent(int numRouteEvent) {
		this.numRouteEvent = numRouteEvent;
	}

	public int getDriveDistance() {
		return driveDistance;
	}

	public void setDriveDistance(int driveDistance) {
		this.driveDistance = driveDistance;
	}

	public int getRangeStart() {
		return rangeStart;
	}

	public void setRangeStart(int rangeStart) {
		this.rangeStart = rangeStart;
	}

	public int getRangeUsed() {
		return rangeUsed;
	}

	public void setRangeUsed(int rangeUsed) {
		this.rangeUsed = rangeUsed;
	}

	public int getRangeEnd() {
		return rangeEnd;
	}

	public void setRangeEnd(int rangeEnd) {
		this.rangeEnd = rangeEnd;
	}

	public double getRangeModifier() {
		return rangeModifier;
	}

	public void setRangeModifier(double rangeModifier) {
		this.rangeModifier = rangeModifier;
	}
	
	public double getFuelSavings() {
		return fuelSavings;
	}

	public void setFuelSavings(double fuelSavings) {
		this.fuelSavings = fuelSavings;
	}

	public String toString() {
		return date;
	}

}