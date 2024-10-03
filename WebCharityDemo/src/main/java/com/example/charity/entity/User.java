package com.example.charity.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="user")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "address")
	@NotBlank(message = "Yêu cầu địa chỉ.")
	private String address;

	@Column(name = "email", unique = true)
	@Email(message = "Email Không đúng định dạng. Vui lòng nhập lại.")
	@NotBlank(message = "Yêu cầu nhập email.")
	private String email;

	@Column(name = "full_name")
	@NotBlank(message = "Yêu cầu thông tin username.")
	private String fullName;

	@Column(name = "note")
	private String note;

	@Column(name = "password")
	@Size(min = 4, message = "Mật khẩu cần ít nhất 4 ký tự.")
	private String password;

	@Column(name = "phone_number")
	@Pattern(regexp = "^(0?)(3[2-9]|5[6|8|9]|7[0|6-9]|8[0-6|8|9]|9[0-4|6-9])[0-9]{7}$", message = "Yêu cầu số điện thoại di động.")
	private String phoneNumber;

	@Column(name = "status")
	@NotNull(message = "Yêu cầu thông tin hoạt động.")
	private int status;

	@Column(name = "user_name", unique = true)
	@NotBlank(message = "Cần thông tin username.")
	private String userName;

	@Column(name = "created")
	private String created;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "role_id")
	@NotNull(message = "Yêu cầu có vai trò.")
	private Role role;

	@ManyToMany()
	@JoinTable(
			name="user_donation",
			joinColumns = @JoinColumn(name="user_id"),
			inverseJoinColumns = @JoinColumn(name="donation_id")
			)
	private List<UserDonation> usersDonation;
	
	public void add(UserDonation tempUserDonation) {
		if (usersDonation == null) {
			usersDonation = new ArrayList<>();
		}
		usersDonation.add(tempUserDonation);
		tempUserDonation.setUser(this);
	}
}
