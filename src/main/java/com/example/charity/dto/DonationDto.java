package com.example.charity.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DonationDto {

	private int id;

	@NotBlank(message="Yêu cầu nhập thông tin code !")
	@Size(min=1,max=10 , message = "Độ dài min là 1 và max là 10.")
	private String code;
	
	@NotBlank(message="Yêu cầu nhập thông tin tên đợt quyên góp!")
	private String name;

	@NotBlank(message = "Cần có ngày bắt đầu.")
	private String startDate;

	@NotBlank(message = "Cần có ngày kết thúc.")
	private String endDate;

	@Size(min = 1,message="Yêu Cầu nhập thông tin !")
	private String organizationName;

	@Pattern(regexp = "^(0?)(3[2-9]|5[6|8|9]|7[0|6-9]|8[0-6|8|9]|9[0-4|6-9])[0-9]{7}$", message = "Yêu cầu số điện thoại liên lạc.")
	private String phoneNumber;

	@NotBlank(message="Yêu Cầu nhập thông tin mô tả !")
	private String description;

	private String local;
	
	private String money;
	
	@Min(value = 0,message = "Chọn trạng thái đợt quyên góp")
	private int status;

}
