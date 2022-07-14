package com.ps.entities.master;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class UserPasswordHistory extends AbstractTimeEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="passwordHistoryId")
	private int id;
	
	private String password;
	
	@ManyToOne
	@JoinColumn(name="globalUserMasterId",referencedColumnName = "globalUserMasterId")
	private GlobalUserMaster userMaster;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public GlobalUserMaster getUserMaster() {
		return userMaster;
	}

	public void setUserMaster(GlobalUserMaster userMaster) {
		this.userMaster = userMaster;
	}
		
}
