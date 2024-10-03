package com.example.charity.util;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.example.charity.entity.User;
import com.example.charity.service.UserService;

import jakarta.servlet.http.HttpSession;

@Component
public class AppUtils {
	
	public static long allUserInDatabase  ;
	
	public static long allDonationInDatabase;
	
	public static long allUserDonatedInDatabase;
	
	public String currentDatefomart() {
		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		String formattedDate = myDateObj.format(myFormatObj);
		return formattedDate;
	}

	public String changeDateFormatDMY(String dateTypeString) {
		LocalDate date = LocalDate.parse(dateTypeString);
		DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String reversedDate = date.format(outputFormat);
		return reversedDate;
	}

	public void setUserAdminToTest(HttpSession session, UserService userService) {

		long currentTime = System.currentTimeMillis();
		long creationTime = session.getCreationTime();
		int maxInactiveInterval = session.getMaxInactiveInterval();
		long sessionDuration = (currentTime - creationTime) / 1000;
		long timeToLive = maxInactiveInterval - sessionDuration;

		if (timeToLive < 20) {
			User user = (User) session.getAttribute("admin");

			if (user == null) {
				user = userService.findById(1);
			}

			session.setAttribute("admin", user);
		}

	}

	
	public void checkInHttpSession(HttpSession session) {

		long currentTime = System.currentTimeMillis();
		long creationTime = session.getCreationTime();
		int maxInactiveInterval = session.getMaxInactiveInterval();
		long sessionDuration = (currentTime - creationTime) / 1000;
		long timeToLive = maxInactiveInterval - sessionDuration;

		System.out.println(" INFO : ================ " + "\n currentTime :" + currentTime + "\n creationTime : "
				+ creationTime + "\n maxInactiveInterval : " + maxInactiveInterval + "\n sessionDuration : "
				+ sessionDuration + "\n timeToLive : " + timeToLive);
	}

	public String convertToVND(long amount) {
		
		if(amount >0) {
			DecimalFormat formatter = new DecimalFormat("###,###,###,### VND");
			return formatter.format(amount);
		}
		return "0 VND";
		
	}

	public boolean checkInputDate(String inputStartDate, String inputEndDate) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");

		LocalDate dateStart = LocalDate.parse(inputStartDate, formatter);

		LocalDate dateEnd = LocalDate.parse(inputEndDate, formatter);

		return dateStart.isAfter(dateEnd);
	}
	


}
