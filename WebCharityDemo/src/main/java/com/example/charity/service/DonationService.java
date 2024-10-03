package com.example.charity.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.charity.dto.DonationDto;
import com.example.charity.entity.Donation;

public interface DonationService {

	Donation findById(Integer id);
	
	Donation findByCode(String code);
	
	Donation saveNew(DonationDto dto); 
	
	Donation update(Donation entity);

	void deleteDonation(Donation entity);

	public List<Donation> findAll();

	Page<DonationDto> findAll(int pageNo , int pageSize);
			
	Page<DonationDto> findDonationByInfo(int pageNumber,int pageSize, String input);
	
	List<DonationDto> findDonationByInfo(String input);
}
