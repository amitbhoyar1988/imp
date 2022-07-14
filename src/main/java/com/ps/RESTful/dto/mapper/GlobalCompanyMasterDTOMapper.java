package com.ps.RESTful.dto.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.ps.RESTful.dto.request.GlobalCompanyMasterRequestDTO;
import com.ps.RESTful.dto.response.GlobalCompanyMasterResponseDTO;
import com.ps.entities.master.GlobalCompanyMaster;
import com.ps.util.StringUtils;

@Component
public class GlobalCompanyMasterDTOMapper implements
		AbstractDTOMapper<GlobalCompanyMasterRequestDTO, GlobalCompanyMasterResponseDTO, GlobalCompanyMaster> {

	Logger logger = Logger.getLogger(GlobalCompanyMasterDTOMapper.class);

	@Override
	public GlobalCompanyMaster dtoToEntity(GlobalCompanyMasterRequestDTO dto) {

		if (dto == null)
			return new GlobalCompanyMaster();
		GlobalCompanyMaster companyMaster = new GlobalCompanyMaster();
		companyMaster.setGlobalCompanyMasterId(dto.getGlobalCompanyMasterId());
		companyMaster.setCode(dto.getCode());
		companyMaster.setCompanyName(dto.getCompanyName());
		companyMaster.setShortName(dto.getShortName());
		companyMaster.setFormerName(dto.getFormerName());
		companyMaster.setCompanyGroupCode(dto.getCompanyGroupCode());
		companyMaster.setAddress1(dto.getAddress1());
		companyMaster.setAddress2(dto.getAddress2());
		companyMaster.setAddress3(dto.getAddress3());
		companyMaster.setCountry(dto.getCountry());
		companyMaster.setPinCode(dto.getPinCode());
		companyMaster.setState(dto.getState());
		companyMaster.setCity(dto.getCity());
		companyMaster.setVillage(dto.getVillage());
		companyMaster.setPhoneNumber(dto.getPhoneNumber());
		companyMaster.setEmailId(dto.getEmailId());
		companyMaster.setWebsite(dto.getWebsite());
		companyMaster.setContractor(dto.isContractor());
		companyMaster.setTypeOfEstablishment(dto.getTypeOfEstablishment());
		companyMaster.setIndustryType(dto.getIndustryType());
		companyMaster.setScale(dto.getScale());
		companyMaster.setCoClassification(dto.getCoClassification());
		companyMaster.setLanguage(dto.getLanguage());
		companyMaster.setCurrency(dto.getCurrency());
		companyMaster.setStartDate(StringUtils.stringToDate(dto.getStartDate()));
		companyMaster.setEndDate(StringUtils.stringToDate(dto.getEndDate()));
		companyMaster.setReason(dto.getReason());
		companyMaster.setCompanyActive(dto.isCompanyActive());
		companyMaster.setRemark(dto.getRemark());

		companyMaster.setCompanyLogo1(dto.getCompanyLogo1());
		companyMaster.setLogo1ImageName(dto.getLogo1ImageName());
		companyMaster.setLogo1Type(dto.getLogo1Type());
		companyMaster.setCompanyLogo2(dto.getCompanyLogo2());
		companyMaster.setLogo2ImageName(dto.getLogo2ImageName());
		companyMaster.setLogo2Type(dto.getLogo2Type());
		companyMaster.setCompanyLogo3(dto.getCompanyLogo3());
		companyMaster.setLogo3ImageName(dto.getLogo3ImageName());
		companyMaster.setLogo3Type(dto.getLogo3Type());

		companyMaster.setCreatedBy(dto.getCreatedBy());
		companyMaster.setCreateDateTime(StringUtils.stringToDate(dto.getCreatedBy()));
		companyMaster.setActive(dto.isActive());
		companyMaster.setLastModifiedBy(dto.getLastModifiedBy());
		companyMaster.setLastModifiedDateTime(StringUtils.stringToDate(dto.getLastModifiedDateTime()));
		return companyMaster;
	}

	@Override
	public GlobalCompanyMasterResponseDTO entityToDto(GlobalCompanyMaster entity) {

		GlobalCompanyMasterResponseDTO dto = new GlobalCompanyMasterResponseDTO();
		dto.setGlobalCompanyMasterId(entity.getGlobalCompanyMasterId());
		dto.setCode(entity.getCode());
		dto.setCompanyName(entity.getCompanyName());
		dto.setShortName(entity.getShortName());
		dto.setFormerName(entity.getFormerName());
		dto.setCompanyGroupCode(entity.getCompanyGroupCode());
		dto.setAddress1(entity.getAddress1());
		dto.setAddress2(entity.getAddress2());
		dto.setAddress3(entity.getAddress3());
		dto.setCountry(entity.getCountry());
		dto.setPinCode(entity.getPinCode());
		dto.setState(entity.getState());
		dto.setCity(entity.getCity());
		dto.setVillage(entity.getVillage());
		dto.setPhoneNumber(entity.getPhoneNumber());
		dto.setEmailId(entity.getEmailId());
		dto.setWebsite(entity.getWebsite());
		dto.setContractor(entity.isContractor());
		dto.setTypeOfEstablishment(entity.getTypeOfEstablishment());
		dto.setIndustryType(entity.getIndustryType());
		dto.setScale(entity.getScale());
		dto.setCoClassification(entity.getCoClassification());
		dto.setLanguage(entity.getLanguage());
		dto.setCurrency(entity.getCurrency());
		dto.setStartDate(StringUtils.dateToString(entity.getStartDate()));
		dto.setEndDate(StringUtils.dateToString(entity.getEndDate()));
		dto.setReason(entity.getReason());
		dto.setCompanyActive(entity.isCompanyActive());
		dto.setRemark(entity.getRemark());

		dto.setCompanyLogo1(entity.getCompanyLogo1());
		dto.setLogo1ImageName(entity.getLogo1ImageName());
		dto.setLogo1Type(entity.getLogo1Type());
		dto.setCompanyLogo2(entity.getCompanyLogo2());
		dto.setLogo2ImageName(entity.getLogo2ImageName());
		dto.setLogo2Type(entity.getLogo2Type());
		dto.setCompanyLogo3(entity.getCompanyLogo3());
		dto.setLogo3ImageName(entity.getLogo3ImageName());
		dto.setLogo3Type(entity.getLogo3Type());

		dto.setActive(entity.isActive());
		dto.setCreatedBy(entity.getCreatedBy());
		dto.setCreateDate(StringUtils.dateToString(entity.getCreateDateTime()));
		dto.setLastModifiedBy(entity.getLastModifiedBy());
		dto.setLastModifiedDateTime(StringUtils.dateToString(entity.getCreateDateTime()));
		return dto;
	}

	public GlobalCompanyMasterResponseDTO entityToDtoWithoutLogo(GlobalCompanyMaster entity) {

		GlobalCompanyMasterResponseDTO dto = new GlobalCompanyMasterResponseDTO();
		dto.setGlobalCompanyMasterId(entity.getGlobalCompanyMasterId());
		dto.setCode(entity.getCode());
		dto.setCompanyName(entity.getCompanyName());
		dto.setShortName(entity.getShortName());
		dto.setFormerName(entity.getFormerName());
		dto.setCompanyGroupCode(entity.getCompanyGroupCode());
		dto.setAddress1(entity.getAddress1());
		dto.setAddress2(entity.getAddress2());
		dto.setAddress3(entity.getAddress3());
		dto.setCountry(entity.getCountry());
		dto.setPinCode(entity.getPinCode());
		dto.setState(entity.getState());
		dto.setCity(entity.getCity());
		dto.setVillage(entity.getVillage());
		dto.setPhoneNumber(entity.getPhoneNumber());
		dto.setEmailId(entity.getEmailId());
		dto.setWebsite(entity.getWebsite());
		dto.setContractor(entity.isContractor());
		dto.setTypeOfEstablishment(entity.getTypeOfEstablishment());
		dto.setIndustryType(entity.getIndustryType());
		dto.setScale(entity.getScale());
		dto.setCoClassification(entity.getCoClassification());
		dto.setLanguage(entity.getLanguage());
		dto.setCurrency(entity.getCurrency());
		dto.setStartDate(StringUtils.dateToString(entity.getStartDate()));
		dto.setEndDate(StringUtils.dateToString(entity.getEndDate()));
		dto.setReason(entity.getReason());
		dto.setCompanyActive(entity.isCompanyActive());
		dto.setRemark(entity.getRemark());

		dto.setActive(entity.isActive());
		dto.setCreatedBy(entity.getCreatedBy());
		dto.setCreateDate(StringUtils.dateToString(entity.getCreateDateTime()));
		dto.setLastModifiedBy(entity.getLastModifiedBy());
		dto.setLastModifiedDateTime(StringUtils.dateToString(entity.getCreateDateTime()));
		return dto;
	}

	public List<GlobalCompanyMasterResponseDTO> entityListToDtoList(List<GlobalCompanyMaster> entity) {

		if (CollectionUtils.isEmpty(entity))
			return new ArrayList<>();

		List<GlobalCompanyMasterResponseDTO> responseDTOList = new ArrayList<>();

		for (GlobalCompanyMaster listValue : entity) {
			GlobalCompanyMasterResponseDTO responseDTO = entityToDtoWithoutLogo(listValue);
			if (responseDTO != null)
				responseDTOList.add(responseDTO);
		}
		return responseDTOList;

	}

	public List<GlobalCompanyMaster> dtoListToEntityList(List<GlobalCompanyMasterRequestDTO> requestDTOList) {

		if (CollectionUtils.isEmpty(requestDTOList))
			return new ArrayList<>();

		List<GlobalCompanyMaster> companyList = new ArrayList<>();

		for (GlobalCompanyMasterRequestDTO listValue : requestDTOList) {
			GlobalCompanyMaster companyMaster = dtoToEntity(listValue);
			if (companyMaster != null)
				companyList.add(companyMaster);
		}
		return companyList;
	}

	public Optional<GlobalCompanyMaster> entityToEntity(GlobalCompanyMaster globalCompanyMasterReq,
			Optional<GlobalCompanyMaster> globalCompanyMasterDBDetails) {

		if (!globalCompanyMasterDBDetails.isPresent())
			return Optional.empty();

		globalCompanyMasterDBDetails.get().setGlobalCompanyMasterId(globalCompanyMasterReq.getGlobalCompanyMasterId());
		globalCompanyMasterDBDetails.get().setCode(globalCompanyMasterReq.getCode());
		globalCompanyMasterDBDetails.get().setCompanyName(globalCompanyMasterReq.getCompanyName());
		globalCompanyMasterDBDetails.get().setShortName(globalCompanyMasterReq.getShortName());
		globalCompanyMasterDBDetails.get().setFormerName(globalCompanyMasterReq.getFormerName());
		globalCompanyMasterDBDetails.get().setCompanyGroupCode(globalCompanyMasterReq.getCompanyGroupCode());
		globalCompanyMasterDBDetails.get().setAddress1(globalCompanyMasterReq.getAddress1());
		globalCompanyMasterDBDetails.get().setAddress2(globalCompanyMasterReq.getAddress2());
		globalCompanyMasterDBDetails.get().setAddress3(globalCompanyMasterReq.getAddress3());
		globalCompanyMasterDBDetails.get().setCountry(globalCompanyMasterReq.getCountry());
		globalCompanyMasterDBDetails.get().setPinCode(globalCompanyMasterReq.getPinCode());
		globalCompanyMasterDBDetails.get().setState(globalCompanyMasterReq.getState());
		globalCompanyMasterDBDetails.get().setCity(globalCompanyMasterReq.getCity());
		globalCompanyMasterDBDetails.get().setVillage(globalCompanyMasterReq.getVillage());
		globalCompanyMasterDBDetails.get().setPhoneNumber(globalCompanyMasterReq.getPhoneNumber());
		globalCompanyMasterDBDetails.get().setEmailId(globalCompanyMasterReq.getEmailId());
		globalCompanyMasterDBDetails.get().setWebsite(globalCompanyMasterReq.getWebsite());
		globalCompanyMasterDBDetails.get().setContractor(globalCompanyMasterReq.isContractor());
		globalCompanyMasterDBDetails.get().setTypeOfEstablishment(globalCompanyMasterReq.getTypeOfEstablishment());
		globalCompanyMasterDBDetails.get().setIndustryType(globalCompanyMasterReq.getIndustryType());
		globalCompanyMasterDBDetails.get().setScale(globalCompanyMasterReq.getScale());
		globalCompanyMasterDBDetails.get().setCoClassification(globalCompanyMasterReq.getCoClassification());
		globalCompanyMasterDBDetails.get().setLanguage(globalCompanyMasterReq.getLanguage());
		globalCompanyMasterDBDetails.get().setCurrency(globalCompanyMasterReq.getCurrency());
		globalCompanyMasterDBDetails.get().setStartDate(globalCompanyMasterReq.getStartDate());
		globalCompanyMasterDBDetails.get().setEndDate(globalCompanyMasterReq.getEndDate());
		globalCompanyMasterDBDetails.get().setReason(globalCompanyMasterReq.getReason());
		globalCompanyMasterDBDetails.get().setCompanyActive(globalCompanyMasterReq.isCompanyActive());
		globalCompanyMasterDBDetails.get().setActive(globalCompanyMasterReq.isActive());
		globalCompanyMasterDBDetails.get().setRemark(globalCompanyMasterReq.getRemark());

		globalCompanyMasterDBDetails.get().setCompanyLogo1(globalCompanyMasterReq.getCompanyLogo1());
		globalCompanyMasterDBDetails.get().setLogo1ImageName(globalCompanyMasterReq.getLogo1ImageName());
		globalCompanyMasterDBDetails.get().setLogo1Type(globalCompanyMasterReq.getLogo1Type());
		globalCompanyMasterDBDetails.get().setCompanyLogo2(globalCompanyMasterReq.getCompanyLogo2());
		globalCompanyMasterDBDetails.get().setLogo2ImageName(globalCompanyMasterReq.getLogo2ImageName());
		globalCompanyMasterDBDetails.get().setLogo2Type(globalCompanyMasterReq.getLogo2Type());
		globalCompanyMasterDBDetails.get().setCompanyLogo3(globalCompanyMasterReq.getCompanyLogo3());
		globalCompanyMasterDBDetails.get().setLogo3ImageName(globalCompanyMasterReq.getLogo3ImageName());
		globalCompanyMasterDBDetails.get().setLogo3Type(globalCompanyMasterReq.getLogo3Type());

		globalCompanyMasterDBDetails.get().setCreatedBy(globalCompanyMasterReq.getCreatedBy());
		globalCompanyMasterDBDetails.get().setCreateDateTime(globalCompanyMasterReq.getCreateDateTime());
		globalCompanyMasterDBDetails.get().setLastModifiedBy(globalCompanyMasterReq.getLastModifiedBy());
		globalCompanyMasterDBDetails.get().setLastModifiedDateTime(globalCompanyMasterReq.getLastModifiedDateTime());
		return globalCompanyMasterDBDetails;
	}

}
