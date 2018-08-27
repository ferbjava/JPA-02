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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.capgemini.listeners.CreateListener;
import com.capgemini.listeners.UpdateListener;

@Entity
@EntityListeners({ CreateListener.class, UpdateListener.class })
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "TRANSACTION")
public class TransactionEntity extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	private ClientEntity client;
	@Column(nullable = false)
	private Calendar date;
	@Column(nullable = true, length = 45)
	private String status;
	@Column(nullable = true)
	private Long items;
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinTable(name = "TRANSACTION_PRODUCT", joinColumns = {
			@JoinColumn(name = "TRANSACTION_ID", nullable = false) }, inverseJoinColumns = {
			@JoinColumn(name = "PRODUCT_ID", nullable = false) })
	private List<ProductEntity> products = new ArrayList<>();

	// for hibernate
	public TransactionEntity() {
	}

	public TransactionEntity(Long id, Calendar date, String status) {
		super(id);
		this.date = date;
		this.status = status;
		this.items = new Long(0);
	}

	public ClientEntity getClient() {
		return client;
	}

	public void setClient(ClientEntity client) {
		this.client = client;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<ProductEntity> getProducts() {
		return products;
	}

	public void setProducts(List<ProductEntity> products) {
		this.products = products;
		this.items = new Long (products.size());
	}

	public Long getItems() {
		return items;
	}

	public void setItems(Long items) {
		this.items = items;
	}

	public void addProducts(List<ProductEntity> products) {
		this.products.addAll(products);
		this.items+= products.size();
	}

	public void addProduct(ProductEntity product) {
		this.products.add(product);
		this.items++;
	}

}
