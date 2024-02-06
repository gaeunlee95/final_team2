package com.khit.recruit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.khit.recruit.dto.MemberDTO;
import com.khit.recruit.service.MemberService;


@RequestMapping("/member")
@Controller
public class MemberController {
	
	@Autowired private JavaMailSender mailSender;
	
	@Autowired
	private MemberService memberService;

	
	@GetMapping("/join")
	public String certified() {
		return "/member/join";
	}
	
	@GetMapping("/terms")
	public String terms() {
		return "/member/terms";
	}
	
	@GetMapping("/mjoin")
	public String joinForm(MemberDTO memberDTO) {
		return "/member/info";
	}
	
	@PostMapping("/mjoin")
	public String mjoin() {
		
		return "redirect:/member/login";
	}
	
	@GetMapping("/cpjoin")
	public String confirm() {
		return "/member/cpinfo";
	}
	
	@GetMapping("/login")
	public String login() {
		return "/member/login";
	}
	
	@GetMapping("/mypage")
	public String myPage() {
		return "/member/mypage";
	}
	
	@GetMapping("/resume")
	public String resume() {
		return "/member/resume";
	}
	
	@GetMapping("/announ")
	public String announ() {
		return "/member/announ";
	}
	
	@GetMapping("/jopapp")
	public String jopapp() {
		return "/member/jopapp";
	}
	
	@ResponseBody
	@GetMapping("/auth/kakao/callback")
	public String kLogin(String code) {
		return "카카오 로그인 성공!: " + code;
	}
}