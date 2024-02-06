package com.khit.recruit.dto;

import java.sql.Timestamp;

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
	private Timestamp mcreatedDate; 
	private String email;
}
