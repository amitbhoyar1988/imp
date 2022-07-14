package com.ps.services.impl;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ps.RESTful.enums.ErrorCode;
import com.ps.RESTful.enums.PasswordPolicyRulesEnum;
import com.ps.RESTful.error.handler.InvalidRequestException;
import com.ps.RESTful.error.handler.ResourceNotFoundException;
import com.ps.dto.EnumDTO;
import com.ps.dto.ErrorDTO;
import com.ps.entities.master.PasswordPolicy;
import com.ps.services.PasswordPolicyService;
import com.ps.services.dao.repository.master.PasswordPolicyRepository;

@Service
public class PasswordPolicyServiceImpl implements PasswordPolicyService{

	Logger logger = Logger.getLogger(PasswordPolicyServiceImpl.class);

	@Autowired
	PasswordPolicyRepository passwordPolicyRepository;
	
	@Override
	public void add(PasswordPolicy passwordPolicy,List<EnumDTO> rulesEnum) {
		
		if(logger.isDebugEnabled()) logger.debug("In  password policy service add method");
		generate(passwordPolicy,rulesEnum);
		
		if(logger.isDebugEnabled()) logger.debug("Checcking if password policy already exists for companyId-> "+passwordPolicy.getCompany().getGlobalCompanyMasterId());
		Optional<PasswordPolicy> existingPolicyOptional = passwordPolicyRepository.findByCompanyId(passwordPolicy.getCompany().getGlobalCompanyMasterId());
		if(existingPolicyOptional.isPresent())
			throw new InvalidRequestException(ErrorCode.BAD_REQUEST, "Password policy for this company already exist!");
		
		if(logger.isDebugEnabled()) logger.debug("adding password policy into database for company id-> "+passwordPolicy.getCompany().getGlobalCompanyMasterId());
		passwordPolicyRepository.save(passwordPolicy);
	}
	
	@Override
	public void update(PasswordPolicy passwordPolicy, List<EnumDTO> rulesEnum) {
		
		if(logger.isDebugEnabled()) logger.debug("In  password policy service update method");
		
		if(passwordPolicy == null || passwordPolicy.getId() == 0)
			throw new InvalidRequestException(ErrorCode.BAD_REQUEST, "Password Policy is Invalid!");
		
		if(logger.isDebugEnabled()) logger.debug("Updating password policy with id-> "+passwordPolicy.getId());
		generate(passwordPolicy,rulesEnum);
		
		if(logger.isDebugEnabled()) logger.debug("updating password policy into database for company id-> "+passwordPolicy.getCompany().getGlobalCompanyMasterId());
		passwordPolicyRepository.save(passwordPolicy);

	}
	
	private PasswordPolicy generate(PasswordPolicy passwordPolicy,List<EnumDTO> rulesEnum){
		
		if(logger.isDebugEnabled()) logger.debug("Calling getRulesBitMap method for Building password policy rules bitMap");
		long rulesBitMap = getRulesBitMap(rulesEnum);
		passwordPolicy.setRulesBitMap(rulesBitMap);
		
		if(logger.isDebugEnabled()) logger.debug("Validating password policy");
		ErrorDTO error = validatePolicy(passwordPolicy);
		if(error != null)
			throw new InvalidRequestException(error.getCode(),error.getMessage());	
		
		return passwordPolicy;
	}
	
	private long getRulesBitMap(List<EnumDTO> rulesEnum) {
		
		if(logger.isDebugEnabled()) logger.debug("In getRulesBitMAp method, building rules bit map");
		
		if(CollectionUtils.isEmpty(rulesEnum)) {
			if(logger.isDebugEnabled()) logger.debug("Rules enum list is empty, returning error response");
			throw new InvalidRequestException(ErrorCode.BAD_REQUEST, "Password Policy rules set is Invalid!");
		}
		
		long policyRulesBitMap = 0;
		if(logger.isDebugEnabled()) logger.debug("Iterating over rulesEnum list size-> "+rulesEnum.size());
		for(EnumDTO rule : rulesEnum){
			if((rule != null) && (rule.getCode() != null)) {
				//check if this results in an exception
				//PasswordPolicyRulesEnum.valueOf(rule.getCode());
				
				if(logger.isDebugEnabled()) logger.debug("Validating rule-> "+rule.getCode());
				
				if(!PasswordPolicyRulesEnum.isValid(rule.getCode())) {					
					throw new InvalidRequestException(ErrorCode.BAD_REQUEST, "Password Policy rule does not exist!");
				}
				
				PasswordPolicyRulesEnum policyruleEnum = PasswordPolicyRulesEnum.valueOf(rule.getCode());
				if(logger.isDebugEnabled()) logger.debug("adding the rule bit into bitMap for rule-> "+policyruleEnum.name()
											+" code-> "+policyruleEnum.getBitValue()+" policyBitMapValue before-> "+policyRulesBitMap);
				
				policyRulesBitMap = policyRulesBitMap | policyruleEnum.getBitValue();	
				
				if(logger.isDebugEnabled()) logger.debug("policy rule-> "+rule.getCode()
				+" code-> "+rule.getCode()+" policyBitMapValue after-> "+policyRulesBitMap);
			}
		};	
		
		return policyRulesBitMap;
	}
	
	private ErrorDTO validatePolicy(PasswordPolicy passwordPolicy) {
		if(logger.isDebugEnabled()) logger.debug("In  password policy service validate method, validating password policy details");
		
		if(passwordPolicy.getCompany() == null) {
			if(logger.isDebugEnabled()) logger.debug("Company is null in password policy, returning error response");
			return new ErrorDTO(ErrorCode.BAD_REQUEST, "Rules are Invalid!");
		}
		
		if(passwordPolicy.getRulesBitMap() <= 0) {
			if(logger.isDebugEnabled()) logger.debug("Rules bitmap is"+passwordPolicy.getRulesBitMap()+" in password policy, returning error response");
			return new ErrorDTO(ErrorCode.BAD_REQUEST, "Company Details are invalid!");
		}
		
		if( ( (passwordPolicy.getRulesBitMap() & PasswordPolicyRulesEnum.ILLEGAL_CHARS_RULE.getBitValue()) == PasswordPolicyRulesEnum.ILLEGAL_CHARS_RULE.getBitValue())
				&& (StringUtils.isBlank(passwordPolicy.getIllegalSetOfChars()))){
			if(logger.isDebugEnabled()) logger.debug("Illegal characters set is null");
			return new ErrorDTO(ErrorCode.BAD_REQUEST, "Illegal characters list is Invalid!");
		}
		
		if( ( (passwordPolicy.getRulesBitMap() & PasswordPolicyRulesEnum.REPEAT_CHARS_RULE.getBitValue()) == PasswordPolicyRulesEnum.REPEAT_CHARS_RULE.getBitValue())
				&& (passwordPolicy.getRepeatCharsCount() == 0)){
			if(logger.isDebugEnabled()) logger.debug("Repeat chars count is 0");
			return new ErrorDTO(ErrorCode.BAD_REQUEST, "Repeat characters count is Invalid!");
		}
		
		if(passwordPolicy.getExpiryInDays() == 0) {
			if(logger.isDebugEnabled()) logger.debug("Password policy expiry in days is 0, returning error response");
			return new ErrorDTO(ErrorCode.BAD_REQUEST, "Password policy expiry day(s) is Invalid!");
		}
		
		if(passwordPolicy.getDueInDays() == 0) {
			if(logger.isDebugEnabled()) logger.debug("Password policy due in days is 0, returning error response");
			return new ErrorDTO(ErrorCode.BAD_REQUEST, "Password policy due day(s) is Invalid!");
		}
		
		return null;
	}


	@Override
	public PasswordPolicy getByCompanyId(int id) {
		
		if(logger.isDebugEnabled()) logger.debug("In getByCompany for company id-> "+id);
				
		if(id == 0) 
			throw new InvalidRequestException(ErrorCode.BAD_REQUEST, "Compay is Invalid!");
		
		Optional<PasswordPolicy> passwordPolicy =  passwordPolicyRepository.findByCompanyId(id);
		
		if(!passwordPolicy.isPresent())
			throw new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "Policy not found");
			
		return passwordPolicy.get();	
	}


	@Override
	public PasswordPolicy getById(int id) {
		
		if(logger.isDebugEnabled()) logger.debug("In getById, id-> "+id);
				
		if(id == 0) 
			throw new InvalidRequestException(ErrorCode.BAD_REQUEST, "Password Policy is Invalid!");
		
		Optional<PasswordPolicy> passwordPolicy =  passwordPolicyRepository.findById(id);
		
		if(!passwordPolicy.isPresent())
			throw new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "Policy not found");
			
		return passwordPolicy.get();	
	}	
}
