package com.khit.recruit.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.khit.recruit.config.SecurityUser;
import com.khit.recruit.dto.CompanyDTO;
import com.khit.recruit.dto.JobDTO;
import com.khit.recruit.dto.MemberDTO;
import com.khit.recruit.service.JobService;
import com.khit.recruit.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/member")
@RequiredArgsConstructor
@Controller
public class MemberController {
	
	private final MemberService memberService;
	
	private final JobService jobService;

	
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
	
	/*@GetMapping("/resume")
	public String resume() {
		return "member/resume";
	}*/
	
	@GetMapping("/resume")
	public String getResumePageList(
		@RequestParam(value="type", required = false) String type,
		@RequestParam(value="keyword", required = false) String keyword,
		@PageableDefault(page = 1) Pageable pageable,
		Model model) {
		
		Page<JobDTO> jobDTOList = null;
		//검색어가 없으면 페이지 처리를 하고, 검색어가 있으면 검색어로 페이지 처리
		if(keyword == null) {
			jobDTOList = jobService.findListAll(pageable);
		}else if(type != null && type.equals("title")) {
			jobDTOList = jobService.findByJobTitleContaining(keyword, pageable);
		}else if(type != null && type.equals("content")){
			jobDTOList = jobService.findByCnameContaining(keyword, pageable);
		}
		log.info("jobDTOList : " + jobDTOList );
		
		//하단의 페이지 블럭 만들기
		int blockLimit = 10;  //하단에 보여줄 페이지 개수
		//시작 페이지 1, 11, 21    12/10 = 1.2 -> 2.2 -> 2-1, 1*10+1 =11
		int startPage = ((int)(Math.ceil((double)pageable.getPageNumber()/blockLimit))-1)*blockLimit+1;
		//마지막 페이지 10, 20, 30 //12page -> 12 마지막
		int endPage = (startPage+blockLimit-1) > jobDTOList.getTotalPages() ?
				jobDTOList.getTotalPages() : startPage+blockLimit-1;
		endPage = Math.max(endPage, startPage); // 마지막 페이지는 시작 페이지와 같거나 큼
		
		model.addAttribute("jobList", jobDTOList);
		model.addAttribute("type", type);    //검색 유형 보내기
		model.addAttribute("kw", keyword);   //검색어 보내기
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
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
	
	//* 회원정보 수정 */
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