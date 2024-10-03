package com.example.charity.mapper;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.example.charity.dto.UserDto;
import com.example.charity.entity.User;

@Component
public class UserMapper {
	
	public User toEntity(UserDto dto) {
		User entity = new User();
		entity.setId(dto.getId());
		entity.setAddress(dto.getAddress());
		entity.setEmail(dto.getEmail());
		entity.setFullName(dto.getFullName());
		entity.setPhoneNumber(dto.getPhoneNumber());
		entity.setStatus(dto.getStatus());
		entity.setUserName(dto.getUserName());
		entity.setCreated(dto.getCreated());
		entity.setPassword(dto.getPassword());
		entity.setRole(dto.getRole());
		return entity;
	}

	public UserDto toDto(User entity) {

		return UserDto.builder()
				.id(entity.getId())
				.address(entity.getAddress())
				.email(entity.getEmail())
				.fullName(entity.getFullName())
				.phoneNumber(entity.getPhoneNumber())
				.status(entity.getStatus())
				.userName(entity.getUserName())
				.created(entity.getCreated())
				.role(entity.getRole())
				.roleId(entity.getRole().getId())
				.build();
	}
	
	public List<UserDto> toListDto(List<User> listDtos){
		return listDtos.stream().map(entity->toDto(entity)).toList();
	}
	
	public Page<UserDto> toListDto(Page<User> listUsers) {
	    return listUsers.map(this::toDto);
	}
	
}
