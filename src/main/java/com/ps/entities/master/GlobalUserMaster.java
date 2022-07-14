package com.ps.entities.master;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "GlobalUserMaster")
@Getter
@Setter
@AllArgsConstructor
@ToString
public class GlobalUserMaster extends AbstractTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "globalUserMasterId")
	private int userMasterId;

	//employeeMasterId-belongs-to-respective-tenant
	private int employeeMasterId;
	
	private String userName;

	private String firstName;

	private String lastName;

	@ManyToOne
	@JoinColumn(name = "companyId", referencedColumnName = "globalCompanyMasterId")
	private GlobalCompanyMaster globalCompanyMaster;

	@ManyToOne
	@JoinColumn(name = "groupDBMasterId", referencedColumnName = "groupDBMasterId")
	private GroupDBMaster groupDBMaster;

	private String emailId;

	private String mobileNumber;

	private boolean isActive = true;

	@Temporal(TemporalType.TIMESTAMP)
	private Date activateDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date deActivateDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLoginDateTime;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastPasswordChangeDateTime;

	private boolean loginStatus = false;

	private boolean isLocked;

	private int failedLoginAttempt = 0;

	private Date nextPasswordChangeDateTime;

	private Date passwordDueDateTime;

	@Column(name = "userPassword")
	private String password;

	/*
	 * This-was-commented-by-MayurG-As-this-column-is-not-null-enabled
	 * @Column(insertable = false, updatable = false)
	 */
	private Date validFrom;

	/*
	 * @Column(insertable = false, updatable = false)
	 * This-was-commented-by-MayurG-As-this-column-is-not-null-enabled
	 */
	private Date validTo;

	public GlobalUserMaster() {
		super();
	}

}
