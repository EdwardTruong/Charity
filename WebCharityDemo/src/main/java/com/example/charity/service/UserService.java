package com.example.charity.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.charity.dto.UserDto;
import com.example.charity.entity.User;

public interface UserService {
	
	List<User> findAll();

	User findById(Integer id);
	
	User findByUserName(String userName);
	
	User findByEmail(String email);

	User saveNew(UserDto dto);

	User update(User user);

	void deleteUser(User entity);

	Page<UserDto> findAll(int pageNo , int pageSize);

	User getUserByUserNameAndPassword(String userName, String password);

	User changeStatusUser(int id);

	List<User> searchingFindUserByEmailOrPhoneNumber(String input);
	
	Page<UserDto> findUserByInfo(int pageNo, int pageSize, String input);
	
}
