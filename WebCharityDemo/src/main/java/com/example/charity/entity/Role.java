package com.example.charity.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "role")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@NotNull
	@Column(name = "role_name")
	private String roleName;

	@OneToMany(mappedBy = "role", fetch = FetchType.EAGER)
	private List<User> users;

	@Override
	public String toString() {
		return "Role [id=" + id + ", name=" + roleName + ", users=" + users + "]";
	}

	public void add(User tempUser) {
		if (users == null) {
			users = new ArrayList<>();
		}
		users.add(tempUser);

		tempUser.setRole(this);
	}
}
