package com.capgemini.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.capgemini.listeners.CreateListener;
import com.capgemini.listeners.UpdateListener;

@Entity
@EntityListeners({ CreateListener.class, UpdateListener.class })
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "CLIENT")
public class ClientEntity extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 45)
	private String firstName;
	@Column(nullable = false, length = 45)
	private String lastName;
	@Column(nullable = false, length = 45)
	private String email;
	@Column(nullable = false)
	private Integer phoneNumber;
	@Column(nullable = false, length = 45)
	private String adress;
	@Column(nullable = false)
	private Calendar dateBirth;
	@OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<TransactionEntity> transactions = new ArrayList<>();

	// for hibernate
	public ClientEntity() {
	}
	
	public ClientEntity(Long id, String firstName, String lastName, String email, Integer phoneNumber, String adress,
			Calendar dateBirth, List<TransactionEntity> transactions) {
		super(id);
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.adress = adress;
		this.dateBirth = dateBirth;
		this.transactions = transactions;
	}
	
	public ClientEntity(Long id, String firstName, String lastName, String email, Integer phoneNumber, String adress,
			Calendar dateBirth) {
		super(id);
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.adress = adress;
		this.dateBirth = dateBirth;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(Integer phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public Calendar getDateBirth() {
		return dateBirth;
	}

	public void setDateBirth(Calendar dateBirth) {
		this.dateBirth = dateBirth;
	}

	public List<TransactionEntity> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<TransactionEntity> transactions) {
		this.transactions = transactions;
	}

	public void addTransaction(TransactionEntity transaction) {
		this.transactions.add(transaction);
	}

}
