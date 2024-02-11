package com.khit.recruit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.khit.recruit.dto.CompanyDTO;
import com.khit.recruit.dto.MemberDTO;
import com.khit.recruit.service.MemberService;


@RequestMapping("/member")
@Controller
public class MemberController {
	
	@Autowired
	private MemberService memberService;

	
	@GetMapping("/identity")
	public String certified() {
		return "member/identity";
	}
	
	@GetMapping("/terms")
	public String terms() {
		return "member/terms";
	}
	
	@GetMapping("/mjoin")
	public String mjoinForm(MemberDTO memberDTO) {
		return "member/mjoin";
	}
	
	@PostMapping("/mjoin")
	public String mjoin(MemberDTO memberDTO) {
		memberService.msave(memberDTO);
		return "redirect:/member/login";
	}
	
	@GetMapping("/cpjoin")
	public String confirm() {
		return "member/cpjoin";
	}
	
	@PostMapping("/cpjoin")
	public String cpjoinForm(CompanyDTO companyDTO) {
		memberService.cpsave(companyDTO);
		return "redirect:/member/login";
	}
	
	@GetMapping("/login")
	public String login() {
		return "member/login";
	}
	
	@GetMapping("/mypage")
	public String myPage() {
		return "member/mypage";
	}
	
	@GetMapping("/resume")
	public String resume() {
		return "member/resume";
	}
	
	@GetMapping("/announ")
	public String announ() {
		return "member/announ";
	}
	
	@GetMapping("/jopapp")
	public String jopapp() {
		return "member/jopapp";
	}
	
	//이메일 중복 검사
	@PostMapping("/member/check-email")
	public @ResponseBody String checkEmail(@RequestParam("memberId") String memberId) {
		String resultText = memberService.memberIdCheck(memberId);
		return resultText;
	}
	
	@ResponseBody
	@GetMapping("/auth/kakao/callback")
	public String kLogin(String code) {
		return "카카오 로그인 성공!: " + code;
	}
}