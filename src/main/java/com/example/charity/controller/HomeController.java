package com.example.charity.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.charity.dto.DonationDto;
import com.example.charity.dto.UserDonationDto;
import com.example.charity.entity.Donation;
import com.example.charity.entity.User;
import com.example.charity.entity.UserDonation;
import com.example.charity.exception.DonationNotFoundException;
import com.example.charity.service.DonationService;
import com.example.charity.service.UserDonationService;
import com.example.charity.service.UserService;
import com.example.charity.util.AppUtils;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

/*
 * First at all I set HttpSession for user if they are login succeed and then i check each any role of users. 
 * 	I use HttpSession.getAttribute("user") or HttpSession.getAttribute("admin") or both for each @GetMapping.
 * 
 * 1.The home method retrieves all donations from the database, converts them into DonationDto objects, 
 * and sends the paginated results to the client.
 * 
 * 2.The donationDetail method is getting information of a donation.
 * 
 * 3.The saveNewUserDonation method is CREATE new user_donation (entity) has validation 
 * 	and of course user need to login succeed and field status is '1' (hoạt động). (@PostMapping) 
 * 
 * 4.The accessDeniedPage method is access denied page. If user wasn't  admin role 
 * 		or HttpSession.getMaxInactiveInterval() = 0 (time off) or some some exceptions
 *  	they going to see access denied page.
 *  
 *  BONUTE : i set timeout of HttpSession :  server.servlet.session.timeout=900s in application.properties.
 *  
 *  DONE !
 */

@Controller
public class HomeController {

	@Autowired
	DonationService dService;

	@Autowired
	UserDonationService uDService;

	@Autowired
	UserService uService;

	@Autowired
	AppUtils utils;

	boolean isHomePage = true;

	@GetMapping({ "/", "/home" })
	public String homePage(Model theModel, HttpSession session) {

		User admin = (User) session.getAttribute("admin");
		if (admin != null) {
			session.setAttribute("admin", admin);
			theModel.addAttribute("admin",admin);

		}

		isHomePage = true;

		return listPage(theModel, 1);
	}

	@GetMapping({ "/page/{pageNumber}" })
	public String listPage(Model theModel, @PathVariable("pageNumber") int currentPage) {

		isHomePage = true;

		int pageSize = 5;

		Page<DonationDto> pages = dService.findAll(currentPage, pageSize);
		List<DonationDto> donations = pages.getContent();

		int totalPages = pages.getTotalPages();
		int totalItems = pages.getNumberOfElements();

		theModel.addAttribute("donations", donations);
		theModel.addAttribute("userDonation", new UserDonationDto());

		theModel.addAttribute("currentPage", currentPage);
		theModel.addAttribute("totalPages", totalPages);
		theModel.addAttribute("totalItem", totalItems);
		return "public/home";
	}

	@GetMapping({ "/detail/{id}" })
	public String donationDetail(@PathVariable("id") Integer donationId,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo, Model theModel, HttpSession session) {

		isHomePage = false;
		
		User curentUser = (User) session.getAttribute("user");
		User admin = (User) session.getAttribute("admin");
		
		if(curentUser == null) {
			curentUser = admin;
		}

		Donation donation = dService.findById(donationId);

		if (donation == null) {
			throw new DonationNotFoundException("Donation not found !");
		}

		int pageSize = 5;

		Page<UserDonationDto> userDonations = uDService.findByDonationIdAndPaginate(donationId, pageNo, pageSize);

		if (userDonations.isEmpty()) {
			theModel.addAttribute("isEmpty", true);
		} else {
			theModel.addAttribute("isEmpty", false);
			theModel.addAttribute("userDonations", userDonations);

			int totalPages = userDonations.getTotalPages();
			int totalItems = userDonations.getNumberOfElements();

			theModel.addAttribute("currentPage", pageNo);
			theModel.addAttribute("totalPages", totalPages);
			theModel.addAttribute("totalItem", totalItems);
		}

		theModel.addAttribute("donation", donation);
		theModel.addAttribute("userDonation", donation.getUsersDonation());
		theModel.addAttribute("newUserDonation", new UserDonationDto());
		theModel.addAttribute("donated", utils.convertToVND(donation.getMoney()));
		theModel.addAttribute("startDate", utils.changeDateFormatDMY(donation.getStartDate()));
		theModel.addAttribute("endDate", utils.changeDateFormatDMY(donation.getEndDate()));

		return "public/detail";
	}

	@PostMapping("/saveUserDonation")
	public String saveNewUserDonation(@Valid @ModelAttribute("newUserDonation") UserDonationDto dto,
			BindingResult bindingResult, Model theModel, RedirectAttributes redirectAttributes, HttpSession session) {

		Donation donation = dService.findById(dto.getDonationId());

		User curentUser = (User) session.getAttribute("user");
		User admin = (User) session.getAttribute("admin");
		
		if (curentUser == null && admin == null) {
			return "redirect:/accessDenied";
		}
		
		if(curentUser == null) {
			curentUser = admin;
		}

		if (curentUser.getStatus() == 0) {
			redirectAttributes.addFlashAttribute("error",
					"Trạng thái người dùng đã bị khóa không thể quyên góp. Liên hệ với admin để thay đổi trạng thái hoạt động .");

			if (isHomePage == true) {
				return "redirect:/home";
			}
			return "redirect:/detail/" + dto.getDonationId();
		}

		AppUtils.allUserDonatedInDatabase = uDService.findAll().size();

		if (AppUtils.allUserDonatedInDatabase >= 10) {
			redirectAttributes.addFlashAttribute("error",
					"Số nhà hảo tâm đã quyên góp đạt tối đa cho toàn bộ website : 10. Làm ơn hãy đợi admin "
							+ "cập nhật để quyên góp.");

			if (isHomePage == true) {
				return "redirect:/home";
			}
			return "redirect:/detail/" + dto.getDonationId();
		}

		if (bindingResult.hasErrors()) {

			int pageSize = 5;
			int pageNo = 1;

			Page<UserDonationDto> userDonations = uDService.findByDonationIdAndPaginate(donation.getId(), pageNo,
					pageSize);

			if (userDonations.isEmpty()) {
				theModel.addAttribute("isEmpty", true);
			} else {
				theModel.addAttribute("isEmpty", false);
				theModel.addAttribute("userDonations", userDonations);

				int totalPages = userDonations.getTotalPages();
				int totalItems = userDonations.getNumberOfElements();

				theModel.addAttribute("currentPage", pageNo);
				theModel.addAttribute("totalPages", totalPages);
				theModel.addAttribute("totalItem", totalItems);
			}

			theModel.addAttribute("donation", donation);
			theModel.addAttribute("userDonation", donation.getUsersDonation());
			theModel.addAttribute("newUserDonation", new UserDonationDto());
			theModel.addAttribute("donated", utils.convertToVND(donation.getMoney()));
			theModel.addAttribute("startDate", utils.changeDateFormatDMY(donation.getStartDate()));
			theModel.addAttribute("endDate", utils.changeDateFormatDMY(donation.getEndDate()));

			theModel.addAttribute("newUserDonation", dto);
			theModel.addAttribute("error", "Quyên góp không thành công.");
			theModel.addAttribute("errorMoney", bindingResult.getFieldError("money"));
			theModel.addAttribute("errorText", bindingResult.getFieldError("text"));
			theModel.addAttribute("errorName", bindingResult.getFieldError("name"));
			return "public/detail";
		}

		UserDonation udEntity = uDService.save(dto);
		udEntity.setDonation(donation);
		udEntity.setUser(curentUser);
		uDService.update(udEntity);

		redirectAttributes.addFlashAttribute("success", "Đã quyên góp vào " + donation.getName() + " số tiền "
				+ utils.convertToVND(Long.parseLong(dto.getMoney())) + "." + " Đợi ADMIN xác nhận.");

		if (isHomePage == true) {
			return "redirect:/home";
		}
		return "redirect:/detail/" + dto.getDonationId();
	}

	@GetMapping("/accessDenied")
	public String accessDeniedPage() {
		return "login/access_denied";
	}
}
