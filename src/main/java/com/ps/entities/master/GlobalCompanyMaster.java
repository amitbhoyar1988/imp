package com.ps.entities.master;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "GlobalCompanyMaster")
@Getter
@Setter
@AllArgsConstructor
@ToString
public class GlobalCompanyMaster {

	/*
	 * Comment By MayurG, As this forign key is not exist in table
	 * 
	 * @ManyToOne
	 * 
	 * @JoinColumn(name="groupDBMasterId",referencedColumnName = "groupDBMasterId")
	 * private GroupDBMaster groupCompany;
	 */

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "globalCompanyMasterId")
	private int globalCompanyMasterId;

	@Column(name = "code")
	private String code;

	@Column(name = "companyName")
	private String companyName;

	@Column(name = "shortName")
	private String shortName;

	@Column(name = "formerName")
	private String formerName;

	@Column(name = "companyGroupCode")
	private String companyGroupCode;

	@Column(name = "address1")
	private String address1;

	@Column(name = "address2")
	private String address2;

	@Column(name = "address3")
	private String address3;

	@Column(name = "country")
	private String country;

	@Column(name = "pinCode")
	private String pinCode;

	@Column(name = "state")
	private String state;

	@Column(name = "city")
	private String city;

	@Column(name = "village")
	private String village;

	@Column(name = "phoneNumber")
	private String phoneNumber;

	@Column(name = "emailId")
	private String emailId;

	@Column(name = "website")
	private String website;

	@Column(name = "isContractor")
	private boolean isContractor;

	@Column(name = "typeOfEstablishment")
	private String typeOfEstablishment;

	@Column(name = "industryType")
	private String industryType;

	@Column(name = "scale")
	private String scale;

	@Column(name = "coClassification")
	private String coClassification;

	@Column(name = "language")
	private String language;

	@Column(name = "currency")
	private String currency;

	@Column(name = "startDate")
	private Date startDate;

	@Column(name = "endDate")
	private Date endDate;

	@Column(name = "reason")
	private String reason;

	@Column(name = "isCompanyActive")
	private boolean isCompanyActive;

	@Column(name = "remark")
	private String remark;

	@Column(name = "createdBy")
	private String createdBy;

	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createDateTime")
	private Date createDateTime;

	@Column(name = "lastModifiedBy")
	private String lastModifiedBy;

	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "lastModifiedDateTime")
	private Date lastModifiedDateTime;

	@Column(name = "isActive")
	private boolean isActive;

	private String logo1ImageName;
	private byte[] companyLogo1;
	private String logo1Type;
	private String logo2ImageName;
	private byte[] companyLogo2;
	private String logo2Type;
	private String logo3ImageName;
	private byte[] companyLogo3;
	private String logo3Type;

	public GlobalCompanyMaster() {
		super();
	}

}
