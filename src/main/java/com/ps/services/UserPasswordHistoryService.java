package com.ps.services;

import java.util.List;

import com.ps.entities.master.UserPasswordHistory;

public interface UserPasswordHistoryService {

	public void add(UserPasswordHistory passwordHistory);
	
	public List<UserPasswordHistory> getAllByUserId(int userId);
	
	public void deleteOldestByUserId(int userId);
	
	public void deleteById(int id);
}
