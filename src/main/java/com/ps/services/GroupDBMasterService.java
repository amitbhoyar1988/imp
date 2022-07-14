package com.ps.services;

import java.util.List;

import com.ps.entities.master.GroupDBMaster;

public interface GroupDBMasterService {

	public List<GroupDBMaster> getAll();
	
	public GroupDBMaster getById(int id);
	
}
