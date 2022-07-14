package com.ps.dto;

public class EnumDTO {

	private String value;
	private String code;
	
	public EnumDTO()
    {
		super();
    }
	public EnumDTO(String value,String code){
		this.value = value;
		this.code = code;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
