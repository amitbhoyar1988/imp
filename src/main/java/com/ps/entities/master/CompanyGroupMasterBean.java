package com.ps.entities.master;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter  
@Setter 
@ToString 
@AllArgsConstructor
@NoArgsConstructor
@Immutable
public class CompanyGroupMasterBean {
	int userGroupId;
	@Id
	int companyGroupMasterId;		
	String companyGroupCode;
	String companyGroupName;
	String shortName;
	boolean isActive;
}
