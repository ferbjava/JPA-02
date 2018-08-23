package com.capgemini.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import com.capgemini.types.ClientTO;
import com.capgemini.types.ClientTO.ClientTOBuilder;
import com.capgemini.types.ProductTO;
import com.capgemini.types.ProductTO.ProductTOBuilder;

/**
 * Support class with helpful data to test application 'JPA-02'
 * <p>
 * Provides following data:
 * <p>
 * <p>
 * - 3 ClientTO,
 * <p>
 * - 3 ProductTO,
 * <p>
 * and getters to each TO's by 'id'.
 * 
 * @author MKOTECKI
 *
 */
public class TestData {

	private List<ClientTO> clients = new ArrayList<>();
	private List<ProductTO> products = new ArrayList<>();

	public TestData() {
	}

	public void initialize() {
		fillClientsList();
		fillProductsList();
	}

	public ClientTO getClientById(int id) {
		return clients.get(id);
	}

	public ProductTO getProductById(int id) {
		return products.get(id);
	}

	private void fillClientsList() {
		this.clients.add(new ClientTOBuilder().withFirstName("Jan").withLastName("Kowalski").withEmail("jan@gmail.com")
				.withPhoneNumber(123456789).withAdress("Poznan, ul. Glogowska 45 m. 8")
				.withDateBirth(new GregorianCalendar(1980, 0, 1)).build());
		this.clients.add(new ClientTOBuilder().withFirstName("Andrzej").withLastName("Nowak").withEmail("andrzej@gmail.com")
				.withPhoneNumber(234567890).withAdress("Pcim, ul. Pusta 0 m. 0")
				.withDateBirth(new GregorianCalendar(1990, 11, 31)).build());
		this.clients.add(new ClientTOBuilder().withFirstName("Pankracy").withLastName("Wisniewski").withEmail("pankracy@gmail.com")
				.withPhoneNumber(123456789).withAdress("Sosnowiec, ul. Ostatnia 913919 m. 13213123")
				.withDateBirth(new GregorianCalendar(1980, 0, 1)).build());
	}

	private void fillProductsList() {
		this.products.add(new ProductTOBuilder().withName("wihajster").withUnitPrice(new BigDecimal("450.50"))
				.withMargin(32).withWeigth(new BigDecimal("12.5")).build());
		this.products.add(new ProductTOBuilder().withName("ajzol").withUnitPrice(new BigDecimal("888.88"))
				.withMargin(15).withWeigth(new BigDecimal("9.99")).build());
		this.products.add(new ProductTOBuilder().withName("dynks").withUnitPrice(new BigDecimal("123.45"))
				.withMargin(50).withWeigth(new BigDecimal("1.5")).build());
	}

}
