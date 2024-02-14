package com.khit.recruit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.khit.recruit.config.SecurityUser;
import com.khit.recruit.dto.CompanyDTO;
import com.khit.recruit.dto.MemberDTO;
import com.khit.recruit.service.MemberService;

import jakarta.validation.Valid;


@RequestMapping("/member")
@Controller
public class MemberController {
	
	@Autowired
	private MemberService memberService;

	
	/* 회원가입 및 로그인 */
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
	public String mjoin(@Valid MemberDTO memberDTO, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return "member/mjoin";
		}else {
			memberService.msave(memberDTO);
			return "redirect:/member/login";
		}
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
	
	/* 마이페이지 */
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
	
	/* 회원정보 수정 */
	@GetMapping("/update/{mid}")
	public String updateForm(@PathVariable Long mid, Model model,
							 @AuthenticationPrincipal SecurityUser principal) {
		MemberDTO memberDTO = memberService.findById(mid);
		model.addAttribute("member", memberDTO);
		return "member/detail";
	}
	
	@PostMapping("/update")
	public String update(@ModelAttribute MemberDTO memberDTO) {
		memberService.update(memberDTO);
		
		return "redirect:/" + memberDTO.getMid();
	}
	
	/* 아이디 중복검사 부분 */
	//아이디 중복 검사
	@PostMapping("/check-id")
	public @ResponseBody String checkId(@RequestParam("memberId") String memberId) {
		String resultText = memberService.memberIdCheck(memberId);
		return resultText;
	}
	
	@ResponseBody
	@GetMapping("/auth/kakao/callback")
	public String kLogin(String code) {
		return "카카오 로그인 성공!: " + code;
	}
}