package com.khit.recruit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/rboard")
@Controller
public class RBoardController {
	
	@GetMapping("/list")
	public String boardListForm() {
		return "rboard/list";
	}
	@GetMapping("/detail")
	public String boardDetailForm() {
		return "rboard/detail";
	}
	@GetMapping("/write")
	public String boardWriteForm() {
		return "rboard/write";
	}

}
