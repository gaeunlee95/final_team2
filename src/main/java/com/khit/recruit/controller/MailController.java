package com.khit.recruit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.khit.recruit.dto.MailDTO;
import com.khit.recruit.service.MailService;

import lombok.AllArgsConstructor;

@Controller
public class MailController {
	
	@Autowired
	private MailService mailService;
	
	@GetMapping("/mail")
    public String dispMail() {
        return "/mail/mail";
    }

    @PostMapping("/mail")
    public void execMail(MailDTO mailDTO) {
        mailService.mailSend(mailDTO);
    }
}
