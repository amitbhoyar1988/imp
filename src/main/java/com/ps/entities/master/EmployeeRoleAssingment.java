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
 * @author MayurG: This class will define the EmployeeRoles for Each Company
 *         which he is working.
 * 
 *         Single User can have access for different companies for different
 *         Roles.
 *
 */

@Entity
@Table(name = "EmployeeRoleAssingment")
@Getter
@Setter
@AllArgsConstructor
@ToString
@EntityListeners(AuditingEntityListener.class)
public class EmployeeRoleAssingment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int employeeRoleAssignmentId;

	@ManyToOne
	@JoinColumn(name = "globalUserMasterId", referencedColumnName = "globalUserMasterId")
	private GlobalUserMaster globalUserMaster;

	@ManyToOne
	@JoinColumn(name = "companyGroupMasterId", referencedColumnName = "companyGroupMasterId")
	private CompanyGroupMaster companyGroupMaster;

	@ManyToOne
	@JoinColumn(name = "globalCompanyMasterId", referencedColumnName = "globalCompanyMasterId")
	private GlobalCompanyMaster globalCompanyMaster;

	@ManyToOne
	@JoinColumn(name = "userRoleId", referencedColumnName = "userRoleId")
	private UserRole userRoles;

	private String remark;
	private Boolean isActive;
	private String createdBy;

	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDateTime;

	private String lastModifiedBy;

	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedDateTime;

	public EmployeeRoleAssingment() {
		super();
	}

}
