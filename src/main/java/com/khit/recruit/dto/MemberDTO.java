package com.khit.recruit.dto;

import java.sql.Timestamp;

import com.khit.recruit.entity.Role;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MemberDTO {
	
	private Long mid;
	private String memberId;
	private String mpasswd;
	private String mname;
	private String gender;
	private Integer birth;
	private String phone;
	private Timestamp mcreatedDate; 
	private String email;
	private Role role;
}
