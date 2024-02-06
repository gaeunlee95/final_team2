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
public class MemberEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long mid;
	
	@Column(unique = true, length = 30)
	private String memberId;
	
	@Column(nullable = false, length = 16)
	private String mpasswd;
	
	@Column(nullable = false, length = 30)
	private String mname;
	
	@Column(nullable = false)
	private String gender;
	
	@Column(nullable = false)
	private Integer birth;
	
	@Column(nullable = false, length = 30)
	private String phone;
	
	@Column
	private Timestamp mcreatedDate; 
	
	@Column
	private String email;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
}
