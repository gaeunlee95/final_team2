package com.khit.recruit.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class CompanyEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cid;
	
	@Column(length = 15)
	private String brNum;
	
	@Column(unique = true, length = 30)
	private String companyId;
	
	@Column(nullable = false, length = 16)
	private String cpasswd;
	
	@Column(nullable = false, length = 30)
	private String ownername;
	
	@Column(nullable = false, length = 16)
	private String ownerphone;
	
	@Column(nullable = false, length = 30)
	private String cname;
	
	@Column(length = 30)
	private String tel;
	
	@Column(nullable = false)
	private String caddress;
	
	@Column(nullable = false, length = 30)
	private String postalcode;
	
	@Column
	private Timestamp mcreatedDate; 
	
	@Column
	private String email;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
}