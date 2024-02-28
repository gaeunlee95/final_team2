package com.khit.recruit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Streamable;

import com.khit.recruit.entity.Scrap;


public interface ScrapRepository extends JpaRepository<Scrap, Long>{

	@Query("select s from Scrap s where s.job.jid = :jobId and s.member.mid = :memberId")
	Optional<Scrap> findByJobIdAndMemberId(@Param("jobId") Long jobId, @Param("memberId") Long memberId);

	List<Scrap> findByMemberMid(Long mid);




}
