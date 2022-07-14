package com.ps.services;

import java.util.List;

import com.ps.dto.EmailDTO;

public interface EmailService {

	public void send(EmailDTO emailDTO);
	
	public void sendAll(List<EmailDTO> emailDTOList);
	
}
