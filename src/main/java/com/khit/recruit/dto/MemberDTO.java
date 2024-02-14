package com.khit.recruit.dto;

import java.sql.Timestamp;

import com.khit.recruit.entity.MemberEntity;
import com.khit.recruit.entity.Role;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MemberDTO {
	
	private Long mid;

	@NotEmpty(message = "아이디는 필수 항목입니다.")
	private String memberId;
	
	@NotEmpty(message = "비밀번호는 필수 항목입니다.")
	private String mpasswd;
	
	@NotEmpty(message = "이름은 필수 항목입니다.")
	private String mname;
	
	private String gender;
	private Integer birth;
	private String phone;
	private Timestamp mcreatedDate; 
	private String email;
	private Role role;
	
	public static MemberDTO toSaveDTO(MemberEntity member) {
		MemberDTO memberDTO = MemberDTO.builder()
				.mid(member.getMid())
				.memberId(member.getMemberId())
				.mpasswd(member.getMpasswd())
				.mname(member.getMname())
				.gender(member.getGender())
				.birth(member.getBirth())
				.phone(member.getPhone())
				.role(member.getRole())
				.mcreatedDate(member.getCreatedDate())
				.email(member.getEmail())
				.build();
		
		return memberDTO;
	}
}
