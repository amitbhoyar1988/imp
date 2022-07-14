package com.ps.services;

import java.util.List;

import com.ps.dto.EnumDTO;
import com.ps.entities.master.PasswordPolicy;

public interface PasswordPolicyService {
		
	public void add(PasswordPolicy passwordPolicy,List<EnumDTO> rulesEnum);
	
	public void update(PasswordPolicy passwordPolicy,List<EnumDTO> rulesEnum);
	
	public PasswordPolicy getByCompanyId(int id);
	
	public PasswordPolicy getById(int id);
	
}
