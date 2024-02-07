package com.khit.recruit.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.khit.recruit.dto.MailDTO;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class MailService {
	
	private JavaMailSender javaMailSender;
	private static final String From_address = "mybity2@gmail.com";
	
	
	public void mailSend(MailDTO mailDTO) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(mailDTO.getAddress());
        message.setFrom(MailService.From_address);
        message.setSubject(mailDTO.getTitle());
        message.setText(mailDTO.getMessage());
        
        javaMailSender.send(message);
	}
}
