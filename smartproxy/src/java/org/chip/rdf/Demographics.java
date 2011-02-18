package org.chip.rdf;

public class Demographics {
	
	public Demographics(String birthDateTime, String givenName, String familyName, String gender, String zipcode){
		this.birthDateTime=birthDateTime;
		this.givenName=givenName;
		this.familyName=familyName;
		this.gender=gender;
		this.zipcode=zipcode;
	}
	
	public String getBirthDateTime() {
		return birthDateTime;
	}
	public void setBirthDateTime(String birthDateTime) {
		this.birthDateTime = birthDateTime;
	}
	public String getGivenName() {
		return givenName;
	}
	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}
	public String getFamilyName() {
		return familyName;
	}
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	private String birthDateTime;
	private String givenName;
	private String familyName;
	private String gender;
	private String zipcode;
}
