package com.example.charity.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.charity.dto.UserDonationDto;
import com.example.charity.entity.UserDonation;

public interface UserDonationService {
	List<UserDonation> findAll();

	UserDonation findById(Integer id);
	
	void delete(UserDonation ud);

	UserDonation update(UserDonation entity);

	UserDonation save(UserDonationDto dto);

	Page<UserDonationDto> findByDonationIdAndPaginate(Integer donationId, int pageNo, int pageSize);

}
