package com.example.charity.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="donation")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Donation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;

	@Column(name = "code")
	@NotBlank(message="Yêu Cầu nhập thông tin !")
	private String code;

	@Column(name = "local")
	private String local;
	
	@Column(name = "name")
	@NotBlank(message="Yêu Cầu nhập thông tin tên đợt quyên góp!")
	private String name;
	
	@Column(name = "start_date")
	@NotBlank(message = "Cần có ngày bắt đầu.")
	private String startDate;
	
	@Column(name = "end_date")
	@NotBlank(message = "Cần có ngày kết thúc.")
	private String endDate;
	
	@Column(name = "organization_name")
	@NotBlank(message="Yêu Cầu nhập thông tin !")
	private String organizationName;

	@Column(name = "phone_number")
	@Pattern(regexp = "^(0?)(3[2-9]|5[6|8|9]|7[0|6-9]|8[0-6|8|9]|9[0-4|6-9])[0-9]{7}$", message = "Yêu cầu số điện thoại")
	private String phoneNumber;
	
	@Column(name = "description")
	@NotBlank(message="Yêu Cầu nhập thông tin !")
	private String description;
	
	@Column(name = "money")
	private long money;

	@Column(name = "status")
	@Min(value = 0,message = "Yêu cầu giai đoạn đợt quyên góp")
	private int status;

	@ManyToMany()
	@JoinTable(
			name="user_donation",
			joinColumns = @JoinColumn(name="donation_id"),
			inverseJoinColumns = @JoinColumn(name="user_id")
			)
	private List<UserDonation> usersDonation;
	
	
	public void add(UserDonation tempUserDonation) {
		if (usersDonation == null) {
			usersDonation = new ArrayList<>();
		}
		usersDonation.add(tempUserDonation);
		
		tempUserDonation.setDonation(this);
	}
}