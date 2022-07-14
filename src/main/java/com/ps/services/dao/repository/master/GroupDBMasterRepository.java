package com.ps.services.dao.repository.master;

import java.util.Optional;

import com.ps.entities.master.GroupDBMaster;

public interface GroupDBMasterRepository extends AbstractRepository<GroupDBMaster, Integer> {
	
	Optional<GroupDBMaster> findByGroupName(String name);
	
}