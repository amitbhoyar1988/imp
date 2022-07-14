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
 * @author MayurG:- Repository to save the application Menu Details. This is
 *         self-joined table, Will abel to hold the Parent-child relationship.
 *
 */
@Entity(name = "ApplicationMenus")
@Getter
@Setter
@ToString
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ApplicationMenus {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int applicationMenuId;

	private int parentMenuId;
	private String menuName;
	private String menuDescription;
	private Boolean isActive;

	private String createdBy;

	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDateTime;

	private String lastModifiedBy;

	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedDateTime;

	public ApplicationMenus() {
		super();
	}

}
