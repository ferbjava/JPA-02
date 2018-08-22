package com.capgemini.types;

import java.util.Calendar;

public class ClientTO {

	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private Integer phoneNumber;
	private String adress;
	private Calendar dateBirth;

	public ClientTO() {
		super();
	}

	public ClientTO(Long id, String firstName, String lastName, String email, Integer phoneNumber, String adress,
			Calendar dateBirth) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.adress = adress;
		this.dateBirth = dateBirth;
	}

	public Long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public Integer getPhoneNumber() {
		return phoneNumber;
	}

	public String getAdress() {
		return adress;
	}

	public Calendar getDateBirth() {
		return dateBirth;
	}

	public ClientTOBuilder builder() {
		return new ClientTOBuilder();
	}

	public static class ClientTOBuilder {

		private Long id;
		private String firstName;
		private String lastName;
		private String email;
		private Integer phoneNumber;
		private String adress;
		private Calendar dateBirth;

		public ClientTOBuilder() {
			super();
		}

		public ClientTOBuilder withId(Long id) {
			this.id = id;
			return this;
		}

		public ClientTOBuilder withFirstName(String firstName) {
			this.firstName = firstName;
			return this;
		}

		public ClientTOBuilder withLastName(String lastName) {
			this.lastName = lastName;
			return this;
		}

		public ClientTOBuilder withEmail(String email) {
			this.email = email;
			return this;
		}

		public ClientTOBuilder withPhoneNumber(Integer phoneNumber) {
			this.phoneNumber = phoneNumber;
			return this;
		}

		public ClientTOBuilder withAdress(String adress) {
			this.adress = adress;
			return this;
		}

		public ClientTOBuilder withDateBirth(Calendar dateBirth) {
			this.dateBirth = dateBirth;
			return this;
		}
		
		public ClientTO build(){
			checkBeforeBuild(firstName, lastName, email, phoneNumber, adress, dateBirth);
			return new ClientTO(id, firstName, lastName, email, phoneNumber, adress, dateBirth);
		}

		private void checkBeforeBuild(String firstName, String lastName, String email, Integer phoneNumber,
				String adress, Calendar dateBirth) {
			boolean isFirstName = false;
			if (firstName != null && !firstName.isEmpty()) {
				isFirstName = true;
			}
			boolean isLastName = false;
			if (lastName != null && !lastName.isEmpty()) {
				isLastName = true;
			}
			boolean isEmail = false;
			if (email != null && !email.isEmpty()) {
				isEmail = true;
			}
			boolean isPhoneNumber = false;
			if (phoneNumber != null) {
				isPhoneNumber = true;
			}
			boolean isAdress = false;
			if (adress != null && !adress.isEmpty()) {
				isAdress = true;
			}
			boolean isDateBirth = false;
			if (dateBirth != null) {
				isDateBirth = true;
			}
			if (!isFirstName || !isLastName || !isEmail || !isPhoneNumber|| !isAdress || !isDateBirth) {
				throw new RuntimeException("Incorrect 'Client' to be created");
			}
		}

	}

}
