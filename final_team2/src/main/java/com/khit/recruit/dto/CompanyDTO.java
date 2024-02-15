package com.khit.recruit.dto;

import java.sql.Timestamp;

import com.khit.recruit.entity.CompanyEntity;
import com.khit.recruit.entity.Job;
import com.khit.recruit.entity.Role;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CompanyDTO {

	private Long cid;
	private String brNum;
	private String companyId;
	private String cpasswd;
	private String ownername;
	private String ownerphone;
	private String cname;
	private String tel;
	private String caddress;
	private String postalcode;
	private Timestamp createdDate; 
	private String email;
	private Role role;
	
	//entity -> dto로 변환할 정적 메서드
	//db에 있는 모든 칼럼을 가져옴
	public static CompanyDTO toSaveDTO(CompanyEntity companyEntity) {
		CompanyDTO companyDTO = CompanyDTO.builder()
				.cid(companyEntity.getCid())
				.brNum(companyEntity.getBrNum())
				.companyId(companyEntity.getCompanyId())
				.cpasswd(companyEntity.getCpasswd())
				.ownername(companyEntity.getOwnername())
				.cname(companyEntity.getCname())
				.tel(companyEntity.getTel())
				.caddress(companyEntity.getCaddress())
				.postalcode(companyEntity.getPostalcode())
				.createdDate(companyEntity.getCreatedDate())
				.email(companyEntity.getEmail())
				.role(companyEntity.getRole())
				.build();
		
		return companyDTO;
	}
}
