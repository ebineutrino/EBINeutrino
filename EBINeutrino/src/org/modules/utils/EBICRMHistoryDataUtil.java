package org.modules.utils;

import java.util.List;

public class EBICRMHistoryDataUtil {

	private int companyID = 0;
	private String category="";
	private List<String> field = null;
	private String changedFrom = null;
	private String changedDate = null;
	
	public EBICRMHistoryDataUtil(final int companyID,final String category,final List<String>field){
		this.companyID = companyID;
		this.category = category;
		this.field = field;
	}
	public EBICRMHistoryDataUtil(){}

	public int getCompanyId() {
		return companyID;
	}

	public void setCompanyId(final int companyID) {
		this.companyID = companyID;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(final String category) {
		this.category = category;
	}

	public List<String> getField() {
		return field;
	}

	public void setField(final List<String> field) {
		this.field = field;
	}
	public String getChangedFrom() {
		return changedFrom;
	}
	public void setChangedFrom(final String changedFrom) {
		this.changedFrom = changedFrom;
	}
	public String getChangedDate() {
		return changedDate;
	}
	public void setChangedDate(final String changedDate) {
		this.changedDate = changedDate;
	}
	
}
