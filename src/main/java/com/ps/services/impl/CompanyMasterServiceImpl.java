package com.ps.services.impl;

import java.util.Optional;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ps.RESTful.enums.ErrorCode;
import com.ps.RESTful.error.handler.ResourceNotFoundException;
import com.ps.entities.master.GlobalCompanyMaster;
import com.ps.services.CompanyMasterService;
import com.ps.services.dao.repository.master.CompanyMasterRepository;

@Service
public class CompanyMasterServiceImpl implements CompanyMasterService {

	Logger logger = Logger.getLogger(CompanyMasterServiceImpl.class);

	@Autowired
	CompanyMasterRepository companyMasterRepository;

	@Override
	public GlobalCompanyMaster getById(int id) {

		if (logger.isDebugEnabled())
			logger.debug("In CompanyMaster getById "
					+ "service method retrieving company from databse for company id-> " + id);

		Optional<GlobalCompanyMaster> company = companyMasterRepository.findById(id);

		if (!company.isPresent()) {
			if (logger.isDebugEnabled())
				logger.debug("Company not found in databse with company id-> " + id);
			throw new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "Company not found!");
		}

		return company.get();
	}

	/*
	 * 
	 * @Override public GlobalCompanyMaster getByIdWithGroup(int id) {
	 * 
	 * 
	 * /* Comment By MayurG: As CompanyMaster relation with GroupCompany not exist.
	 * 
	 * 
	 * if(logger.isDebugEnabled()) logger.debug("In CompanyMaster getById " +
	 * "service method retrieving company from databse with group company for company id-> "
	 * +id);
	 * 
	 * Optional<CompanyMaster> company =
	 * companyMasterRepository.findByIdAndFetchGroup(id);
	 * 
	 * if(!company.isPresent()) { if(logger.isDebugEnabled())
	 * logger.debug("Company not found in databse with company id-> "+id); throw new
	 * ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND,
	 * "Company not found!"); }
	 * 
	 * return company.get();
	 * 
	 * return null;
	 * 
	 * }
	 * 
	 */

}
