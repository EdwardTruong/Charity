package com.example.charity.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.charity.dao.DonationDao;
import com.example.charity.dto.DonationDto;
import com.example.charity.entity.Donation;
import com.example.charity.mapper.DonationMapper;

import jakarta.transaction.Transactional;

/*
 * In is project the method findDonationByInfo () has a little error : 
 * if in page 1, the function is ok but in page 2 have problem with count list - fix late
 */


@Service
public class DonationServiceImpl implements DonationService {

	@Autowired
	DonationDao dDao;
	
	@Autowired
	DonationMapper dMapper;

	@Override
	public Donation findById(Integer id) {
		Optional<Donation> result = dDao.findById(id);
			if(result.isPresent()) {
				return result.get();
			}
		return null;
	}

	@Override
	@Transactional
	public Donation saveNew(DonationDto dto) {
		dto.setStatus(0);
		dto.setMoney("0 VND");
		Donation entity = dMapper.toEntity(dto);
		dDao.save(entity);
		return entity ;
	}

	@Override
	public Donation findByCode(String code) {
		return dDao.findByCode(code);
	}
	
	@Override
	@Transactional
	public Donation update(Donation entity) {
		return dDao.save(entity);
	}

	@Override
	@Transactional
	public void deleteDonation(Donation entity) {
		dDao.delete(entity);
	}

	@Override
	public List<Donation> findAll() {
		return dDao.findAll();
	}

	@Override
	public Page<DonationDto> findAll(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo-1, pageSize);
		Page<Donation> dPage = dDao.findAll(pageable);
		return dMapper.toListDto(dPage);
	}


	@Override
	public List<DonationDto> findDonationByInfo(String input) {
		List<Donation> list = new ArrayList<>();
		if(input.toLowerCase().contains("mới") || input.toLowerCase().contains("moi")) {
			list= dDao.findDonationByStatus(0);
				
			}else if(input.toLowerCase().contains("đang quyên góp")|| input.toLowerCase().contains("đang") || input.toLowerCase().contains("đang quyên")|| input.toLowerCase().contains("dang") || input.toLowerCase().contains("dang quyen")) {
				list= dDao.findDonationByStatus(1);
				
			}else if(input.toLowerCase().contains("kết thúc") ||input.toLowerCase().contains("ket thuc")||input.toLowerCase().contains("kết") ||input.toLowerCase().contains("ket") ) {
				list= dDao.findDonationByStatus(2);
				
			}else if(input.toLowerCase().contains("đóng quyên góp") ||input.toLowerCase().contains("đóng")||input.toLowerCase().contains("đóng quyên")||input.toLowerCase().contains("dong")) {
				list= dDao.findDonationByStatus(3);
			}else {
				list=dDao.foundDonationByInfo(input);
			}
		
		List<DonationDto> result = dMapper.toListDto(list);
		return result;
	}

	@Override
	public Page<DonationDto> findDonationByInfo(int pageNumber, int pageSize , String input) {
		
		Pageable pageable = PageRequest.of(pageNumber-1,pageSize);
		
		Page<Donation> list  = dDao.foundDonationByInfo(input,pageable);
	
		
		if(input.toLowerCase().contains("mới") || input.toLowerCase().contains("moi")) {
		list= dDao.findDonationByStatus(0,pageable);
			
		}else if(input.toLowerCase().contains("đang quyên góp")|| input.toLowerCase().contains("đang") || input.toLowerCase().contains("đang quyên")|| input.toLowerCase().contains("dang") || input.toLowerCase().contains("dang quyen")) {
			list= dDao.findDonationByStatus(1,pageable);
			
		}else if(input.toLowerCase().contains("kết thúc") ||input.toLowerCase().contains("ket thuc")||input.toLowerCase().contains("kết") ||input.toLowerCase().contains("ket") ) {
			list= dDao.findDonationByStatus(2,pageable);
			
		}else if(input.toLowerCase().contains("đóng quyên góp") ||input.toLowerCase().contains("đóng")||input.toLowerCase().contains("đóng quyên")||input.toLowerCase().contains("dong")) {
			list= dDao.findDonationByStatus(3,pageable);
		}
			
		
		return dMapper.toListDto(list);

	}




	

}
