/**
 * 
 */
package com.ps.entities.master;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
 * @author MayurG: This class will define the Application Roles which are assign
 *         to the Users.
 *
 */
@Entity
@Table(name = "ApplicationUserRoles")
@Getter
@Setter
@AllArgsConstructor
@ToString
@EntityListeners(AuditingEntityListener.class)
public class ApplicationUserRoles {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int applicationRoleId;

	@ManyToOne
	@JoinColumn(name = "companyGroupMasterId", referencedColumnName = "companyGroupMasterId")
	private CompanyGroupMaster companyGroupMaster;

	@ManyToOne
	@JoinColumn(name = "globalCompanyMasterId", referencedColumnName = "globalCompanyMasterId")
	private GlobalCompanyMaster globalCompanyMaster;

	private String roleName;
	private String roleDiscription;
	private int isActive;
	private String createdBy;

	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDateTime;

	private String lastModifiedBy;

	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedDateTime;

	public ApplicationUserRoles() {
		super();
	}
}
