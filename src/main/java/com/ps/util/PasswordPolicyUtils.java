package com.ps.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.IllegalCharacterRule;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.Rule;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

import com.ps.RESTful.enums.PasswordPolicyRulesEnum;
import com.ps.entities.master.PasswordPolicy;
import com.ps.entities.master.GlobalUserMaster;

public class PasswordPolicyUtils {
	
	static Logger logger = Logger.getLogger(PasswordPolicyUtils.class);

	public static List<String> isValid(GlobalUserMaster user,PasswordPolicy passwordPolicy) {
		
		if(logger.isDebugEnabled()) logger.debug("In PasswordPolicyUtils isValid method");
		
		List<String> results = new ArrayList<String>();
		
		if(passwordPolicy == null) {
			if(logger.isDebugEnabled()) logger.debug("Password policy is null returning false");
			results.add("Password not found!");			
			return results;
		}
		
		if(user == null) {
			if(logger.isDebugEnabled()) logger.debug("UserMaster object is null returning false");
			results.add("User not found!");			
			return results;
		}
		
		results.addAll(validateUserNameRule(user, passwordPolicy.getRulesBitMap()));
		
		List<Rule> rules = getRules(passwordPolicy);
		
		if(logger.isDebugEnabled()) logger.debug("Creating password validator with all the rules");
		PasswordValidator validator = new PasswordValidator(rules);		
		RuleResult result = validator.validate(new PasswordData(user.getPassword()));
		
		if(logger.isDebugEnabled()) logger.debug("Returning result isValid-> "+result.isValid());
		
		if(logger.isDebugEnabled()) logger.debug("Invalid Password: " + validator.getMessages(result));
		
		results.addAll(validator.getMessages(result));

		return results ;
	}
	
	 
	private static List<Rule> getRules(PasswordPolicy passwordPolicy){
		
		if(logger.isDebugEnabled()) logger.debug("In getRules method creating password validation rules based on policy");
		
		long bitMap = passwordPolicy.getRulesBitMap();
		
		List<Rule> rules = new ArrayList<>();
		rules.add(new LengthRule(passwordPolicy.getMinLength(), passwordPolicy.getMaxLength()));
		
		if((bitMap & PasswordPolicyRulesEnum.ONE_DIGIT_RULE.getBitValue()) == 
				PasswordPolicyRulesEnum.ONE_DIGIT_RULE.getBitValue()) {					
			
			if(logger.isDebugEnabled()) logger.debug("Password policy contains ONE_DIGIT_RULE rule");
			rules.add(new CharacterRule(EnglishCharacterData.Digit, 1) );
			
		}
		
		if((bitMap & PasswordPolicyRulesEnum.ONE_LOWERCASE_RULE.getBitValue()) == 
				PasswordPolicyRulesEnum.ONE_LOWERCASE_RULE.getBitValue()) {			
			
			if(logger.isDebugEnabled()) logger.debug("Password policy contains ONE_LOWERCASE_RULE rule");	
			rules.add(new CharacterRule(EnglishCharacterData.LowerCase, 1) );
			
		}
		
		if((bitMap & PasswordPolicyRulesEnum.ONE_UPPERCASE_RULE.getBitValue()) == 
				PasswordPolicyRulesEnum.ONE_UPPERCASE_RULE.getBitValue()) {	
			
			if(logger.isDebugEnabled()) logger.debug("Password policy contains ONE_UPPERCASE_RULE rule");
			rules.add(new CharacterRule(EnglishCharacterData.UpperCase, 1)); 
			
		}
		
		if((bitMap & PasswordPolicyRulesEnum.REPEAT_CHARS_RULE.getBitValue()) == 
				PasswordPolicyRulesEnum.REPEAT_CHARS_RULE.getBitValue()) {	
						
		}
		
		if(!StringUtils.isBlank(passwordPolicy.getAllowedSetOfSpecialChars())) {	
			
			if(logger.isDebugEnabled()) logger.debug("Allowed set of chars in policy are-> "+passwordPolicy.getAllowedSetOfSpecialChars());			
			
			Rule allowedSpecialCharsRule = new CharacterRule(new CharacterData() {
				
				@Override
				public String getErrorCode() {
					return "INSUFFICIENT_SPECIAL";
				}
				
				@Override
				public String getCharacters() {
					return passwordPolicy.getAllowedSetOfSpecialChars();
				}
				
			}, 1);

			rules.add(allowedSpecialCharsRule);
			
		}else {
			if((bitMap & PasswordPolicyRulesEnum.SPECIAL_CHARS_RULE.getBitValue()) == 
					PasswordPolicyRulesEnum.SPECIAL_CHARS_RULE.getBitValue()) {	
				
				if(logger.isDebugEnabled()) logger.debug("Password policy contains SPECIAL_CHARS_RULE rule");
				rules.add(new CharacterRule(EnglishCharacterData.Special, 1)); 
				
			}
		}
		
		if((bitMap & PasswordPolicyRulesEnum.ILLEGAL_CHARS_RULE.getBitValue()) == 
				PasswordPolicyRulesEnum.ILLEGAL_CHARS_RULE.getBitValue()) {	
			
			if(logger.isDebugEnabled()) logger.debug("Password policy contains ILLEGAL_CHARS_RULE rule");
						
			if(!StringUtils.isBlank(passwordPolicy.getIllegalSetOfChars())) {
				
				String illegalCharsString = passwordPolicy.getIllegalSetOfChars().replace(",", "");
				
				rules.add(new IllegalCharacterRule(illegalCharsString.toCharArray()));
			}				
		}
		
		if((bitMap & PasswordPolicyRulesEnum.NO_WHITESPACE_RULE.getBitValue()) == 
				PasswordPolicyRulesEnum.NO_WHITESPACE_RULE.getBitValue()) {	
			
			if(logger.isDebugEnabled()) logger.debug("Password policy contains NO_WHITESPACE_RULE rule");
			 rules.add(new WhitespaceRule());
			 
		}
				
		return rules;
	}
	
	private static List<String> validateUserNameRule(GlobalUserMaster user, long bitMap){
		
		List<String> result = new ArrayList<String>();
		
		if((bitMap & PasswordPolicyRulesEnum.NO_FIRSTNAME_RULE.getBitValue()) == 
				PasswordPolicyRulesEnum.NO_FIRSTNAME_RULE.getBitValue()) {		
			if(logger.isDebugEnabled()) logger.debug("Password policy contains NO_FIRSTNAME rule");
			
			if(user.getPassword().contains(user.getFirstName())) 
				result.add("Password Should not contain users' First name!");
		}
		
		if ((bitMap & PasswordPolicyRulesEnum.NO_LASTNAME_RULE.getBitValue()) == 
				PasswordPolicyRulesEnum.NO_LASTNAME_RULE.getBitValue()) {
			if(logger.isDebugEnabled()) logger.debug("Password policy contains NO_LASTNAME rule");

			if(user.getPassword().contains(user.getLastName())) 
				result.add("Password Should not contain Users' Last name!");
		}
				
		return result;
	}
			
}


