package com.ps.services.dao.repository.master;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ps.entities.master.UserPasswordHistory;

public interface UserPasswordHistoryRepository extends AbstractRepository<UserPasswordHistory, Integer> {

	@Query("SELECT ph FROM UserPasswordHistory ph WHERE ph.userMaster.userMasterId = :userId")
	List<UserPasswordHistory> findAllByUserId(@Param("userId") int userId);
	
	@Modifying
	@Query("delete FROM UserPasswordHistory ph WHERE ph.userMaster.userMasterId = :userId and id = (SELECT MIN(id) FROM UserPasswordHistory)")
	List<UserPasswordHistory> deleteOldestByUserId(@Param("userId") int userId);
}
