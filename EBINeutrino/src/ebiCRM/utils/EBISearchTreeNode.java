package ebiCRM.utils;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;

public class EBISearchTreeNode extends DefaultMutableTreeTableNode {

	private String IntNr;
	private String name;
	private String Street;
	private String Zip;
	private String Location;
	private String Country;
	private String Category;
	private String Cooperation;
	private String Qualification;
	private String isLock;
	private Integer numValue;
	private String companyID;

	public EBISearchTreeNode(final String IntNr, final String name, final String Street, final String Zip,
			final String Location, final String Country, final String Category, final String Cooperation,
			final String Qualification, final String isLock, final String companyID) {
		this.IntNr = IntNr;
		this.name = name;
		this.Street = Street;
		this.Zip = Zip;
		this.Location = Location;
		this.Country = Country;
		this.Category = Category;
		this.Qualification = Qualification;
		this.isLock = isLock;
		this.companyID = companyID;
		this.Cooperation = Cooperation;
	}

	public Integer getNumValue() {
		return numValue;
	}

	public void setNumValue(final Integer numValue) {
		this.numValue = numValue;
	}

	@Override
	public String toString() {
		return this.getIntNr();
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return Category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(final String category) {
		Category = category;
	}

	/**
	 * @return the companyID
	 */
	public String getCompanyID() {
		return companyID;
	}

	/**
	 * @param companyID the companyID to set
	 */
	public void setCompanyID(final String companyID) {
		this.companyID = companyID;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return Country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(final String country) {
		Country = country;
	}

	/**
	 * @return the intNr
	 */
	public String getIntNr() {
		return IntNr;
	}

	/**
	 * @param intNr the intNr to set
	 */
	public void setIntNr(final String intNr) {
		IntNr = intNr;
	}

	/**
	 * @return the isLock
	 */
	public String getIsLock() {
		return isLock;
	}

	/**
	 * @param isLock the isLock to set
	 */
	public void setIsLock(final String isLock) {
		this.isLock = isLock;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return Location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(final String location) {
		Location = location;
	}

	/**
	 * @return the qualification
	 */
	public String getQualification() {
		return Qualification;
	}

	/**
	 * @param qualification the qualification to set
	 */
	public void setQualification(final String qualification) {
		Qualification = qualification;
	}

	/**
	 * @return the street
	 */
	public String getStreet() {
		return Street;
	}

	/**
	 * @param street the street to set
	 */
	public void setStreet(final String street) {
		Street = street;
	}

	/**
	 * @return the zip
	 */
	public String getZip() {
		return Zip;
	}

	/**
	 * @param zip the zip to set
	 */
	public void setZip(final String zip) {
		Zip = zip;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param zip the zip to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	public String getCooperation() {
		return Cooperation;
	}

	public void setCooperation(final String cooperation) {
		Cooperation = cooperation;
	}

}
