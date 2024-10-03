package com.example.charity.controller;

import java.util.List;

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
 * First at all I set HttpSession for check each any role so 
 * 	for each @GetMapping i use HttpSession.getAttribute to do it.
 * 1. The index method is home page for list donations.
 * 2. The listPage method is pagination of donations.
 * 
 * 3. The formAddNewDonation method is show a form to register a new donation.
 * 4. The addNewDonation method is CREATE a new donation (entity) has validation function and no more than 10 entities in
 * 		databases (@PostMapping).
 * 
 * 5. The detail method is READ donation (entity) and it allows the admin to see details of a specific donation, 
 * 		including information about the user and providing pagination functionality.
 * 6. The editDonation method is UPDATE donation (entity) and it allows admin to active of a specific donation,
 * 		including information about the user and providing pagination functionality.
 * 
 * 7. The updateDonation method is update informations of donation without money donated  (@PostMapping)
 * 8. The deleteDonation method is DELETE a donation entity. 
 * 		WHEN delete a donation(entity) every user_donation(entity) have donation_id same id of 
 * 		the donation will be delete too but no for user(entity)
 * 
 * 9. The updateMoneyDonatedSuccess method is update Money. It change status 0 to 1 and count donated into the donation.
 * 10. The deniedDonated method is changing status 0 to 2 of user_donation entity and not count donated into donation
 * 
 * 11. The deleteUserDonation method is DELETE a userDonation entity and update(minus money if admin accepted - field status is 1 ) 
 * 		money of donation then user donated 
 * 
 * BONUTE : function find donation by info include : code or phone number or name of organization or status ; 
 * 		was working.(using 1 word or words) 
 * DONE ! 
 */

@Controller
@RequestMapping("/admin")
public class DonationController {

	@Autowired
	DonationService dService;

	@Autowired
	UserService userService;

	@Autowired
	AppUtils utils;

	@Autowired
	UserDonationService uDService;

	@GetMapping("/donations")
	public String index(Model theModel, @Param("input") String input, HttpSession session) {

		User user = (User) session.getAttribute("user");
		User admin = (User) session.getAttribute("admin");

		if (admin == null && (user == null || user.getRole().getId() != 1)) {
			return "login/access_denied";
		}

		utils.setUserAdminToTest(session, userService);

		return listPage(theModel, 1, session, input);
	}

	@GetMapping("/pageDonation/{pageDonationNumber}")
	public String listPage(Model theModel, @PathVariable("pageDonationNumber") int currentPage, HttpSession session,
			@Param("input") String input) {

		User user = (User) session.getAttribute("user");
		User admin = (User) session.getAttribute("admin");

		if (admin == null && (user == null || user.getRole().getId() != 1)) {
			return "login/access_denied";
		}

		AppUtils.allDonationInDatabase = dService.findAll().size();

		if (AppUtils.allDonationInDatabase >= 10) {
			theModel.addAttribute("max", true);
		} else {
			theModel.addAttribute("max", false);
		}

		int pageSize = 5;
		int totalPages;
		int totalItems;
		long all;
		int firstObject;
		String empty = "";
		theModel.addAttribute("newDonation", new DonationDto());

		if (input == null || input.equals(empty)) {
			Page<DonationDto> pages = dService.findAll(currentPage, pageSize);
			List<DonationDto> donations = pages.getContent();

			totalPages = pages.getTotalPages();
			totalItems = pages.getNumberOfElements();
			firstObject = (int) pages.getPageable().getOffset();
			all = pages.getTotalElements();
			firstObject = (int) pages.getPageable().getOffset();

			theModel.addAttribute("page", true);
			theModel.addAttribute("donations", donations);
			theModel.addAttribute("totalDonationPages", totalPages);
			theModel.addAttribute("firstObjectOnPage", firstObject + 1);
			theModel.addAttribute("lastObjectOnPage", totalItems + firstObject);
			theModel.addAttribute("currentDonationPage", currentPage);
			theModel.addAttribute("all", all);

		} else {

			theModel.addAttribute("input", input);
			List<DonationDto> result = dService.findDonationByInfo(input);
			theModel.addAttribute("donations", result);
			
			if(result.size() == 0) {				
				theModel.addAttribute("empty","Không tìm thấy kết quả.");
			}
			
		}

		return "admin/donation/donations";
	}

	@GetMapping("/formAddNewDonation")
	public String formAddNewDonation(Model theModel) {
		theModel.addAttribute("newDonation", new DonationDto());

		if (AppUtils.allDonationInDatabase >= 10) {
			theModel.addAttribute("max", "Số donation đã tạo đạt giới hạn là 10. Hãy xóa bớt user để thêm mới !");
		}

		return "admin/donation/new";
	}

	@PostMapping("/addNewDonation")
	public String addNewDonation(@Valid @ModelAttribute("newDonation") DonationDto donationDto,
			BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, HttpSession session) {

		if (AppUtils.allDonationInDatabase >= 10) {
			model.addAttribute("error", "Số donation entity đã vượt quá số lượng. Hãy xóa bớt user !");
			return listPage(model, 1, session, null);
		}

		if (bindingResult.hasErrors()) {
			return "admin/donation/new";
		}

		if (utils.checkInputDate(donationDto.getStartDate(), donationDto.getEndDate())) {
			model.addAttribute("error", "Sai ngày bắt đầu và kết thúc");
			return "admin/donation/new";
		}

		if (dService.findByCode(donationDto.getCode()) != null) {
			model.addAttribute("error", "Code đã tồn tại hãy chọn code khác.");
			return "admin/donation/new";
		}

		dService.saveNew(donationDto);
		redirectAttributes.addFlashAttribute("message", "Thêm mới đợi quyên góp thành công.");
		return "redirect:/admin/donations";
	}

	@GetMapping("/detailDonation/{id}")
	public String detail(@PathVariable(value = "id") Integer donationId,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo, Model theModel, HttpSession session) {

		User user = (User) session.getAttribute("user");
		User admin = (User) session.getAttribute("admin");

		if (admin == null && (user == null || user.getRole().getId() != 1)) {
			return "login/access_denied";
		}

		utils.setUserAdminToTest(session, userService);

		Donation theDonation = dService.findById(donationId);

		System.out.println("Check in form detail : ");

		if (theDonation == null) {
			throw new DonationNotFoundException("Donation not found !");
		}

		int pageSize = 5;

		Page<UserDonationDto> userDonations = uDService.findByDonationIdAndPaginate(donationId, pageNo, pageSize);
		int totalPages = userDonations.getTotalPages();
		int totalItems = userDonations.getNumberOfElements();
		theModel.addAttribute("userDonations", userDonations);
		theModel.addAttribute("donation", theDonation);
		theModel.addAttribute("totalPages", totalPages);
		if (userDonations.isEmpty()) {
			theModel.addAttribute("isEmpty", true);
		} else {
			theModel.addAttribute("isEmpty", false);
			theModel.addAttribute("currentPage", pageNo);
			theModel.addAttribute("totalItem", totalItems);
		}

		theModel.addAttribute("donated", utils.convertToVND(theDonation.getMoney()));
		theModel.addAttribute("startDate", utils.changeDateFormatDMY(theDonation.getStartDate()));
		theModel.addAttribute("endDate", utils.changeDateFormatDMY(theDonation.getEndDate()));

		theModel.addAttribute("pageTitle", "Thông tin đợi quyên góp : " + theDonation.getName());

		return "admin/donation/detail";
	}

	@PostMapping("/donationSave")
	public String updateDonation(@Valid @ModelAttribute("donation") Donation donation, BindingResult bindingResult,
			Model theModel, RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors() || utils.checkInputDate(donation.getStartDate(), donation.getEndDate())) {
			int pageSize = 5;

			Page<UserDonationDto> userDonations = uDService.findByDonationIdAndPaginate(donation.getId(), 1, pageSize);
			int totalPages = userDonations.getTotalPages();

			theModel.addAttribute("userDonations", userDonations);
			theModel.addAttribute("userDonations", userDonations);
			theModel.addAttribute("totalPages", totalPages);
			if (userDonations.isEmpty()) {
				theModel.addAttribute("isEmpty", true);
			} else {
				theModel.addAttribute("isEmpty", false);
				theModel.addAttribute("currentPage", 1);
				int totalItems = userDonations.getNumberOfElements();
				theModel.addAttribute("totalItem", totalItems);
			}
			theModel.addAttribute("error", "Sai ngày bắt đầu và kết thúc");
			return "admin/donation/edit";
		}

		dService.update(donation);
		redirectAttributes.addFlashAttribute("message", "Cập nhật thông tin thành công");
		return "redirect:/admin/detailDonation/" + donation.getId();
	}

	@GetMapping("/editDonation/{id}")
	public String editDonation(@PathVariable("id") Integer donationId,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo, Model theModel, HttpSession session) {

		User user = (User) session.getAttribute("user");
		User admin = (User) session.getAttribute("admin");

		if (admin == null && (user == null || user.getRole().getId() != 1)) {
			return "login/access_denied";
		}

		utils.setUserAdminToTest(session, userService);

		Donation theDonation = dService.findById(donationId);

		if (theDonation == null) {
			throw new DonationNotFoundException("Donation not found !");
		}

		int pageSize = 5;

		Page<UserDonationDto> userDonations = uDService.findByDonationIdAndPaginate(donationId, pageNo, pageSize);
		int totalPages = userDonations.getTotalPages();

		theModel.addAttribute("userDonations", userDonations);
		theModel.addAttribute("donation", theDonation);
		theModel.addAttribute("totalPages", totalPages);
		if (userDonations.isEmpty()) {
			theModel.addAttribute("isEmpty", true);
		} else {
			theModel.addAttribute("isEmpty", false);
			theModel.addAttribute("currentPage", pageNo);
			int totalItems = userDonations.getNumberOfElements();
			theModel.addAttribute("totalItem", totalItems);
		}
		theModel.addAttribute("userDonations", userDonations);

		theModel.addAttribute("donated", utils.convertToVND(theDonation.getMoney()));

		theModel.addAttribute("pageTitle", "Cập nhật đợt quyên góp : " + theDonation.getName());

		theModel.addAttribute("donation", theDonation);

		theModel.addAttribute("usersDonationList", theDonation.getUsersDonation());

		return "admin/donation/edit";
	}

	@GetMapping("/deleteDonation/{id}")
	public String deleteDonation(@PathVariable("id") Integer donationId, Model theModel, RedirectAttributes redirect,
			HttpSession session) {

		User user = (User) session.getAttribute("user");
		User admin = (User) session.getAttribute("admin");

		if (admin == null && (user == null || user.getRole().getId() != 1)) {
			return "login/access_denied";
		}

		Donation theDonation = dService.findById(donationId);

		if (theDonation == null) {
			throw new DonationNotFoundException("Donation not found !");
		}

		dService.deleteDonation(theDonation);

		redirect.addFlashAttribute("message", "Đã xóa đợt quyên góp : " + theDonation.getName() + " thành công.");

		return "redirect:/admin/donations";
	}

	@PostMapping("/updateDonated")
	public String updateMoneyDonatedSuccess(@RequestParam("userDonationId") Integer userDonationId,
			RedirectAttributes redirectAttributes) {

		UserDonation ud = uDService.findById(userDonationId);
		ud.setStatus(1);
		Donation currentDonation = ud.getDonation();
		long currentMoney = currentDonation.getMoney();
		long donated = ud.getMoney();
		long updateMoney = currentMoney + donated;
		currentDonation.setMoney(updateMoney);
		dService.update(currentDonation);
		uDService.update(ud);

		redirectAttributes.addFlashAttribute("success",
				"Cập nhật tiền cho quyên góp thành công :" + " + " + utils.convertToVND(donated));
		return "redirect:/admin/editDonation/" + currentDonation.getId();
	}

	@PostMapping("/deniedDonated")
	public String deniedDonated(@RequestParam("userDonationId") Integer userDonationId,
			RedirectAttributes redirectAttributes) {

		UserDonation ud = uDService.findById(userDonationId);
		long donated = ud.getMoney();
		ud.setStatus(2);
		Donation currentDonation = ud.getDonation();
		uDService.update(ud);

		redirectAttributes.addFlashAttribute("success", "Hủy quyên góp với tiền : " + utils.convertToVND(donated));
		return "redirect:/admin/editDonation/" + currentDonation.getId();
	}

	@PostMapping("/deleteUserDonation")
	public String deleteUserDonation(@RequestParam("userDonationId") Integer userDonationId,
			RedirectAttributes redirectAttributes) {

		UserDonation ud = uDService.findById(userDonationId);
		String name = ud.getName();
		Donation currentDonation = ud.getDonation();
		long donated = ud.getMoney();
		if (ud.getStatus() == 1) {
			long currentMoney = currentDonation.getMoney();
			currentDonation.setMoney(currentMoney - donated);
			dService.update(currentDonation);
		}
		redirectAttributes.addFlashAttribute("success",
				"Đã xóa quyên góp của nhà hảo tâm " + name + " với tiền : " + utils.convertToVND(donated));

		uDService.delete(ud);
		return "redirect:/admin/editDonation/" + currentDonation.getId();
	}
}
