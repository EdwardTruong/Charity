package com.example.charity.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.charity.entity.UserDonation;

public interface UserDonationtDao extends JpaRepository<UserDonation, Integer>  {
	
	Page<UserDonation> findByDonationId(Integer donationId, Pageable pageable);
}
