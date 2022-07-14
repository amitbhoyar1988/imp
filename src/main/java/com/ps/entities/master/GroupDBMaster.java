package com.ps.entities.master;

/*
 * Class Name Update By MayurG: GroupCompanyMaster to GroupDBMaster
 * Reason: GroupCompanyMaster Table Already exist in 0001 Database.
 */
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "GroupDBMaster")
@Getter
@Setter
@AllArgsConstructor
public class GroupDBMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "groupDBMasterId")
	private int groupCompanyMasterId;

	private String serverName;
	
	private String instanceName;
	
	private String databaseName;
	
	private String groupName;
	
	private String userName;
	
	private String password;
	
	private String currency;
	
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createDateTime;

	private boolean isActive;

	// Default-Constructor
	public GroupDBMaster() {
		super();
	}
}