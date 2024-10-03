package com.example.charity.mapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.example.charity.dto.UserDonationDto;
import com.example.charity.entity.UserDonation;
import com.example.charity.util.AppUtils;

@Component
public class UserDonationMapper {
	
	@Autowired
	AppUtils utils;
	
	public UserDonation toEntity(UserDonationDto dto) {
		UserDonation entity = new UserDonation();
		entity.setCreated(dto.getCreated());
		entity.setMoney(Long.parseLong(dto.getMoney()));
		entity.setName(dto.getName());
		entity.setStatus(dto.getStatus());
		entity.setText(dto.getText());
		return entity;
	}
	
	public UserDonationDto toDto(UserDonation entity) {
		UserDonationDto dto = new UserDonationDto();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setMoney(utils.convertToVND(entity.getMoney()));
		dto.setText(entity.getText());
		dto.setStatus(entity.getStatus());
		dto.setCreated(entity.getCreated());
		return dto;
	}
	
	public List<UserDonationDto> toListDto(List<UserDonation> listUserDonations){
		return listUserDonations.stream().map(dto->toDto(dto)).toList();
	}
	
	public Page<UserDonationDto> toListDto(Page<UserDonation>listUserDonations){
		return listUserDonations.map(this::toDto);
	}
}
