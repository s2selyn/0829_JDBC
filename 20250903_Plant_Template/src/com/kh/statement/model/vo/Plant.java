package com.kh.statement.model.vo;

import java.sql.Date;
import java.util.Objects;

public class Plant {

	private int plantNo;
	private String plantName;
	private String plantColor;
	private Date enrollDate;
	
	public Plant() {
		super();
	}
	
	public Plant(String plantName, String plantColor) {
		super();
		this.plantName = plantName;
		this.plantColor = plantColor;
	}
	
	public Plant(int plantNo, String plantName, String plantColor, Date enrollDate) {
		
		super();
		this.plantNo = plantNo;
		this.plantName = plantName;
		this.plantColor = plantColor;
		this.enrollDate = enrollDate;
		
	}
	
	public int getPlantNo() {
		return plantNo;
	}
	
	public void setPlantNo(int plantNo) {
		this.plantNo = plantNo;
	}
	
	public String getPlantName() {
		return plantName;
	}
	
	public void setPlantName(String plantName) {
		this.plantName = plantName;
	}
	
	public String getPlantColor() {
		return plantColor;
	}
	
	public void setPlantColor(String plantColor) {
		this.plantColor = plantColor;
	}
	
	public Date getEnrollDate() {
		return enrollDate;
	}
	
	public void setEnrollDate(Date enrollDate) {
		this.enrollDate = enrollDate;
	}
	
	@Override
	public String toString() {
		return "Plant [plantNo=" + plantNo + ", plantName=" + plantName + ", plantColor=" + plantColor + ", enrollDate="
				+ enrollDate + "]";
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(enrollDate, plantColor, plantName, plantNo);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Plant other = (Plant) obj;
		return Objects.equals(enrollDate, other.enrollDate) && Objects.equals(plantColor, other.plantColor)
				&& Objects.equals(plantName, other.plantName) && plantNo == other.plantNo;
	}
	
}
