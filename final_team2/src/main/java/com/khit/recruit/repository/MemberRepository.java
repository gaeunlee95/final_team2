package com.khit.recruit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khit.recruit.entity.CompanyEntity;
import com.khit.recruit.entity.MemberEntity;

public interface MemberRepository extends JpaRepository<MemberEntity, Long>{
	Optional<MemberEntity> findByMemberId(String String);

	Optional<MemberEntity> findByMid(Long mid);

}
