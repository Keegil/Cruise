package com.prototype.cruise;


public class Stats {
	private long id;
	private int driveLength;
	private String date;
	private int accMistakes;
	private int speedMistakes;
	private int point;
	private int remainingRange;

	// Empty constructor
	public Stats() {

	}

	// Constructor
	public Stats(long id, String date, int driveLength, int accMistakes, int speedMistakes, int point, int remainingRange) {
		this.id = id;
		this.driveLength = driveLength;
		this.date = date;
		this.accMistakes = accMistakes;
		this.speedMistakes = speedMistakes;
		this.point = point;
		this.remainingRange = remainingRange;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getDriveLength() {
		return driveLength;
	}

	public void setDriveLength(int driveLength) {
		this.driveLength = driveLength;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int accMistakes() {
		return accMistakes;
	}

	public void setAccMistakes(int accMistakes) {
		this.accMistakes = accMistakes;
	}

	public int getSpeedMistakes() {
		return speedMistakes;
	}

	public void setSpeedMistakes(int speedMistakes) {
		this.speedMistakes = speedMistakes;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public int getRemainingRange() {
		return remainingRange;
	}

	public void setRemainingRange(int remainingRange) {
		this.remainingRange = remainingRange;
	}
	
	public String toString(){
		return date;
	}
	
	
	
}