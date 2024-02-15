package com.khit.recruit.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
public class ChatController {
    
	
	 @GetMapping("/chat") 
	 public String chat(
			 Model model/*, 
			 @AuthenticationPrincipal SecurityUser principal*/){ 
		 /*String name = principal.getMember().getMname();*/
		 /*model.addAttribute("name", name); */ //시큐리티 연결 후
		 return "chat"; 
	}
	 
}