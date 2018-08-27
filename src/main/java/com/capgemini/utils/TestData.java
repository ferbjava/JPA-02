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
 * - 3 ClientTO's (indexes 0-3),
 * <p>
 * - 12 ProductTO's (indexes 0-12),
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
		this.clients.add(new ClientTOBuilder().withFirstName("Ryszard").withLastName("Ochodzki").withEmail("mis@gmail.com")
				.withPhoneNumber(123456789).withAdress("Swornegacie, ul. Dluga 432 m. 12")
				.withDateBirth(new GregorianCalendar(1985, 5, 3)).build());
	}

	private void fillProductsList() {
		this.products.add(new ProductTOBuilder().withName("wihajster").withPrice(new BigDecimal("450.50"))
				.withMargin(32).withWeigth(new BigDecimal("12.5")).build());
		this.products.add(new ProductTOBuilder().withName("ajzol").withPrice(new BigDecimal("888.88"))
				.withMargin(15).withWeigth(new BigDecimal("9.99")).build());
		this.products.add(new ProductTOBuilder().withName("dynks").withPrice(new BigDecimal("123.45"))
				.withMargin(50).withWeigth(new BigDecimal("1.5")).build());
		this.products.add(new ProductTOBuilder().withName("Golden Rolex").withPrice(new BigDecimal("100000.00"))
				.withMargin(12).withWeigth(new BigDecimal("0.2")).build());
		this.products.add(new ProductTOBuilder().withName("Glock 17 gen 5").withPrice(new BigDecimal("2400.00"))
				.withMargin(23).withWeigth(new BigDecimal("0.7")).build());
		this.products.add(new ProductTOBuilder().withName("CZ SP-01 Shadow").withPrice(new BigDecimal("2950.00"))
				.withMargin(25).withWeigth(new BigDecimal("1.3")).build());
		this.products.add(new ProductTOBuilder().withName("CZ P09").withPrice(new BigDecimal("1900.00"))
				.withMargin(26).withWeigth(new BigDecimal("0.75")).build());
		this.products.add(new ProductTOBuilder().withName("CZ P07 Kadet 0.22").withPrice(new BigDecimal("1950.00"))
				.withMargin(38).withWeigth(new BigDecimal("0.74")).build());
		this.products.add(new ProductTOBuilder().withName("H&K USP").withPrice(new BigDecimal("3000.00"))
				.withMargin(30).withWeigth(new BigDecimal("0.67")).build());
		this.products.add(new ProductTOBuilder().withName("Sig Sauer 1911 0.22").withPrice(new BigDecimal("1650.00"))
				.withMargin(35).withWeigth(new BigDecimal("0.8")).build());
		this.products.add(new ProductTOBuilder().withName("Chiappa 1911 0.22").withPrice(new BigDecimal("1500.00"))
				.withMargin(45).withWeigth(new BigDecimal("0.82")).build());
		this.products.add(new ProductTOBuilder().withName("GSG FireFly 0.22").withPrice(new BigDecimal("1050.00"))
				.withMargin(32).withWeigth(new BigDecimal("0.73")).build());
	}

}
