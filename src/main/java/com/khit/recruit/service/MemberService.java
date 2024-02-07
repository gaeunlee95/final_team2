package com.khit.recruit.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.khit.recruit.dto.CompanyDTO;
import com.khit.recruit.dto.MemberDTO;
import com.khit.recruit.entity.CompanyEntity;
import com.khit.recruit.entity.MemberEntity;
import com.khit.recruit.entity.Role;
import com.khit.recruit.repository.CompanyRepository;
import com.khit.recruit.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberService {

	private final MemberRepository memberRepository;
	private final CompanyRepository companyRepository;
	
	private final PasswordEncoder pwEncoder;
	
	public void msave(MemberDTO memberDTO) {
		String encPw = pwEncoder.encode(memberDTO.getMpasswd());
		memberDTO.setMpasswd(encPw);
		memberDTO.setRole(Role.MEMBER);
		
		//dto를 entity로 변환 메서드
		MemberEntity member = MemberEntity.toSaveEntity(memberDTO);
		
		memberRepository.save(member);
		
	}
	
	public String memberIdCheck(String memberId) {
		//db에 있는 이메일을 조회해서 있으면 "OK" 없으면 "NO"를 보냄
		Optional<MemberEntity> findMember = memberRepository.findByMemberId(memberId);
		if(findMember.isEmpty()) {
			return "OK";
		}else {
			return "NO";
		}
	}
	
	public void cpsave(CompanyDTO companyDTO) {
		String encPw = pwEncoder.encode(companyDTO.getCpasswd());
		companyDTO.setCpasswd(encPw);
		companyDTO.setRole(Role.COMPANY);
		
		//dto를 entity로 변환 메서드
		CompanyEntity company = CompanyEntity.toSaveEntity(companyDTO);
		
		companyRepository.save(company);
	}
}
