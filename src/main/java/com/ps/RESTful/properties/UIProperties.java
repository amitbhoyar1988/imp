package com.ps.RESTful.properties;

import org.springframework.stereotype.Component;

@Component
public class UIProperties {

	private String setPasswordLink;	
	private String resetPasswordLink;
	
	public String getSetPasswordLink() {
		return setPasswordLink;
	}
	public void setSetPasswordLink(String setPasswordLink) {
		this.setPasswordLink = setPasswordLink;
	}
	public String getResetPasswordLink() {
		return resetPasswordLink;
	}
	public void setResetPasswordLink(String resetPasswordLink) {
		this.resetPasswordLink = resetPasswordLink;
	}
}
