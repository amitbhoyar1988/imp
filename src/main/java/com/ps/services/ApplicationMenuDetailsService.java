package com.ps.services;

import java.util.List;

import com.ps.beans.ApplicationMenuHierarchyBean;

public interface ApplicationMenuDetailsService {

	//GetAll-Menus List<ApplicationMenuDetails> getAllMenues();
	
	List<ApplicationMenuHierarchyBean> getAllMenues();
	
}
