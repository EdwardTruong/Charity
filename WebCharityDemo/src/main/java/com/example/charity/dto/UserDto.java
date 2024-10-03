package com.example.charity.dto;


import com.example.charity.entity.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	
	private int id;

	@Size(min = 1, max = 100, message = "Nhập địa chỉ")
	private String address;

	@Email(message = "Email Không đúng định dạng. Vui lòng nhập lại.")
	@NotBlank(message = "Yêu cầu thông tin email.")
	private String email;

	@Size(min = 4, message = "Cần điền đầy đủ thông tin.")
	private String fullName;

	@Size(min = 4, message = "Mật khẩu cần ít nhất 4 ký tự.")
	private String password;
	
	@Pattern(regexp = "^(0?)(3[2-9]|5[6|8|9]|7[0|6-9]|8[0-6|8|9]|9[0-4|6-9])[0-9]{7}$", message = "Nhập số điện thoại di động.")
	private String phoneNumber;

	@Size(min = 6, max = 20, message = " Yêu cầu cần có 6 đến 12 ký tự, không có khoảng trắng và không dấu.")
	private String userName;

	private int status;

	private String created;
	

	@NotNull(message = "Yêu cầu có vai trò.")
	private Role role;

	private int roleId;
}
