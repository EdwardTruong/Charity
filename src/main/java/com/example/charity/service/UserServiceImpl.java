package com.example.charity.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.charity.dao.UserDao;
import com.example.charity.dto.UserDto;
import com.example.charity.entity.User;
import com.example.charity.mapper.UserMapper;
import com.example.charity.util.AppUtils;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserDao userDao;

	@Autowired
	UserMapper userMapper;

	@Autowired
	AppUtils utils;

	@Override
	public User findById(Integer id) {

		Optional<User> result = userDao.findById(id);
		if (result.isPresent()) {
			return result.get();
		}
		return null;
	}

	@Override
	public List<User> findAll() {
		return (List<User>)userDao.findAll();
	}

	@Override
	@Transactional
	public User update(User user) {
		userDao.save(user);
		return user;
	}

	@Override
	@Transactional
	public void deleteUser(User entity) {
		userDao.delete(entity);
	}

	@Override
	public Page<UserDto> findAll(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		Page<User> users = userDao.findAll(pageable);
		return userMapper.toListDto(users);
	}

	
	@Override
	public User findByUserName(String userName) {
		return userDao.getUserByUserName(userName);
	}



	@Override
	public User findByEmail(String email) {
		return userDao.getUserByEmail(email);
	}
	
	
	@Override
	public User getUserByUserNameAndPassword(String userName, String password) {
		User theUser = (User) userDao.getUserByUserName(userName);
		if (theUser != null && theUser.getPassword().equals(password)) {
			return theUser;
		}
		return null;
	}

	@Override
	@Transactional
	public User saveNew(UserDto dto) {
		dto.setStatus(1);
		dto.setCreated(utils.currentDatefomart());
		User newUser = userMapper.toEntity(dto);
		userDao.save(newUser);
		return newUser;
	}

	@Override
	@Transactional
	public User changeStatusUser(int id) {
		Optional<User> result = userDao.findById(id);
		if (!result.isPresent()) {
			return null;
		}
		User theUser = result.get();

		if (theUser.getStatus() == 1) {
			theUser.setStatus(0);
		} else if (theUser.getStatus() == 0) {
			theUser.setStatus(1);
		}
		userDao.save(theUser);
		return theUser;

	}

	@Override
	public List<User> searchingFindUserByEmailOrPhoneNumber(String input) {
		return userDao.searchingFindUserByEmailOrPhoneNumber(input);
	}


	@Override
	public Page<UserDto> findUserByInfo(int pageNumber,int pageSize, String input) {
		Pageable pageable = PageRequest.of(pageNumber-1,pageSize);
		Page<User> entitiesFound = userDao.findByInfo(input, pageable);
		return userMapper.toListDto(entitiesFound);
	}









}
