package com.khit.recruit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/board")
@Controller
public class BoardController {
	
	@GetMapping("/list")
	public String boardListForm() {
		return "board/list";
	}
	@GetMapping("/detail")
	public String boardDetailForm() {
		return "board/detail";
	}
	@GetMapping("/write")
	public String boardWriteForm() {
		return "board/write";
	}
	
	
}
