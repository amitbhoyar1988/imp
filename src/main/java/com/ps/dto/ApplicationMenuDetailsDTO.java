package com.ps.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ApplicationMenuDetailsDTO implements Comparable<ApplicationMenuDetailsDTO> {

	private int menuId;

	private String mainMenuName;

	private String subMenuOrFormName1;
	private String subMenuOrFormName2;
	private String subMenuOrFormName3;
	private String subMenuOrFormName4;
	private String subMenuOrFormName5;
	private String subMenuOrFormName6;
	private String subMenuOrFormName7;
	private String subMenuOrFormName8;
	private String subMenuOrFormName9;
	private String subMenuOrFormName10;

	private String description;
	private int isActive;

	private String createdBy;
	private String createdDateTime;

	public ApplicationMenuDetailsDTO() {
		super();
	}

	/*
	 * To-Arrange-Menus-by-ParentMenuId-Below-Comparable-will-used
	 */
	@Override
	public int compareTo(ApplicationMenuDetailsDTO menuDetails) {

		if (this.getMenuId() == menuDetails.getMenuId())
			return 0;
		else if (this.getMenuId() > menuDetails.getMenuId())
			return 1;
		else
			return -1;
	}

}
