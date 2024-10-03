package com.example.charity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.charity.entity.User;
import com.example.charity.exception.UserNotFoundException;
import com.example.charity.service.UserService;
import com.example.charity.util.AppUtils;

import jakarta.servlet.http.HttpSession;

/*
 * In this project users can find their own information and change some their fields information.
 * 
 * 1.The userInfo method is getting user information.
 * 	- I decide the user's password is "text" to easy check with database. (without login form !)
 *  - file.html user can access : public/home, public/detail , admin/user/detail.html , admin/user/edit.html 
 *  - form user can use  @PostMapping : admin/updateUser , /saveNewUserDonation
 */

@Controller
@RequestMapping("/user")
public class UserInfoController {

	@Autowired 
	UserService userService;
	
	@Autowired
	AppUtils utils;
	
	@GetMapping("/info/{id}")
	public String userInfo(@PathVariable("id") Integer userId, Model theModel, HttpSession session) {
		User user = (User) session.getAttribute("user");
		
		if(user.getId() != userId) {
			return "redirect:/accessDenied";
		}
		
		User entity = userService.findById(userId);
		
		if(entity == null) {
			throw new UserNotFoundException("User not found");
		}
			
			session.setAttribute("user", entity);
			theModel.addAttribute("user",entity);
			theModel.addAttribute("user_created", utils.changeDateFormatDMY(entity.getCreated()));
			theModel.addAttribute("pageTitle", "Thông tin người dùng : " + entity.getFullName());
		
		return "admin/user/detail";
	}
	
	@GetMapping("/edit/{id}")
	public String userEdit(@PathVariable("id") Integer userId, Model theModel, HttpSession session) {
		User user = (User) session.getAttribute("user");
		
		if( (user == null)|| user.getId() != userId) {
			return "redirect:/accessDenied";
		}
		
		User entity = userService.findById(userId);
		
		if(entity == null) {
			throw new UserNotFoundException("User not found");
		}
			
			session.setAttribute("user", entity);
			theModel.addAttribute("user",entity);
			theModel.addAttribute("user_created", utils.changeDateFormatDMY(entity.getCreated()));
			theModel.addAttribute("pageTitle", "Thông tin người dùng : " + entity.getFullName());
		
		return "admin/user/edit";
	}
	
}
