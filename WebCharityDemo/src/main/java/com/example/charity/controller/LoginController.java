package com.example.charity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.charity.entity.User;
import com.example.charity.service.UserService;
import com.example.charity.util.AppUtils;

import jakarta.servlet.http.HttpSession;

/*
 *	LoginController is a function for authentication.
 *	
 *	1. The formLogin method is a form for client login page
 *	2. The processForm method is process form.
 *		- I decided to allow users with the admin role to access any urls.
 *		- Users with the user role restrict access url by HttpSession for each method.
 *		- URL access for User role is UserInfoController.class 
 *		and 1 @PostMapping in UserController.class (/home , user/info, user/edit, admin/updateUser)
 */

@Controller
public class LoginController {

	@Autowired
	UserService userService;

	@Autowired
	AppUtils utils;

	@GetMapping("/login")
	public String formLogin(Model theModel) {
		return "login/login";
	}

	// Login form with error
	@PostMapping("/processFormLogin")
	public String processForm(@RequestParam("userName") String userName, @RequestParam("password") String password,
			HttpSession session, RedirectAttributes resoult, Model theModel) {

		theModel.addAttribute("userName", userName);
		theModel.addAttribute("password", password);

		User theUser = userService.getUserByUserNameAndPassword(userName, password);

		if (theUser == null) {
			resoult.addFlashAttribute("error", "Sai tài khoản hoặc mật khẩu.");
			return "redirect:/login";
		}
		if (theUser.getRole().getId() == 1) {
			session.setAttribute("admin", theUser);
			return "redirect:/admin/home";

		}
		session.setAttribute("user", theUser);
		return "redirect:/home";

	}

	@GetMapping("/logout")
	public String logout(Integer userId, Model theModel, HttpSession session) {

		session.invalidate();

		return "redirect:/";
	}
}
