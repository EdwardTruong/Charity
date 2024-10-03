package com.example.charity.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UserDonationDto {

	private int id;
	
	private String created;
	
	@NotBlank(message = "Nhập tên người quyên góp")
	private String name;
	
	private int userId;

	private int donationId;

	@Min(value = 10000, message = "Số tiền tối thiểu là 10.000 VND")
	@Max(value = 999999999 , message = "Số tiền tối đa là 999,999,999 VND.")
	private String money;
	
	private int status;

	@NotBlank(message = "Hãy viết một số thông tin chia sẽ.")
	private String text;

}
