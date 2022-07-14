package com.ps.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class GlobalCompanyMasterDTO extends AbstractTimeDTO {

	private String code;
	private String companyName;
	private String shortName;
	private String formerName;
	private String companyGroupCode;
	private String address1;
	private String address2;
	private String address3;
	private String country;
	private String pinCode;
	private String state;
	private String city;
	private String village;
	private String phoneNumber;
	private String emailId;
	private String website;
	private boolean isContractor;
	private String typeOfEstablishment;
	private String industryType;
	private String scale;
	private String coClassification;
	private String language;
	private String currency;
	private String startDate;
	private String endDate;
	private String reason;
	private boolean isCompanyActive;
	private String remark;
	private boolean isActive;
	private int globalCompanyMasterId;
	private String logo1ImageName;
	private byte[] companyLogo1;
	private String logo1Type;
	private String logo2ImageName;
	private byte[] companyLogo2;
	private String logo2Type;
	private String logo3ImageName;
	private byte[] companyLogo3;
	private String logo3Type;

	public GlobalCompanyMasterDTO() {
		super();
	}

}
