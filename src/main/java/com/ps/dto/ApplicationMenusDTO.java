package com.ps.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ApplicationMenusDTO implements Comparable<ApplicationMenusDTO> {

	private int applicationMenuId;

	private int parentMenuId;
	private String menuName;
	private String menuDescription;
	private Boolean isActive;

	private String createdBy;

	private String createdDateTime;

	private String lastModifiedBy;

	private String lastModifiedDateTime;

	/*
	 * To-Arrange-Menus-by-ParentMenuId-Below-Comparable-will-used
	 */
	@Override
	public int compareTo(ApplicationMenusDTO menuDetails) {

		if (this.getApplicationMenuId() == menuDetails.getApplicationMenuId())
			return 0;
		else if (this.getApplicationMenuId() > menuDetails.getApplicationMenuId())
			return 1;
		else
			return -1;
	}
}
