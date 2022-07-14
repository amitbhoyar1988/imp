package com.ps.RESTful.dto.mapper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;

import com.ps.dto.EnumDTO;

@Component
public class EnumDTOMapper {

public List<EnumDTO> enumToDTO(Object contents) {
	
	Logger logger = Logger.getLogger(EnumDTOMapper.class);

		List<EnumDTO> enumResponseDTOList = new ArrayList<EnumDTO>();
		
		Enum[] enums = (Enum[])contents;
		
		for(Enum myEnum: enums){
			
			String shortCode = myEnum.name();
			String displayText = null;
			
			try {
			
				Method method = myEnum.getClass().getDeclaredMethod("getValue", null);
				
				if(!method.isAccessible()){
					method.setAccessible(true);
				}
				
				displayText = (String)method.invoke(myEnum, null);
			} catch (Exception e) {
				logger.error("Exception !", e);
			}
			
			enumResponseDTOList.add(new EnumDTO(displayText,shortCode));
			
		}
		
		return enumResponseDTOList;
	}
}
