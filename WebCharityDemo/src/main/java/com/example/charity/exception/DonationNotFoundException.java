package com.example.charity.exception;

public class DonationNotFoundException extends RuntimeException {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -509202036628494404L;

	public DonationNotFoundException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DonationNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public DonationNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public DonationNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public DonationNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	
}
