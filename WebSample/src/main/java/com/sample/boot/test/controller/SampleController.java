package com.sample.boot.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SampleController {
	@RequestMapping(path = "/")
	public String hello(Model model) {
		System.out.println("asdf");
		model.addAttribute("hello", "ooooooppppppp");
		return "home";
	}
}
