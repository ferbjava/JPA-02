package com.capgemini.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.capgemini.exceptions.HighPrizeException;
import com.capgemini.exceptions.TransactionHistoryException;
import com.capgemini.mappers.ProductMapper;
import com.capgemini.types.ClientTO;
import com.capgemini.types.ProductTO;
import com.capgemini.types.TransactionTO;
import com.capgemini.types.TransactionTO.TransactionTOBuilder;
import com.capgemini.utils.TestData;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClientServiceTest {

	@Autowired
	private ClientService clientService;

	@Autowired
	private ProductService productService;

	@Test
	@Transactional
	public void shouldAddClient() {
		// given
		TestData data = new TestData();
		data.initialize();

		final long EXPECTED_INITIAL_CLIENTS = 0;
		final long EXPECTED_FINAL_CLIENTS = 1;

		long initialClients = clientService.findClientsNo();
		// when
		clientService.save(data.getClientById(0));
		long finalClients = clientService.findClientsNo();

		// then
		assertEquals(EXPECTED_INITIAL_CLIENTS, initialClients);
		assertEquals(EXPECTED_FINAL_CLIENTS, finalClients);
	}

	@Test
	@Transactional
	public void shouldFailOptimisticLocking() {
		// given
		TestData data = new TestData();
		data.initialize();

		final long EXPECTED_INITIAL_CLIENTS = 0;
		final long EXPECTED_FINAL_CLIENTS = 1;
		long initialClients = clientService.findClientsNo();
		ClientTO savedClient01 = clientService.save(data.getClientById(0));
		long finalClients = clientService.findClientsNo();

		ClientTO selectedClient01_01 = clientService.findClientById(savedClient01.getId());
		ClientTO selectedClient01_02 = clientService.findClientById(savedClient01.getId());
		int version01_01 = selectedClient01_01.getVersion();
		int version01_02 = selectedClient01_02.getVersion();
		// when
		selectedClient01_01.setFirstName("Jarogniew");
		selectedClient01_02.setFirstName("Adam");
		clientService.save(selectedClient01_01);
		long clientsAfterUpdate = clientService.findClientsNo();

		boolean isException = false;
		try {
			clientService.save(selectedClient01_02);
		} catch (OptimisticLockingFailureException ex) {
			isException = true;
		}

		// then
		assertEquals(EXPECTED_INITIAL_CLIENTS, initialClients);
		assertEquals(EXPECTED_FINAL_CLIENTS, finalClients);
		assertEquals(EXPECTED_FINAL_CLIENTS, clientsAfterUpdate);
		assertEquals(version01_01, version01_02);
		assertTrue(isException);
	}

	@Test
	@Transactional
	public void shouldAddTransactionToClient() throws Exception {
		// given
		TestData data = new TestData();
		data.initialize();

		final long EXPECTED_INITIAL_TRANSACTIONS = 0;
		final long EXPECTED_FINAL_TRANSACTIONS = 1;

		ProductTO savedProduct01 = productService.save(data.getProductById(0));
		List<ProductTO> productsList = new ArrayList<>();
		productsList.add(savedProduct01);
		ClientTO savedClient01 = clientService.save(data.getClientById(0));
		long initialTransactions = clientService.findClientTransactionsNo(savedClient01.getId());

		// when
		TransactionTO transaction = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 7, 23))
				.withProductsIds(ProductMapper.map2TOsId(productsList)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction);
		long finalTransactions = clientService.findClientTransactionsNo(savedClient01.getId());

		// then
		assertEquals(EXPECTED_INITIAL_TRANSACTIONS, initialTransactions);
		assertEquals(EXPECTED_FINAL_TRANSACTIONS, finalTransactions);
	}

	@Test
	@Transactional
	public void shouldRemoveClientCascadeTest() throws Exception {
		// given
		TestData data = new TestData();
		data.initialize();

		final long EXPECTED_INITIAL_PRODUCTS = 1;
		final long EXPECTED_FINAL_PRODUCTS = 1;
		final long EXPECTED_INITIAL_CLIENTS = 2;
		final long EXPECTED_FINAL_CLIENTS = 1;
		final long EXPECTED_INITIAL_TRANSACTIONS = 2;
		final long EXPECTED_FINAL_TRANSACTIONS = 1;

		ProductTO savedProduct01 = productService.save(data.getProductById(0));
		List<ProductTO> productsList = new ArrayList<>();
		productsList.add(savedProduct01);
		ClientTO savedClient01 = clientService.save(data.getClientById(0));
		ClientTO savedClient02 = clientService.save(data.getClientById(1));

		TransactionTO transaction01 = new TransactionTOBuilder().withClientId(savedClient01.getId())
				.withDate(new GregorianCalendar(2018, 7, 22)).withProductsIds(ProductMapper.map2TOsId(productsList))
				.withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction01);

		TransactionTO transaction02 = new TransactionTOBuilder().withClientId(savedClient02.getId())
				.withDate(new GregorianCalendar(2018, 7, 23)).withProductsIds(ProductMapper.map2TOsId(productsList))
				.withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient02.getId(), transaction02);

		long initalProductsNo = productService.findProductsNo();
		long initalClientsNo = clientService.findClientsNo();
		long initalTransactionsNo = clientService.findTransactionsNo();

		// when
		clientService.removeClient(savedClient01.getId());

		long finalProductsNo = productService.findProductsNo();
		long finalClientsNo = clientService.findClientsNo();
		long finalTransactionsNo = clientService.findTransactionsNo();

		// then
		assertEquals(EXPECTED_INITIAL_PRODUCTS, initalProductsNo);
		assertEquals(EXPECTED_FINAL_PRODUCTS, finalProductsNo);
		assertEquals(EXPECTED_INITIAL_CLIENTS, initalClientsNo);
		assertEquals(EXPECTED_FINAL_CLIENTS, finalClientsNo);
		assertEquals(EXPECTED_INITIAL_TRANSACTIONS, initalTransactionsNo);
		assertEquals(EXPECTED_FINAL_TRANSACTIONS, finalTransactionsNo);
	}

	@Test
	@Transactional
	public void shouldPerformInvalidTransaction6ExpensiveItems() throws Exception {
		// given
		TestData data = new TestData();
		data.initialize();

		ProductTO savedProduct01 = productService.save(data.getProductById(0));
		ProductTO savedProduct02 = productService.save(data.getProductById(3));
		ProductTO savedProduct03 = productService.save(data.getProductById(1));
		
		List<ProductTO> productsList01 = new ArrayList<>();
		productsList01.add(savedProduct01);
		productsList01.add(savedProduct03);
		List<ProductTO> productsList02 = new ArrayList<>();
		productsList02.add(savedProduct01);
		List<ProductTO> productsList03 = new ArrayList<>();
		productsList03.add(savedProduct01);
		List<ProductTO> productsList04 = new ArrayList<>();
		productsList04.add(savedProduct01);
		productsList04.add(savedProduct02);
		productsList04.add(savedProduct02);
		productsList04.add(savedProduct02);
		productsList04.add(savedProduct02);
		productsList04.add(savedProduct02);
		productsList04.add(savedProduct02);
		ClientTO savedClient01 = clientService.save(data.getClientById(0));

		TransactionTO transaction01 = new TransactionTOBuilder().withClientId(savedClient01.getId())
				.withDate(new GregorianCalendar(2018, 7, 22)).withProductsIds(ProductMapper.map2TOsId(productsList01))
				.withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction01);

		TransactionTO transaction02 = new TransactionTOBuilder().withClientId(savedClient01.getId())
				.withDate(new GregorianCalendar(2018, 7, 22)).withProductsIds(ProductMapper.map2TOsId(productsList02))
				.withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction02);

		TransactionTO transaction03 = new TransactionTOBuilder().withClientId(savedClient01.getId())
				.withDate(new GregorianCalendar(2018, 7, 22)).withProductsIds(ProductMapper.map2TOsId(productsList03))
				.withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction03);
		
		TransactionTO transaction04 = new TransactionTOBuilder().withClientId(savedClient01.getId())
				.withDate(new GregorianCalendar(2018, 7, 22)).withProductsIds(ProductMapper.map2TOsId(productsList04))
				.withStatus("Completed").build();

		// when
		boolean isException = false;
		try {
			clientService.addTransactionToClient(savedClient01.getId(), transaction04);
		} catch (HighPrizeException ex) {
			isException = true;
		}

		// then
		assertTrue(isException);
	}

	@Test
	@Transactional
	public void shouldPerformInvalidTransactionSecondPurchaseExpensive() throws Exception {
		// given
		TestData data = new TestData();
		data.initialize();

		ProductTO savedProduct01 = productService.save(data.getProductById(0));
		ProductTO savedProduct02 = productService.save(data.getProductById(1));
		ProductTO savedProduct03 = productService.save(data.getProductById(3));
		List<ProductTO> productsList01 = new ArrayList<>();
		productsList01.add(savedProduct01);
		productsList01.add(savedProduct02);
		List<ProductTO> productsList02 = new ArrayList<>();
		productsList02.add(savedProduct01);
		productsList02.add(savedProduct03);
		ClientTO savedClient01 = clientService.save(data.getClientById(0));
		
		TransactionTO transaction01 = new TransactionTOBuilder().withClientId(savedClient01.getId())
				.withDate(new GregorianCalendar(2018, 7, 21)).withProductsIds(ProductMapper.map2TOsId(productsList01))
				.withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction01);

		// when
		boolean isException = false;
		TransactionTO transaction02 = new TransactionTOBuilder().withClientId(savedClient01.getId())
				.withDate(new GregorianCalendar(2018, 7, 22)).withProductsIds(ProductMapper.map2TOsId(productsList02))
				.withStatus("Completed").build();
		try {
			clientService.addTransactionToClient(savedClient01.getId(), transaction02);
		} catch (TransactionHistoryException ex) {
			isException = true;
		}

		// then
		assertTrue(isException);
	}

	@Test
	@Transactional
	public void shouldReturnClientTransactioncCost() throws Exception {
		// given
		TestData data = new TestData();
		data.initialize();
		BigDecimal EXPECTED_COST = new BigDecimal("2240.38");

		ProductTO savedProduct01 = productService.save(data.getProductById(0));
		ProductTO savedProduct02 = productService.save(data.getProductById(1));
		
		List<ProductTO> productsList01 = new ArrayList<>();
		productsList01.add(savedProduct01);
		productsList01.add(savedProduct02);
		List<ProductTO> productsList02 = new ArrayList<>();
		productsList02.add(savedProduct01);
		List<ProductTO> productsList03 = new ArrayList<>();
		productsList03.add(savedProduct01);
		ClientTO savedClient01 = clientService.save(data.getClientById(0));

		TransactionTO transaction01 = new TransactionTOBuilder().withClientId(savedClient01.getId())
				.withDate(new GregorianCalendar(2018, 7, 22)).withProductsIds(ProductMapper.map2TOsId(productsList01))
				.withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction01);

		TransactionTO transaction02 = new TransactionTOBuilder().withClientId(savedClient01.getId())
				.withDate(new GregorianCalendar(2018, 7, 22)).withProductsIds(ProductMapper.map2TOsId(productsList02))
				.withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction02);

		TransactionTO transaction03 = new TransactionTOBuilder().withClientId(savedClient01.getId())
				.withDate(new GregorianCalendar(2018, 7, 22)).withProductsIds(ProductMapper.map2TOsId(productsList03))
				.withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction03);

		// when
		BigDecimal cost = clientService.findCostOfTransactionsByClient(savedClient01.getId());

		// then
		assertEquals(EXPECTED_COST, cost);
	}

	@Test
	@Transactional
	public void shouldReturnProfitByPeriod() throws Exception {
		// given
		TestData data = new TestData();
		data.initialize();
		BigDecimal EXPECTED_COST = new BigDecimal("2240.38");

		ProductTO savedProduct01 = productService.save(data.getProductById(0));
		ProductTO savedProduct02 = productService.save(data.getProductById(1));
		Calendar startPeriod = new GregorianCalendar(2018, 6, 1);
		Calendar endPeriod = new GregorianCalendar(2018, 7, 1);
		
		List<ProductTO> productsList01 = new ArrayList<>();
		productsList01.add(savedProduct01);
		productsList01.add(savedProduct02);
		List<ProductTO> productsList02 = new ArrayList<>();
		productsList02.add(savedProduct01);
		List<ProductTO> productsList03 = new ArrayList<>();
		productsList03.add(savedProduct01);
		ClientTO savedClient01 = clientService.save(data.getClientById(0));

		TransactionTO transaction01 = new TransactionTOBuilder().withClientId(savedClient01.getId())
				.withDate(new GregorianCalendar(2018, 0, 1)).withProductsIds(ProductMapper.map2TOsId(productsList01))
				.withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction01);

		TransactionTO transaction02 = new TransactionTOBuilder().withClientId(savedClient01.getId())
				.withDate(new GregorianCalendar(2018, 6, 12)).withProductsIds(ProductMapper.map2TOsId(productsList02))
				.withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction02);

		TransactionTO transaction03 = new TransactionTOBuilder().withClientId(savedClient01.getId())
				.withDate(new GregorianCalendar(2018, 7, 29)).withProductsIds(ProductMapper.map2TOsId(productsList03))
				.withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction03);

		// when
		BigDecimal cost = clientService.findProfitByPeriod(startPeriod, endPeriod);

		// then
		assertEquals(EXPECTED_COST, cost);
	}

}
