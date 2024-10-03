package com.example.charity.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.charity.dto.UserDto;
import com.example.charity.entity.User;
import com.example.charity.entity.UserDonation;
import com.example.charity.exception.UserNotFoundException;
import com.example.charity.mapper.UserMapper;
import com.example.charity.service.DonationService;
import com.example.charity.service.UserDonationService;
import com.example.charity.service.UserService;
import com.example.charity.util.AppUtils;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

/*
 * I set user id = 1 in database is admin in AppUtils.class.
 * 
 * 1. The homeAdmin method is main page to return 2 urls userController and DonationController.
 * 
 * 2. The allUsers method is home page for list users. (database have 7 and no more than 10 entities. 
 * 		Request 01 in https://courses.funix.edu.vn/courses/course-v1:FUNiX+PRJ321x.3.0.VN+0123.DN1/courseware/160c6ece178e4d3b8844a0052930a948/62cb4cb296534460bd418c0a4c1f2d2b/?activate_block_id=block-v1%3AFUNiX%2BPRJ321x.3.0.VN%2B0123.DN1%2Btype%40sequential%2Bblock%4062cb4cb296534460bd418c0a4c1f2d2b)
 * 
 * 3. The showFormAddNewUser method is a form for making new user
 * 
 * 4. The pages method is pagination of users have in databases. 
 * 
 * 5. The processAddNewUser method is CREATE a new user (entity) and using validation function (@PostMapping)
 * 
 * 6. The detailUser method is READ a user (entity)
 * 
 * 7. The editUser method is a view page to see detail and it can edit a user
 * 
 * 8. The updateUser method is UPDATE user and it have validation function (@PostMapping) 
 * 
 * 9. The changeStatutToLock method is a function of admin role. It lock or unlock (change status 1 to 0 or 0 to 1 ) user
 * 		- Users have user role can't change field status by themself.
 * 
 * 10.The deleteUser method is DELETE a user (entity) .
 * 	- UserDonation (entity) have field user_id : same id of user will be delete too.
 * 	- Donation (entity) nothing change. If user donated and admin accepted then the field money in the donation (entity) 
 * 		still update.
 * 	- Can't delete user have role : admin.
 * 
 * BONUTE : - In the pages method a function find a user by e-mail or phone number was done. (using 1 word or words) 
 */

@Controller
@RequestMapping("/admin")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	UserMapper userMapper;

	@Autowired
	DonationService dService;

	@Autowired
	UserDonationService uDService;

	@Autowired
	AppUtils util;

	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	
	@GetMapping("/home")
	public String homeAdmin(HttpSession session) {
		return "admin/home";
	}

	@GetMapping("/users")
	public String allUsers(Model theModel, @Param("input") String input, HttpSession session) {
		util.setUserAdminToTest(session, userService);

		User user = (User) session.getAttribute("user");
		User admin = (User) session.getAttribute("admin");

		if (admin == null && (user == null || user.getRole().getId() != 1)) {
			return "login/access_denied";
		}

		return pages(theModel, 1, input, session);
	}

	@GetMapping("/showFormAddNewUser")
	public String showFormAddNewUser(Model theModel) {

		theModel.addAttribute("newUser", new UserDto());

		if (AppUtils.allUserInDatabase >= 10) {
			theModel.addAttribute("max", "Số user đã tạo đạt giới hạn là 10. Hãy xóa bớt user để thêm mới !");
		}

		return "admin/user/new";
	}

	@GetMapping("/pageUser/{pageUserNumber}")
	public String pages(Model theModel, @PathVariable("pageUserNumber") int currentPage, @Param("input") String input,
			HttpSession session) {

		User user = (User) session.getAttribute("user");
		User admin = (User) session.getAttribute("admin");

		if (admin == null && (user == null || user.getRole().getId() != 1)) {
			return "login/access_denied";
		}

		util.setUserAdminToTest(session, userService);

		int pageSize = 5;
		int totalPages;
		int totalItems;
		long all;
		int firstObject;
		String empty = "";
		Page<UserDto> pages = userService.findAll(currentPage, pageSize);
		theModel.addAttribute("newUser", new UserDto());
		AppUtils.allUserInDatabase = pages.getTotalElements();

		if (AppUtils.allUserInDatabase >= 10) {
			theModel.addAttribute("max", true);
		} else {
			theModel.addAttribute("max", false);
		}

		if (input == null || input.equals(empty)) {

			totalPages = pages.getTotalPages();
			totalItems = pages.getNumberOfElements();
			all = pages.getTotalElements();
			firstObject = (int) pages.getPageable().getOffset();

			List<UserDto> users = pages.getContent();
			theModel.addAttribute("page", true);
			theModel.addAttribute("users", users);
			theModel.addAttribute("totalPages", totalPages);
			theModel.addAttribute("totalItems", totalItems);
			theModel.addAttribute("currentUserPage", currentPage);
			theModel.addAttribute("firstObjectOnPage", firstObject + 1);
			theModel.addAttribute("lastObjectOnPage", totalItems + firstObject);
			theModel.addAttribute("all", all);

		} else if (input != null) {
			theModel.addAttribute("input", input);
			List<User> listFound = userService.searchingFindUserByEmailOrPhoneNumber(input);
			List<UserDto> dtoFound = userMapper.toListDto(listFound);
			theModel.addAttribute("users", dtoFound);
			if(dtoFound.size() == 0) {				
				theModel.addAttribute("empty","Không tìm thấy kết quả.");
			}
		}

		return "admin/user/users";
	}

	@PostMapping("/processAddNewUser")
	public String processAddNewUser(@Valid @ModelAttribute("newUser") UserDto userDto, BindingResult result,
			HttpSession session, Model theModel, RedirectAttributes redirect) {

		if (AppUtils.allUserInDatabase >= 10) {
			theModel.addAttribute("error", "Số lượng user đã vượt quá số lượng. Hãy xóa bớt user !");
			return pages(theModel, 1, null, session);
		}

		if (result.hasErrors()) {
			return "admin/user/new";
		}
		if (userService.findByUserName(userDto.getUserName()) != null) {
			theModel.addAttribute("error", "Username đã tồn tại hãy chọn username khác.");
			return "admin/user/new";
		}
		if (userService.findByEmail(userDto.getEmail()) != null) {
			theModel.addAttribute("error", "Email đã tồn tại hãy chọn địa chỉ email khác.");
			return "admin/user/new";
		}

		userService.saveNew(userDto);

		redirect.addFlashAttribute("success", "Đã thêm tài khoản mới " + userDto.getEmail() + " .");
		return "redirect:/admin/users";

	}

	@GetMapping("/detailUser/{id}")
	public String detailUser(@PathVariable("id") Integer userId, Model theModel, HttpSession session) {

		User user = (User) session.getAttribute("user");
		User admin = (User) session.getAttribute("admin");

		if (admin == null && (user == null || user.getRole().getId() != 1)) {
			return "login/access_denied";
		}

		util.setUserAdminToTest(session, userService);

		User theUser = userService.findById(userId);

		if (theUser == null) {
			throw new UserNotFoundException("User not found");
		}

		theModel.addAttribute("user", theUser);
		theModel.addAttribute("user_created", util.changeDateFormatDMY(theUser.getCreated()));
		theModel.addAttribute("pageTitle", "Thông tin người dùng : " + theUser.getFullName());

		return "admin/user/detail";
	}

	@GetMapping("/editUser/{id}")
	public String editUser(@PathVariable("id") Integer userId, Model theModel, HttpSession session) {

		User user = (User) session.getAttribute("user");
		User admin = (User) session.getAttribute("admin");

		if (admin == null && (user == null || user.getRole().getId() != 1)) {
			return "login/access_denied";
		}

		util.setUserAdminToTest(session, userService);

		User entity = userService.findById(userId);

		if (entity == null) {
			throw new UserNotFoundException("User not found");
		}

		theModel.addAttribute("user", entity);
		theModel.addAttribute("pageTitle", "Chỉnh sữa - " + entity.getUserName());
		return "admin/user/edit";
	}

	@PostMapping("/updateUser")
	public String updateUser(@Valid @ModelAttribute("user") User user, BindingResult result,
			RedirectAttributes redirect, HttpSession session) {

		if (result.hasErrors()) {
			return "admin/user/edit";
		}
		
		for(UserDonation d : user.getUsersDonation()) {
			logger.info(d.toString());
		}
		
		userService.update(user);
		for(UserDonation d : user.getUsersDonation()) {
			logger.info(d.toString());
		}
		
		user.getUsersDonation();

		
		redirect.addFlashAttribute("message", "Chỉnh sữa người dùng thành công");

		User currentUser = (User) session.getAttribute("user");
		if (currentUser != null) {
			return "redirect:/user/info/" + user.getId();
		}

		return "redirect:/admin/detailUser/" + user.getId();
	}

	@PostMapping("/changeStatutToLock/{id}")
	public String changeStatutToLock(@PathVariable(value = "id") Integer id, RedirectAttributes redirect,
			Model theModel) {

		User theUser = userService.changeStatusUser(id);

		String status = "hoạt động";
		if (theUser.getStatus() == 0) {
			status = "khóa";
		}

		redirect.addFlashAttribute("success",
				"Thay đổi trạng thái người dùng " + theUser.getFullName() + " thành " + status);

		return "redirect:/admin/users";
	}

	@GetMapping("/deleteUser/{id}")
	public String deleteUser(@PathVariable(value = "id") Integer id, RedirectAttributes ra, Model theModel,
			HttpSession session) {

		User user = (User) session.getAttribute("user");
		User admin = (User) session.getAttribute("admin");

		if (admin == null && (user == null || user.getRole().getId() != 1)) {
			return "login/access_denied";
		}

		User entity = userService.findById(id);

		if (entity == null) {
			throw new UserNotFoundException("User not found");
		}

		userService.deleteUser(entity);

		ra.addFlashAttribute("success", "Người dùng có id : " + id + " đã bị xóa.");

		return "redirect:/admin/users";
	}
}
