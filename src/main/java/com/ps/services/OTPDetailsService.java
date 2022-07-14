package com.ps.services;

import com.ps.beans.JWTBean;
import com.ps.entities.master.OTPMaster;
import com.ps.entities.master.GlobalUserMaster;

public interface OTPDetailsService {

	public JWTBean validateOTP(OTPMaster otpMaster);
	
	public boolean generateAndSaveOTP(GlobalUserMaster userData);
	public String resend(OTPMaster otpMaster);
}
