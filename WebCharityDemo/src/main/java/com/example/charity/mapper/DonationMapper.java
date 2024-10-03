package com.example.charity.mapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.example.charity.dto.DonationDto;
import com.example.charity.entity.Donation;
import com.example.charity.util.AppUtils;

@Component
public class DonationMapper {

	@Autowired
	AppUtils util;
	
	public Donation toEntity(DonationDto dto) {
		Donation entity = new Donation();
		entity.setId(dto.getId());
		entity.setCode(dto.getCode());
		entity.setName(dto.getName());
		entity.setStartDate(dto.getStartDate());
		entity.setEndDate(dto.getEndDate());
		entity.setOrganizationName(dto.getOrganizationName());
		entity.setPhoneNumber(dto.getPhoneNumber());
		entity.setDescription(dto.getDescription());
		entity.setLocal(dto.getLocal());
		entity.setStatus(dto.getStatus());
		return entity;
	}
	
	public DonationDto toDto(Donation entity) {
		DonationDto dto = new DonationDto();
		dto.setId(entity.getId());
		dto.setCode(entity.getCode());
		dto.setDescription(entity.getDescription());
		dto.setEndDate(util.changeDateFormatDMY(entity.getEndDate())); 
		dto.setMoney(util.convertToVND(entity.getMoney()));
		dto.setName(entity.getName());
		dto.setOrganizationName(entity.getOrganizationName());
		dto.setPhoneNumber(entity.getPhoneNumber());
		dto.setStartDate(util.changeDateFormatDMY(entity.getStartDate()));
		dto.setStatus(entity.getStatus());
		dto.setLocal(entity.getLocal());
		dto.setMoney(util.convertToVND(entity.getMoney()));
		return dto;
				
	}
	public List<DonationDto> toListDto(List<Donation> listDtos){
		return listDtos.stream().map(dto->toDto(dto)).toList();
	}
	
	public Page<DonationDto> toListDto(Page<Donation> listDonations){
		return listDonations.map(this::toDto);
	}
}
