package com.ps.RESTful.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AbstractProperties {
	
	private UIProperties ui;

	public UIProperties getUi() {
		return ui;
	}

	public void setUi(UIProperties ui) {
		this.ui = ui;
	}
	
}
