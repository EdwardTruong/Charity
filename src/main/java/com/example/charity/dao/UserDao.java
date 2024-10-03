package com.example.charity.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.example.charity.entity.User;

/* 

 * 1. getUserByUserName and getUserByEmail to check information when using login and register new user 
 * 	
 * 
 * 2. The searchingFindUserByEmailOrPhoneNumber method using to find user of user have admin role
 * 		
 * 3. The findByInfo method using for testing and update project later. Found user and pagination
 * 	 It doesn't use for anythings in website. 
 * 
 */



@Repository
public interface UserDao extends JpaRepository<User, Integer>, PagingAndSortingRepository<User, Integer> {

	User getUserByUserName(String userName);
	
	User getUserByEmail(String email);
	
	@Query("SELECT u FROM User u WHERE u.email LIKE %?1% OR u.phoneNumber LIKE %?1%")
	List<User> searchingFindUserByEmailOrPhoneNumber(String input);
	
	@Query("SELECT u FROM User u WHERE u.email LIKE %?1% OR u.phoneNumber LIKE %?1%")
	Page<User> findByInfo(String keyword, Pageable pageable);
}
