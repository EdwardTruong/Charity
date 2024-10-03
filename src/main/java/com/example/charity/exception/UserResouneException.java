package com.example.charity.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResouneException {

	private int status;
	private String messenger;
	private Long timeSpamt;
}
