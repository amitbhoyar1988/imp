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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author MayurG: This class will define the Application Menu Details. Will be
 *         used for the JWT creation as Menus are used in User Roles.
 */
@Entity
@Table(name = "ApplicationMenuDetails")
@Getter
@Setter
@AllArgsConstructor
@ToString
@EntityListeners(AuditingEntityListener.class)
public class ApplicationMenuDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDateTime;

	public ApplicationMenuDetails() {
		super();
	}

}
