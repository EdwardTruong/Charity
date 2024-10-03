package com.example.charity.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.charity.dao.UserDonationtDao;
import com.example.charity.dto.UserDonationDto;
import com.example.charity.entity.UserDonation;
import com.example.charity.mapper.UserDonationMapper;
import com.example.charity.util.AppUtils;

import jakarta.transaction.Transactional;

@Service
public class UserDonationServiceImpl implements UserDonationService {

	@Autowired
	UserDonationtDao uDDao;

	@Autowired
	UserDonationMapper udMapper;
	
	@Autowired
	AppUtils utils;

	@Override
	public List<UserDonation> findAll() {
		return uDDao.findAll();
	}

	@Override
	public UserDonation findById(Integer id) {
		Optional<UserDonation> result = uDDao.findById(id);
		return result.orElse(null);
	}

	@Override
	@Transactional
	public UserDonation update(UserDonation entity) {
		return uDDao.save(entity);
	}

	@Override
	@Transactional
	public void delete(UserDonation ud) {
		 uDDao.delete(ud);
	}
	
	@Override
	@Transactional
	public UserDonation save(UserDonationDto dto) {
		dto.setCreated(utils.currentDatefomart());
		dto.setStatus(0);
		return uDDao.save(udMapper.toEntity(dto));
	}

	@Override
	public Page<UserDonationDto> findByDonationIdAndPaginate(Integer donationId, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo-1, pageSize);
		
		Page<UserDonation> pageUserDonation = uDDao.findByDonationId(donationId, pageable);
		Page<UserDonationDto> result = udMapper.toListDto(pageUserDonation);
		return result;
	}



}
