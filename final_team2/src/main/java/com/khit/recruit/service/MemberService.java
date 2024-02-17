package com.khit.recruit.service;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.khit.recruit.dto.CompanyDTO;
import com.khit.recruit.dto.JobDTO;
import com.khit.recruit.dto.MemberDTO;
import com.khit.recruit.entity.CompanyEntity;
import com.khit.recruit.entity.Job;
import com.khit.recruit.entity.MemberEntity;
import com.khit.recruit.entity.Role;
import com.khit.recruit.exception.BootBoardException;
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
	
	public MemberDTO findById(Long mid) {
		Optional<MemberEntity> findMember = memberRepository.findById(mid);
		if(findMember.isPresent()) {
			MemberDTO memberDTO = MemberDTO.toSaveDTO(findMember.get());
			
			return memberDTO;
		}else {
			return null;
		}
	}
	
	/* 아이디 중복검사 */
	public String checkId(String memberId) {
		//db에 있는 이메일을 조회해서 있으면 "OK" 없으면 "NO"를 보냄
		Optional<MemberEntity> findMember = memberRepository.findByMemberId(memberId);
		if(findMember.isEmpty()) {
			return "OK";
		}else if(memberId.length() > 6 || memberId.length() <= 15){ 
			return "leng";
		}else {
			return "NO";
		}
	}

	public MemberDTO findByMid(Long mid) {
		Optional<MemberEntity> findMember = memberRepository.findByMid(mid);
		if(findMember.isPresent()) { //검색한 게시글이 있으면 dto를 컨트롤러로 반환함
			MemberDTO memberDTO = MemberDTO.toSaveDTO(findMember.get());
			return memberDTO;
		}else { //게시글이 없으면 에러 처리
			throw new BootBoardException("게시글을 찾을 수 없습니다.");
		}
	}

	public MemberDTO update(MemberDTO memberDTO, MultipartFile memberFile) throws Exception {
		if(!memberFile.isEmpty()) { //전달된 파일이 있으면
			UUID uuid = UUID.randomUUID();  //무작위 아이디 생성(중복파일의 이름을 생성해줌)
			  String filename = uuid + "_" +memberFile.getOriginalFilename(); //원본 파일
			  String filepath = "C:/springfiles/" + filename;
			  //File 클래스 객체 생성
			  File savedFile = new File(filepath); //실제 저장되는 파일
				memberFile.transferTo(savedFile);
			  //2.파일 이름은 db에 저장
			  memberDTO.setFilename(filename);
			  memberDTO.setFilepath(filepath); //파일 경로 설정함
		}else {
			//수정할 파일이 없으면 게시글 번호 경로만 보여줌
			memberDTO.setFilename(findById(memberDTO.getMid()).getFilename());
			memberDTO.setFilepath(findById(memberDTO.getMid()).getFilepath());
		}
		MemberEntity member = MemberEntity.toSaveUpdate(memberDTO);
	    memberRepository.save(member);
	    return memberDTO;
	}

	public void update(MemberDTO memberDTO) {
		//암호화, 권한 설정
		String encPW = pwEncoder.encode(memberDTO.getMpasswd());
		memberDTO.setMpasswd(encPW);
		memberDTO.setRole(Role.MEMBER);
		
		//변환시 엔티티 메서드를 toSaveUpdate()로 바꿔줌
		MemberEntity member = MemberEntity.toSaveUpdate(memberDTO);
		
		memberRepository.save(member);
	}
}
