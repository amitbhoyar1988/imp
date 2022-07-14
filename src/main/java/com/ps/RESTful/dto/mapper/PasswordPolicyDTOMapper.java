package com.ps.RESTful.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.ps.RESTful.dto.request.PasswordPolicyRequestDTO;
import com.ps.RESTful.dto.response.PasswordPolicyResponseDTO;
import com.ps.RESTful.enums.PasswordPolicyRulesEnum;
import com.ps.dto.EnumDTO;
import com.ps.entities.master.PasswordPolicy;

@Component
public class PasswordPolicyDTOMapper implements AbstractDTOMapper<PasswordPolicyRequestDTO,PasswordPolicyResponseDTO, PasswordPolicy> {
	
	Logger logger = Logger.getLogger(PasswordPolicyDTOMapper.class);
	
	// Request Mapping methods below
	
	@Override
	public PasswordPolicy dtoToEntity(PasswordPolicyRequestDTO passwordPolicyDTO) {
		
		if(logger.isDebugEnabled()) logger.debug("In PasswordPolicyDTOMapper mapping PasswordPolicyDTO to PasswordPolicy entity"); 		
		if(passwordPolicyDTO == null) return new PasswordPolicy();
		
		PasswordPolicy passwordPolicy = new PasswordPolicy();	
		passwordPolicy.setExpiryInDays(passwordPolicyDTO.getExpiryInDays());
		passwordPolicy.setExpired(passwordPolicyDTO.isExpired());
		passwordPolicy.setAllowedSetOfSpecialChars(passwordPolicyDTO.getAllowedSetOfSpecialChars());
		passwordPolicy.setIllegalSetOfChars(passwordPolicyDTO.getIllegalSetOfChars());
		passwordPolicy.setRepeatCharsCount(passwordPolicyDTO.getRepeatCharsCount());
		passwordPolicy.setAllowedPasswordHistorycount(passwordPolicyDTO.getAllowedPasswordHistorycount());
		passwordPolicy.setDueInDays(passwordPolicyDTO.getDueInDays());
		
		return passwordPolicy;
	}

	public List<PasswordPolicy> dtoListToEntityList(List<PasswordPolicyRequestDTO> passwordPolicyDTOList) {		
		
		if(logger.isDebugEnabled()) logger.debug("In PasswordPolicyDTOMapper dtoListToEntityList method "
				+ "mapping PasswordPolicyRequestDTO list to PasswordPolicy entity list"); 
		
		List<PasswordPolicy> passwordPolicyEntityList = new ArrayList<PasswordPolicy>();		

		if(!CollectionUtils.isEmpty(passwordPolicyDTOList)) {
			for (PasswordPolicyRequestDTO passwordPolicyDTO : passwordPolicyDTOList) {
				PasswordPolicy passwordPolicy = dtoToEntity(passwordPolicyDTO);
				if(passwordPolicy != null) passwordPolicyEntityList.add(passwordPolicy);
			}
		}		
		return passwordPolicyEntityList;
	}
	
	// Response Mapping methods below

	@Override
	public PasswordPolicyResponseDTO entityToDto(PasswordPolicy entity) {
		
		PasswordPolicyResponseDTO responseDTO = new PasswordPolicyResponseDTO();
		
		responseDTO.setIllegalSetOfChars(entity.getIllegalSetOfChars());
		responseDTO.setAllowedSetOfSpecialChars(entity.getAllowedSetOfSpecialChars());
		responseDTO.setRepeatCharsCount(entity.getRepeatCharsCount());
		responseDTO.setDescription(entity.getDescription());
		responseDTO.setExpired(entity.isExpired());
		responseDTO.setExpiryInDays(entity.getExpiryInDays());
		responseDTO.setId(entity.getId());
		responseDTO.setRules(bitMapToEnumDTO(entity));

		return responseDTO;
	}
	
	
	private List<EnumDTO> bitMapToEnumDTO(PasswordPolicy passwordPolicy){
		
		if(logger.isDebugEnabled()) logger.debug("In bitMapToEnumDTO method");
		
		long bitMap = passwordPolicy.getRulesBitMap();
		
		List<EnumDTO> rulesEnum = new ArrayList<>();
		
		if(passwordPolicy.getMaxLength() != 0)
			rulesEnum.add(new EnumDTO("Password can have maximum of "+passwordPolicy.getMaxLength()+" characters",
					"MAX_LENGTH"));
		
		if(passwordPolicy.getMinLength() != 0)
			rulesEnum.add(new EnumDTO("Password must have minimum of "+passwordPolicy.getMinLength()+" characters",
					"MIN_LENGTH"));
		
		if((bitMap & PasswordPolicyRulesEnum.ONE_DIGIT_RULE.getBitValue()) == 
				PasswordPolicyRulesEnum.ONE_DIGIT_RULE.getBitValue()) {	
			
			if(logger.isDebugEnabled()) logger.debug("Password policy contains ONE_DIGIT_RULE rule");
			rulesEnum.add(new EnumDTO(PasswordPolicyRulesEnum.ONE_DIGIT_RULE.getValue(),
					PasswordPolicyRulesEnum.ONE_DIGIT_RULE.name()));
			
		}
		
		if((bitMap & PasswordPolicyRulesEnum.ONE_LOWERCASE_RULE.getBitValue()) == 
				PasswordPolicyRulesEnum.ONE_LOWERCASE_RULE.getBitValue()) {	
			
			if(logger.isDebugEnabled()) logger.debug("Password policy contains ONE_LOWERCASE_RULE rule");	
			rulesEnum.add(new EnumDTO(PasswordPolicyRulesEnum.ONE_LOWERCASE_RULE.getValue(),
					PasswordPolicyRulesEnum.ONE_LOWERCASE_RULE.name()));
			
		}
		
		if((bitMap & PasswordPolicyRulesEnum.ONE_UPPERCASE_RULE.getBitValue()) == 
				PasswordPolicyRulesEnum.ONE_UPPERCASE_RULE.getBitValue()) {	
			
			if(logger.isDebugEnabled()) logger.debug("Password policy contains ONE_UPPERCASE_RULE rule");
			rulesEnum.add(new EnumDTO(PasswordPolicyRulesEnum.ONE_UPPERCASE_RULE.getValue(),
					PasswordPolicyRulesEnum.ONE_UPPERCASE_RULE.name()));
			
		}
		
		if((bitMap & PasswordPolicyRulesEnum.ILLEGAL_CHARS_RULE.getBitValue()) == 
				PasswordPolicyRulesEnum.ILLEGAL_CHARS_RULE.getBitValue()) {
			
			if(logger.isDebugEnabled()) logger.debug("Password policy contains ILLEGAL_CHARS_RULE rule");
			
			String value = String.format(PasswordPolicyRulesEnum.ILLEGAL_CHARS_RULE.getValue(),passwordPolicy.getIllegalSetOfChars());
			rulesEnum.add(new EnumDTO(value, PasswordPolicyRulesEnum.ILLEGAL_CHARS_RULE.name()));
			
		}
		
		if(!StringUtils.isBlank(passwordPolicy.getAllowedSetOfSpecialChars())) {
			
			if(logger.isDebugEnabled()) logger.debug("Password policy contains SPECIAL_CHARS_RULE rule allowedSetOfSpecialChars-> "+passwordPolicy.getAllowedSetOfSpecialChars());

			String value = PasswordPolicyRulesEnum.SPECIAL_CHARS_RULE.getValue()+" (Allowed Special Characters - "+passwordPolicy.getAllowedSetOfSpecialChars()+")";
			rulesEnum.add(new EnumDTO(value, PasswordPolicyRulesEnum.SPECIAL_CHARS_RULE.name()));
			
		}else {
			if((bitMap & PasswordPolicyRulesEnum.SPECIAL_CHARS_RULE.getBitValue()) == 
					PasswordPolicyRulesEnum.SPECIAL_CHARS_RULE.getBitValue()) {				
				
				if(logger.isDebugEnabled()) logger.debug("Password policy contains SPECIAL_CHARS_RULE rule");
				rulesEnum.add(new EnumDTO(PasswordPolicyRulesEnum.SPECIAL_CHARS_RULE.getValue(),
						PasswordPolicyRulesEnum.SPECIAL_CHARS_RULE.name()));
				
			}
		}		
		
		if((bitMap & PasswordPolicyRulesEnum.REPEAT_CHARS_RULE.getBitValue()) == 
				PasswordPolicyRulesEnum.REPEAT_CHARS_RULE.getBitValue()) {
						
			if(logger.isDebugEnabled()) logger.debug("Password policy contains REPEAT_CHARS_RULE rule");
			
			String value = String.format(PasswordPolicyRulesEnum.REPEAT_CHARS_RULE.getValue(),passwordPolicy.getRepeatCharsCount());
			rulesEnum.add(new EnumDTO(value, PasswordPolicyRulesEnum.REPEAT_CHARS_RULE.name()));
			
		}
		
		if((bitMap & PasswordPolicyRulesEnum.NO_WHITESPACE_RULE.getBitValue()) == 
				PasswordPolicyRulesEnum.NO_WHITESPACE_RULE.getBitValue()) {	
			
			if(logger.isDebugEnabled()) logger.debug("Password policy contains WHITESPACE_NOT_ALLOWED rule");
			rulesEnum.add(new EnumDTO(PasswordPolicyRulesEnum.NO_WHITESPACE_RULE.getValue(),
					PasswordPolicyRulesEnum.NO_WHITESPACE_RULE.name()));
			
		}
				
		if((bitMap & PasswordPolicyRulesEnum.NO_FIRSTNAME_RULE.getBitValue()) == 
				PasswordPolicyRulesEnum.NO_FIRSTNAME_RULE.getBitValue()) {		
			
			if(logger.isDebugEnabled()) logger.debug("Password policy contains NO_FIRSTNAME rule");
			rulesEnum.add(new EnumDTO(PasswordPolicyRulesEnum.NO_FIRSTNAME_RULE.getValue(),
					PasswordPolicyRulesEnum.NO_FIRSTNAME_RULE.name()));
			
		}
		
		if((bitMap & PasswordPolicyRulesEnum.NO_LASTNAME_RULE.getBitValue()) == 
				PasswordPolicyRulesEnum.NO_LASTNAME_RULE.getBitValue()){
			
			if(logger.isDebugEnabled()) logger.debug("Password policy contains NO_LASTNAME rule");
			rulesEnum.add(new EnumDTO(PasswordPolicyRulesEnum.NO_LASTNAME_RULE.getValue(),
					PasswordPolicyRulesEnum.NO_LASTNAME_RULE.name()));
			
		}
		
		return rulesEnum;
	}
	
}
