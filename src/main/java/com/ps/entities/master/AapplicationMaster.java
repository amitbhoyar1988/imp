/**
 * 
 */
package com.ps.entities.master;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author MayurG: AapplicationMaster Table
 * Use full to store the information of Application.
 */
@Entity
@Table(name = "AapplicationMaster")
@Getter
@Setter
@ToString
public class AapplicationMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int applicationMasterId;
	
	private String name;
	private String type;
	private Integer currentVersion;
	private Date deployementDate;
	private int isActive;
	private Date startDate;
	private Date endDate;

}
