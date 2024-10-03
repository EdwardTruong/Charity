package com.example.charity.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.charity.entity.Donation;

/* 

 * 1.find phoneNumber or organizationName of a donation. The input in searchingFindDonationByInfoInput method 
 * 	is parameter in form
 * 
 * 2.Two methods Page<Donation> foundDonationByInfo and Page<Donation> findDonationByStatus using for testing and update project later. 
 *	
 * 3.The Page<Donation> foundDonationByInfo method find exact objects by entering each field. 
 * 	I commented for reference without using it.
 * 
 * 4.The List<Donation> foundDonationByInfo method find every information of phone number or organization's name or donation's code 
 * 	by 1 word or any words 
 * 
 * 5. The findDonationByStatus method accurately retrieves donations based on the specified status criteria.
 * 
 * BONUTE : I make a file html for result searching donations (admin/donation/search_result.html) already to update project later. 
 */

@Repository
public interface DonationDao extends JpaRepository<Donation, Integer> {

	Donation findByCode(String code);

//	@Query("SELECT d FROM Donation d WHERE CONCAT(d.phoneNumber,' ',d.organizationName,' ',d.code) LIKE %?1%")
//	Page<Donation> foundDonationByInfo(String input, Pageable pageable);

	@Query("SELECT d FROM Donation d WHERE d.phoneNumber LIKE %?1% OR d.organizationName LIKE %?1% OR d.code LIKE %?1%")
	Page<Donation> foundDonationByInfo(String input, Pageable pageable);
	
	@Query("SELECT d FROM Donation d WHERE d.status = :input")
	Page<Donation> findDonationByStatus(@Param("input") Integer input, Pageable pageable);

	@Query("SELECT d FROM Donation d WHERE d.phoneNumber LIKE %?1% OR d.organizationName LIKE %?1% OR d.code LIKE %?1%")
	List<Donation>  foundDonationByInfo(String input);
	
	@Query("SELECT d FROM Donation d WHERE d.status = :input")
	List<Donation> findDonationByStatus(@Param("input") Integer input);
}
