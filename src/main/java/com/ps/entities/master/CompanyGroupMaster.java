/**
 * 
 */
package com.ps.entities.master;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author MayurG: This class will define the CompanyGroupMaster. Group Data.
 *
 */
@Entity
@Table(name = "CompanyGroupMaster")
@Getter
@Setter
@AllArgsConstructor
@ToString
@EntityListeners(AuditingEntityListener.class)
public class CompanyGroupMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int companyGroupMasterId;

	private String dbCode;
	private String companyGroupCode;
	private String companyGroupName;
	private String shortName;
	private String scale;
	private Date startDate;
	private Date endDate;
	private String reasonForExit;
	private boolean isCompanyGroupActive;
	private String remark;
	private String createdBy;

	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDateTime;
	private String lastModifiedBy;

	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "lastModifiedDateTime")
	private Date lastModifiedDateTIme;

	private boolean isActive;

	public CompanyGroupMaster() {
		super();
	}
}
