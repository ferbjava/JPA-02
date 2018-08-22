package com.capgemini.types;

public class AuthorTO {

	public AuthorTO(String firstName, String lastName) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public AuthorTO(String firstName, String lastName, Long id) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.id = id;
	}

	private String firstName;
	private String lastName;
	private Long id;

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Long getId() {
		return id;
	}

	public static AuthorTOBuilder builder() {
		return new AuthorTOBuilder();
	}

	public static class AuthorTOBuilder {

		private String firstName;
		private String lastName;
		private Long id;

		public AuthorTOBuilder() {
			super();
		}

		public AuthorTOBuilder withFirstName(String firstName) {
			this.firstName = firstName;
			return this;
		}

		public AuthorTOBuilder withLastName(String lastName) {
			this.lastName = lastName;
			return this;
		}

		public AuthorTOBuilder withId(Long id) {
			this.id = id;
			return this;
		}

		public AuthorTO build() {
			checkBeforeBuild(firstName, lastName);
			return new AuthorTO(firstName, lastName, id);
		}

		private void checkBeforeBuild(String firstName, String lastName) {
			if (firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty()) {
				throw new RuntimeException("Incorrect author to be created");
			}

		}

	}

	@Override
	public String toString() {
		return "AuthorTO [firstName=" + firstName + ", lastName=" + lastName + ", id=" + id + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuthorTO other = (AuthorTO) obj;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		return true;
	}

}
