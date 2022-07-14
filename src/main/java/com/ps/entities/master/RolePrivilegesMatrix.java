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
 * @author MayurG: This class will define the RolePrivilegesMatrix for
 *         application. Every Application User must and an Role whose privileges
 *         are defined from here.
 *
 */
@Entity
@Table(name = "RolePrivilegesMatrix")
@Getter
@Setter
@AllArgsConstructor
@ToString
@EntityListeners(AuditingEntityListener.class)
public class RolePrivilegesMatrix {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int rolePrivilegeMatrixId;

	@ManyToOne
	@JoinColumn(name = "userRoleId", referencedColumnName = "userRoleId")
	private UserRole userRoles;

	@ManyToOne
	@JoinColumn(name = "applicationMenuId", referencedColumnName = "applicationMenuId")
	private ApplicationMenus applicationMenus;

	@ManyToOne
	@JoinColumn(name="globalCompanyMasterId", referencedColumnName = "globalCompanyMasterId")
	GlobalCompanyMaster globalCompanyMaster;
	
	private int readAccess;
	private int writeAccess;
	private int modifyAccess;
	private int deleteAccess;
	private int isActive;
	private String createdBy;

	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDateTime;

	private String lastModifiedBy;

	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedDateTime;

	public RolePrivilegesMatrix() {
		super();
	}

}