package com.khit.recruit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@RequestMapping("/job")
@Controller
public class JobController {

	@GetMapping("/job_main")
	public String job_mainForm() {
		return "job/job_main";
	}
	
	@GetMapping("/job_detail")
	public String job_detailForm() {
		return "job/job_detail";
	}
	
	@GetMapping("/job_write")
	public String job_writeForm() {
		return "job/job_write";
	}
}
