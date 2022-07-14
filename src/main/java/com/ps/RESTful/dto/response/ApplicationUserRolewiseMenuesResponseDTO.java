/**
 * 
 */
package com.ps.RESTful.dto.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author MayurG: This ResponseDTO will be used to hold the details of
 *         Application User Details. Includes: 1. Basic User Detail 2.User Role
 *         Details
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class ApplicationUserRolewiseMenuesResponseDTO {

	private int rolePrivilegeMatrixId;
	private int globalCompanyMasterId;
	private String companyName;
	
	private UserRoleResponseDTO userRoleDetail;
	//private ApplicationMenuDetailsResponseDTO accessibleMenuDetails;
	private ApplicationMenusResponseDTO accessibleMenuDetail;
	
	private int readAccess;
	private int writeAccess;
	private int modifyAccess;
	private int deleteAccess;
	private int isActive;
	private String createdBy;
	private String createdDateTime;
	private String lastModifiedBy;
	private String lastModifiedDateTime;

	public ApplicationUserRolewiseMenuesResponseDTO() {
		super();
	}
}
