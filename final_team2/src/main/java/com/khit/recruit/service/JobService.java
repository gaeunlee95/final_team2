package com.khit.recruit.service;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.khit.recruit.dto.ApplyDTO;
import com.khit.recruit.dto.CompanyDTO;
import com.khit.recruit.dto.JobDTO;
import com.khit.recruit.entity.Apply;
import com.khit.recruit.entity.CompanyEntity;
import com.khit.recruit.entity.Job;
import com.khit.recruit.entity.MemberEntity;
import com.khit.recruit.entity.Scrap;
import com.khit.recruit.exception.BootBoardException;
import com.khit.recruit.repository.CompanyRepository;
import com.khit.recruit.repository.JobRepository;
import com.khit.recruit.repository.MemberRepository;
import com.khit.recruit.repository.ScrapRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class JobService {
	
	private final JobRepository jobRepository;
	private final MemberRepository memberRepository;
	private final ScrapRepository scrapRepository;
	
	
	public void save(JobDTO jobDTO, MultipartFile jobFile ) throws IllegalStateException, IOException {
		//1. 파일을 서버에 저장하고,
		if(jobFile != null && !jobFile.isEmpty()) {  //전달된 파일이 있으면
			//저장 경로
			//배포 후 해당 경로 못찾음..
			//String filepath = "C:\\bootworks\\bootboard\\src\\main\\resources\\static\\upload\\";
			
			UUID uuid = UUID.randomUUID();  //무작위 아이디 생성(중복파일의 이름을 생성해줌)
			String filename = uuid + "_" + jobFile.getOriginalFilename();  //원본 파일
			String filepath = "C:/springfiles/" + filename;
		
			//File 클래스 객체 생성
			File savedFile = new File(filepath); //실제 파일
			jobFile.transferTo(savedFile);
		
			//2. 파일 이름은 db에 저장
			jobDTO.setFilename(filename);
			jobDTO.setFilepath(filepath); //파일 경로 설정함
		}
		
		//dto -> entity로 변환
		Job job = Job.toSaveEntity(jobDTO);
		//entity를 db에 저장
		jobRepository.save(job);
	}
	
	//job_main : 주소(address01) 간소화하는 메서드
	private String extractAddress(String fullAddress) {
	    // 예시로, "서울 영등포구 선유로 1"에서 "서울 영등포구" 부분만 추출
	    String[] parts = fullAddress.split(" ");
	    if(parts.length >= 2) {
	        // 첫 번째와 두 번째 부분만 합쳐서 반환
	        return parts[0] + " " + parts[1];
	    }
	    return fullAddress; // 분할에 실패한 경우 원본 주소 반환
	}
	

	public Page<JobDTO> findListAll(Pageable pageable, Long mid) {
		int page = pageable.getPageNumber() - 1; //db는 현재페이지보다 1 작아야함
		int pageSize = 10;
		pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "jid");
		
		Page<Job> jobList = jobRepository.findAll(pageable);
		
		log.info("jobList.isFirst()=" + jobList.isFirst());
		log.info("jobList.isLast()=" + jobList.isLast());
		log.info("jobList.getNumber()=" + jobList.getNumber());
		
		// 현재 사용자가 스크랩한 공고의 jid 목록 조회
	    List<Long> scrappedJobIds = scrapRepository.findByMemberMid(mid)
	        .stream()
	        //scrap의 jid를 추출
	        .map(scrap -> scrap.getJob().getJid())
	        .collect(Collectors.toList());
		
		
		// Page<Job>를 Page<JobDTO>로 변환
	    Page<JobDTO> jobDTOList = jobList.map(job -> {
	    	//scrappedJobIds(사용자가 스크랩한 jid)에 현 공고의 jid가 포함되어 있는지 확인
	    	boolean isScrapped = scrappedJobIds.contains(job.getJid());
	    	log.info("scrap: " + isScrapped);
	        // JobDTO 생성, 이 때 companyDTO도 설정
	    	// Job 엔티티를 JobDTO로 변환
	        return new JobDTO(
	            job.getJid(),
	            job.getTitle(),
	            job.getOccupation(),
	            job.getSalary(),
	            job.getMinSalary(),
	            job.getMaxSalary(),
	            job.getExperience(),
	            job.getMinDuration(),
	            job.getMaxDuration(),
	            job.getEducation(),
	            job.getQualification(),
	            job.getCertification(),
	            job.getRecruitNum(),
	            job.getEmpType(),
	            job.getPosition(),
	            job.getPostalCode(),
	            extractAddress(job.getAddress01()), //간소화한 주소를 address01에 저장
	            job.getAddress02(),
	            job.getDay(),
	            job.getStartTimeWk(),
	            job.getEndTimeWk(),
	            job.getStartTimeWkE(),
	            job.getEndTimeWkE(),
	            job.getWorkTimeEtc(),
	            job.getDuty(),
	            job.getBenefits(),
	            job.getTimeOff(),
	            job.getAmenities(),
	            job.getAwards(),
	            job.getFacilities(),
	            job.getStartDate(),
	            job.getEndDate(),
	            job.getApplyMtd(),
	            job.getDescription(),
	            job.getFilename(),
	            job.getFilepath(),
	            job.getCreatedDate(),
	            job.getUpdatedDate(),
	            job.getJobHits(),
	            job.getCompany(),
	            isScrapped 
	        );
	    });

	    return jobDTOList;
	}



	public Page<JobDTO> findByJobTitleContaining(String keyword, Pageable pageable, Long mid) {
		int page = pageable.getPageNumber() - 1; //db는 현재페이지보다 1 작아야함
		int pageSize = 10;
		pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "jid");
		
		Page<Job> jobList = jobRepository.findByTitleContaining(keyword, pageable);
		
		// 현재 사용자가 스크랩한 공고의 jid 목록 조회
	    List<Long> scrappedJobIds = scrapRepository.findByMemberMid(mid)
	        .stream()
	        //scrap의 jid를 추출
	        .map(scrap -> scrap.getJob().getJid())
	        .collect(Collectors.toList());

		// Page<Job>를 Page<JobDTO>로 변환
	    Page<JobDTO> jobDTOList = jobList.map(job -> {
	        // 각 Job의 CompanyEntity로부터 CompanyDTO를 조회
	    	//scrappedJobIds(사용자가 스크랩한 jid)에 현 공고의 jid가 포함되어 있는지 확인
	    	boolean isScrapped = scrappedJobIds.contains(job.getJid());
	        // JobDTO 생성, 이 때 companyDTO도 설정
	        return new JobDTO(
	            job.getJid(),
	            job.getTitle(),
	            job.getOccupation(),
	            job.getSalary(),
	            job.getMinSalary(),
	            job.getMaxSalary(),
	            job.getExperience(),
	            job.getMinDuration(),
	            job.getMaxDuration(),
	            job.getEducation(),
	            job.getQualification(),
	            job.getCertification(),
	            job.getRecruitNum(),
	            job.getEmpType(),
	            job.getPosition(),
	            job.getPostalCode(),
	            extractAddress(job.getAddress01()),
	            job.getAddress02(),
	            job.getDay(),
	            job.getStartTimeWk(),
	            job.getEndTimeWk(),
	            job.getStartTimeWkE(),
	            job.getEndTimeWkE(),
	            job.getWorkTimeEtc(),
	            job.getDuty(),
	            job.getBenefits(),
	            job.getTimeOff(),
	            job.getAmenities(),
	            job.getAwards(),
	            job.getFacilities(),
	            job.getStartDate(),
	            job.getEndDate(),
	            job.getApplyMtd(),
	            job.getDescription(),
	            job.getFilename(),
	            job.getFilepath(),
	            job.getCreatedDate(),
	            job.getUpdatedDate(),
	            job.getJobHits(),
	            job.getCompany(),
	            isScrapped 
	        );
	    });

	    return jobDTOList;
	}


	public Page<JobDTO> findByCnameContaining(String keyword, Pageable pageable, Long mid) {
		int page = pageable.getPageNumber() - 1; //db는 현재페이지보다 1 작아야함
		int pageSize = 10;
		pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "jid");
		
		Page<Job> jobList = jobRepository.findByCnameContaining(keyword, pageable);
		
		// 현재 사용자가 스크랩한 공고의 jid 목록 조회
	    List<Long> scrappedJobIds = scrapRepository.findByMemberMid(mid)
	        .stream()
	        //scrap의 jid를 추출
	        .map(scrap -> scrap.getJob().getJid())
	        .collect(Collectors.toList());
		
		// Page<Job>를 Page<JobDTO>로 변환
	    Page<JobDTO> jobDTOList = jobList.map(job -> {
	        // 각 Job의 CompanyEntity로부터 CompanyDTO를 조회
	    	//scrappedJobIds(사용자가 스크랩한 jid)에 현 공고의 jid가 포함되어 있는지 확인
	    	boolean isScrapped = scrappedJobIds.contains(job.getJid());
	        // JobDTO 생성, 이 때 companyDTO도 설정
	        return new JobDTO(
	            job.getJid(),
	            job.getTitle(),
	            job.getOccupation(),
	            job.getSalary(),
	            job.getMinSalary(),
	            job.getMaxSalary(),
	            job.getExperience(),
	            job.getMinDuration(),
	            job.getMaxDuration(),
	            job.getEducation(),
	            job.getQualification(),
	            job.getCertification(),
	            job.getRecruitNum(),
	            job.getEmpType(),
	            job.getPosition(),
	            job.getPostalCode(),
	            extractAddress(job.getAddress01()),
	            job.getAddress02(),
	            job.getDay(),
	            job.getStartTimeWk(),
	            job.getEndTimeWk(),
	            job.getStartTimeWkE(),
	            job.getEndTimeWkE(),
	            job.getWorkTimeEtc(),
	            job.getDuty(),
	            job.getBenefits(),
	            job.getTimeOff(),
	            job.getAmenities(),
	            job.getAwards(),
	            job.getFacilities(),
	            job.getStartDate(),
	            job.getEndDate(),
	            job.getApplyMtd(),
	            job.getDescription(),
	            job.getFilename(),
	            job.getFilepath(),
	            job.getCreatedDate(),
	            job.getUpdatedDate(),
	            job.getJobHits(),
	            job.getCompany(),
	            isScrapped 
	        );
	    });

	    return jobDTOList;
	}

	public Page<JobDTO> findByAddress01Containing(String city, Pageable pageable, Long mid) {
		int page = pageable.getPageNumber() - 1; //db는 현재페이지보다 1 작아야함
		int pageSize = 10;
		pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "jid");
		
		Page<Job> jobList = jobRepository.findByAddress01Containing(city, pageable);
		
		// 현재 사용자가 스크랩한 공고의 jid 목록 조회
	    List<Long> scrappedJobIds = scrapRepository.findByMemberMid(mid)
	        .stream()
	        //scrap의 jid를 추출
	        .map(scrap -> scrap.getJob().getJid())
	        .collect(Collectors.toList());
		
		// Page<Job>를 Page<JobDTO>로 변환
	    Page<JobDTO> jobDTOList = jobList.map(job -> {
	        // 각 Job의 CompanyEntity로부터 CompanyDTO를 조회
	    	//scrappedJobIds(사용자가 스크랩한 jid)에 현 공고의 jid가 포함되어 있는지 확인
	    	boolean isScrapped = scrappedJobIds.contains(job.getJid());
	        // JobDTO 생성, 이 때 companyDTO도 설정
	        return new JobDTO(
	            job.getJid(),
	            job.getTitle(),
	            job.getOccupation(),
	            job.getSalary(),
	            job.getMinSalary(),
	            job.getMaxSalary(),
	            job.getExperience(),
	            job.getMinDuration(),
	            job.getMaxDuration(),
	            job.getEducation(),
	            job.getQualification(),
	            job.getCertification(),
	            job.getRecruitNum(),
	            job.getEmpType(),
	            job.getPosition(),
	            job.getPostalCode(),
	            extractAddress(job.getAddress01()),
	            job.getAddress02(),
	            job.getDay(),
	            job.getStartTimeWk(),
	            job.getEndTimeWk(),
	            job.getStartTimeWkE(),
	            job.getEndTimeWkE(),
	            job.getWorkTimeEtc(),
	            job.getDuty(),
	            job.getBenefits(),
	            job.getTimeOff(),
	            job.getAmenities(),
	            job.getAwards(),
	            job.getFacilities(),
	            job.getStartDate(),
	            job.getEndDate(),
	            job.getApplyMtd(),
	            job.getDescription(),
	            job.getFilename(),
	            job.getFilepath(),
	            job.getCreatedDate(),
	            job.getUpdatedDate(),
	            job.getJobHits(),
	            job.getCompany(),
	            isScrapped 
	        );
	    });

	    return jobDTOList;
	}

	public JobDTO findByJid(Long jid) {
		Optional<Job> findJob = jobRepository.findByJid(jid);
		if(findJob.isPresent()) { //검색한 게시글이 있으면 dto를 컨트롤러로 반환함
			JobDTO jobDTO = JobDTO.toSaveDTO(findJob.get());
			return jobDTO;
		}else { //게시글이 없으면 에러 처리
			throw new BootBoardException("게시글을 찾을 수 없습니다.");
		}
	}
	
	//기업회원이 작성한 공고글 (마이페이지)
	public Page<JobDTO> findListByCid(Pageable pageable, Long cid) {
		int page = pageable.getPageNumber() - 1; //db는 현재페이지보다 1 작아야함
		int pageSize = 10;
		pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "jid");
		
		Page<Job> jobList = jobRepository.findByCompany_Cid(pageable, cid);
		
		log.info("jobList.isFirst()=" + jobList.isFirst());
		log.info("jobList.isLast()=" + jobList.isLast());
		log.info("jobList.getNumber()=" + jobList.getNumber());
		
		// Page<Job>를 Page<JobDTO>로 변환
	    Page<JobDTO> jobDTOList = jobList.map(job -> {
	        return new JobDTO(
	        		 job.getJid(),
	 	            job.getTitle(),
	 	            job.getOccupation(),
	 	            job.getSalary(),
	 	            job.getMinSalary(),
	 	            job.getMaxSalary(),
	 	            job.getExperience(),
	 	            job.getMinDuration(),
	 	            job.getMaxDuration(),
	 	            job.getEducation(),
	 	            job.getQualification(),
	 	            job.getCertification(),
	 	            job.getRecruitNum(),
	 	            job.getEmpType(),
	 	            job.getPosition(),
	 	            job.getPostalCode(),
	 	            extractAddress(job.getAddress01()),
	 	            job.getAddress02(),
	 	            job.getDay(),
	 	            job.getStartTimeWk(),
	 	            job.getEndTimeWk(),
	 	            job.getStartTimeWkE(),
	 	            job.getEndTimeWkE(),
	 	            job.getWorkTimeEtc(),
	 	            job.getDuty(),
	 	            job.getBenefits(),
	 	            job.getTimeOff(),
	 	            job.getAmenities(),
	 	            job.getAwards(),
	 	            job.getFacilities(),
	 	            job.getStartDate(),
	 	            job.getEndDate(),
	 	            job.getApplyMtd(),
	 	            job.getDescription(),
	 	            job.getFilename(),
	 	            job.getFilepath(),
	 	            job.getCreatedDate(),
	 	            job.getUpdatedDate(),
	 	            job.getJobHits(),
	 	            job.getCompany(),
		            job.isScrapped()
	        );
	    });

	    return jobDTOList;
	}


	public boolean scrapJob(Long jid, Long mid) {
		// Job 및 Member 엔티티 조회
	    Optional<Job> job = jobRepository.findByJid(jid);
	    Optional<MemberEntity> member = memberRepository.findByMid(mid);
	    
	    if (job.isEmpty() || member.isEmpty()) {
	        // Job 또는 Member가 존재하지 않는 경우
	        return false;
	    }
		
		// 스크랩 여부 확인
        Optional<Scrap> existingScrap = scrapRepository.findByJobIdAndMemberId(jid, mid);
        if (existingScrap.isPresent()) {
        	// 이미 스크랩한 경우 스크랩 취소 처리
            scrapRepository.delete(existingScrap.get());
            return false;
        }
        
        // 스크랩 처리
        Scrap scrap = new Scrap();
        scrap.setJob(job.get());
        scrap.setMember(member.get());
        scrapRepository.save(scrap);

        return true; // 스크랩 성공
    }
	
	
	 /* 공고메인페이지 검색 */
	 /*public Page<JobDTO> findJobs(String type, String keyword, String city, String sort, Pageable pageable) {
		 // 정렬 조건 설정
	    Sort sortCondition = Sort.by("jid").descending(); // 기본 정렬
	    if ("register".equals(sort)) {
	        sortCondition = Sort.by("createdDate").descending();
	    } else if ("deadline".equals(sort)) {
	        sortCondition = Sort.by("endDate").ascending();
	    } else if ("update".equals(sort)) {
	        sortCondition = Sort.by("updatedDate").descending();
	    }
	    
	    // 페이지 요청 객체에 정렬 조건 적용
	    Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortCondition);

	    // 조건에 맞는 데이터 조회
	    Page<Job> jobs;
	    jobs = jobRepository.findByOptionalCriteria(city, type, keyword, sortedPageable);
	     
	    // Job 엔티티를 JobDTO로 변환
	    return jobs.map(job -> JobDTO.toSaveDTO(job));
		
	}*/
}
	
	

	
	
	
	

	

