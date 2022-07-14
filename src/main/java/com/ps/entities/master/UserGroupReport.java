package com.ps.entities.master;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import org.hibernate.annotations.Immutable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@NamedStoredProcedureQueries({ @NamedStoredProcedureQuery(
		name = "CreateUserGroup",
		procedureName = "CreateUserGroup",
		parameters = { 
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "companyGroupMasterIds", type = String.class),				
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "groupName", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "groupDescription", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "isDefault", type = Boolean.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "remark", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "isActive", type = Boolean.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "currentUser", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "flag", type = String.class)
		}
		) })

@Entity
@Getter  
@Setter 
@ToString 
@AllArgsConstructor
@Immutable
public class UserGroupReport {
	@Id
	private int userGroupId;
	private String groupName;	
	private String groupDescription;
	private String companyGroupIds;	
	private boolean isDefault;	
	private String remark;
	private boolean isActive;
	private String createdBy;
	
	public UserGroupReport() {
		super();
	}
}
