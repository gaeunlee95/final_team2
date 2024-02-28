package com.khit.recruit.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.khit.recruit.entity.Job;

public interface JobRepository extends JpaRepository<Job, Long>{
	
	//공고 제목 검색
	Page<Job> findByTitleContaining(String keyword, Pageable pageable);
	
	//병원이름 검색
	@Query("SELECT j FROM Job j JOIN j.company c WHERE c.cname LIKE %:cname%")
    Page<Job> findByCnameContaining(@Param("cname") String keyword, Pageable pageable);
	
	//지역별 검색
	Page<Job> findByAddress01Containing(String city, Pageable pageable);
	
	Optional<Job> findByJid(Long jid);

	Page<Job> findByCompany_Cid(Pageable pageable, Long cid);

	
	
	// 도시(지역), 공고 제목, 병원 이름을 기준으로 검색
	// 값이 NULL일 경우 무시
	/*@Query("SELECT j FROM Job j JOIN j.company c WHERE " +
	           "(:city IS NULL OR j.address01 LIKE %:city%) AND " +
	           "(:title IS NULL OR j.title LIKE %:title%) AND " +
	           "(:cname IS NULL OR c.cname LIKE %:cname%)")
	    Page<Job> findByOptionalCriteria(@Param("city") String city, 
	                                     @Param("title") String title, 
	                                     @Param("cname") String cname, 
	                                     Pageable pageable);
	}*/
}