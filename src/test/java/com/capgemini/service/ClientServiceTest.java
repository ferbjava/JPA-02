package com.capgemini.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.YearMonth;
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

import com.capgemini.domain.ClientEntity;
import com.capgemini.domain.TransactionEntity;
import com.capgemini.exceptions.HighPrizeException;
import com.capgemini.exceptions.TransactionHistoryException;
import com.capgemini.mappers.ProductMapper;
import com.capgemini.types.ClientTO;
import com.capgemini.types.ProductTO;
import com.capgemini.types.TransactionSearchCriteriaTO;
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

	@Test(expected = OptimisticLockingFailureException.class)
	@Transactional
	public void shouldFailClientOptimisticLocking() {
		// given
		TestData data = new TestData();
		data.initialize();

		ClientTO savedClient01 = clientService.save(data.getClientById(0));
		
		ClientTO selectedClient01_01 = clientService.findClientById(savedClient01.getId());
		ClientTO selectedClient01_02 = clientService.findClientById(savedClient01.getId());
		
		// when
		selectedClient01_01.setFirstName("Jarogniew");
		selectedClient01_02.setFirstName("Adam");
		
		clientService.save(selectedClient01_01);
		clientService.findClientsNo();
		clientService.save(selectedClient01_02);

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

	@Test(expected = OptimisticLockingFailureException.class)
	@Transactional
	public void shouldFailTransactionOptimisticLocking() throws Exception {
		// given
		TestData data = new TestData();
		data.initialize();

		ProductTO savedProduct01 = productService.save(data.getProductById(0));
		List<ProductTO> productsList = new ArrayList<>();
		productsList.add(savedProduct01);
		ClientTO savedClient01 = clientService.save(data.getClientById(0));
		
		TransactionTO transaction = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 7, 23))
				.withProductsIds(ProductMapper.map2TOsId(productsList)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction);

		// when
		TransactionTO selectedTrasnaction01_01 = clientService.findTransactionById(new Long(1));
		TransactionTO selectedTrasnaction01_02 = clientService.findTransactionById(new Long(1));
		
		selectedTrasnaction01_01.setStatus("Completed");
		selectedTrasnaction01_02.setStatus("Completed");

		clientService.updateTransaction(savedClient01.getId(), selectedTrasnaction01_01);
		clientService.findClientTransactionsNo(savedClient01.getId());
		clientService.updateTransaction(savedClient01.getId(), selectedTrasnaction01_02);
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

		TransactionTO transaction01 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 7, 22))
				.withProductsIds(ProductMapper.map2TOsId(productsList)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction01);

		TransactionTO transaction02 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 7, 23))
				.withProductsIds(ProductMapper.map2TOsId(productsList)).withStatus("Completed").build();
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

	@Test(expected = HighPrizeException.class)
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

		TransactionTO transaction01 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 7, 22))
				.withProductsIds(ProductMapper.map2TOsId(productsList01)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction01);

		TransactionTO transaction02 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 7, 22))
				.withProductsIds(ProductMapper.map2TOsId(productsList02)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction02);

		TransactionTO transaction03 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 7, 22))
				.withProductsIds(ProductMapper.map2TOsId(productsList03)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction03);

		TransactionTO transaction04 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 7, 22))
				.withProductsIds(ProductMapper.map2TOsId(productsList04)).withStatus("Completed").build();

		// when
		clientService.addTransactionToClient(savedClient01.getId(), transaction04);
			
	}

	@Test(expected = TransactionHistoryException.class)
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
		
		TransactionTO transaction01 = new TransactionTOBuilder()
				.withDate(new GregorianCalendar(2018, 7, 21)).withProductsIds(ProductMapper.map2TOsId(productsList01))
				.withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction01);

		// when
		TransactionTO transaction02 = new TransactionTOBuilder()
				.withDate(new GregorianCalendar(2018, 7, 22)).withProductsIds(ProductMapper.map2TOsId(productsList02))
				.withStatus("Completed").build();
		
		clientService.addTransactionToClient(savedClient01.getId(), transaction02);
		
	}

	@Test
	@Transactional
	public void shouldReturnClientTransactioncCost() throws Exception {
		// given
		TestData data = new TestData();
		data.initialize();
		
		final BigDecimal EXPECTED_COST = new BigDecimal("2240.38");

		ProductTO savedProduct01 = productService.save(data.getProductById(0));
		ProductTO savedProduct02 = productService.save(data.getProductById(1));
		
		List<ProductTO> productsList01 = new ArrayList<>();
		productsList01.add(savedProduct01);
		productsList01.add(savedProduct02);
		List<ProductTO> productsList02 = new ArrayList<>();
		productsList02.add(savedProduct01);
		List<ProductTO> productsList03 = new ArrayList<>();
		productsList03.add(savedProduct01);
		List<ProductTO> productsList04 = new ArrayList<>();
		productsList04.add(savedProduct01);
		
		ClientTO savedClient01 = clientService.save(data.getClientById(0));
		ClientTO savedClient02 = clientService.save(data.getClientById(1));

		TransactionTO transaction01 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 7, 22))
				.withProductsIds(ProductMapper.map2TOsId(productsList01)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction01);

		TransactionTO transaction02 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 7, 22))
				.withProductsIds(ProductMapper.map2TOsId(productsList02)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction02);

		TransactionTO transaction03 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 7, 22))
				.withProductsIds(ProductMapper.map2TOsId(productsList03)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction03);

		TransactionTO transaction04 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 7, 22))
				.withProductsIds(ProductMapper.map2TOsId(productsList04)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient02.getId(), transaction04);

		// when
		BigDecimal cost = clientService.findTotalCostByClient(savedClient01.getId());

		// then
		assertEquals(EXPECTED_COST, cost);
	}

	@Test
	@Transactional
	public void shouldReturnClientCostWithGivenStatus() throws Exception {
		// given
		TestData data = new TestData();
		data.initialize();
		
		final BigDecimal EXPECTED_COST = new BigDecimal("1789.88");
		final String EXPECTED_STATUS = "In delivery";

		ProductTO savedProduct01 = productService.save(data.getProductById(0));
		ProductTO savedProduct02 = productService.save(data.getProductById(1));
		
		List<ProductTO> productsList01 = new ArrayList<>();
		productsList01.add(savedProduct01);
		productsList01.add(savedProduct02);
		List<ProductTO> productsList02 = new ArrayList<>();
		productsList02.add(savedProduct01);
		List<ProductTO> productsList03 = new ArrayList<>();
		productsList03.add(savedProduct01);
		List<ProductTO> productsList04 = new ArrayList<>();
		productsList04.add(savedProduct01);
		
		ClientTO savedClient01 = clientService.save(data.getClientById(0));
		ClientTO savedClient02 = clientService.save(data.getClientById(1));

		TransactionTO transaction01 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 7, 22))
				.withProductsIds(ProductMapper.map2TOsId(productsList01)).withStatus(EXPECTED_STATUS).build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction01);

		TransactionTO transaction02 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 7, 22))
				.withProductsIds(ProductMapper.map2TOsId(productsList02)).withStatus(EXPECTED_STATUS).build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction02);

		TransactionTO transaction03 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 7, 22))
				.withProductsIds(ProductMapper.map2TOsId(productsList03)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction03);

		TransactionTO transaction04 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 7, 22))
				.withProductsIds(ProductMapper.map2TOsId(productsList04)).withStatus(EXPECTED_STATUS).build();
		clientService.addTransactionToClient(savedClient02.getId(), transaction04);

		// when
		BigDecimal cost = clientService.findCostByClientAndStatus(savedClient01.getId(), EXPECTED_STATUS);

		// then
		assertEquals(EXPECTED_COST, cost);
	}

	@Test
	@Transactional
	public void shouldReturnTotalCostWithGivenStatus() throws Exception {
		// given
		TestData data = new TestData();
		data.initialize();

		final BigDecimal EXPECTED_COST = new BigDecimal("2240.38");
		final String EXPECTED_STATUS = "In delivery";

		ProductTO savedProduct01 = productService.save(data.getProductById(0));
		ProductTO savedProduct02 = productService.save(data.getProductById(1));

		List<ProductTO> productsList01 = new ArrayList<>();
		productsList01.add(savedProduct01);
		productsList01.add(savedProduct02);
		List<ProductTO> productsList02 = new ArrayList<>();
		productsList02.add(savedProduct01);
		List<ProductTO> productsList03 = new ArrayList<>();
		productsList03.add(savedProduct01);
		List<ProductTO> productsList04 = new ArrayList<>();
		productsList04.add(savedProduct01);

		ClientTO savedClient01 = clientService.save(data.getClientById(0));
		ClientTO savedClient02 = clientService.save(data.getClientById(1));

		TransactionTO transaction01 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 7, 22))
				.withProductsIds(ProductMapper.map2TOsId(productsList01)).withStatus(EXPECTED_STATUS).build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction01);

		TransactionTO transaction02 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 7, 22))
				.withProductsIds(ProductMapper.map2TOsId(productsList02)).withStatus(EXPECTED_STATUS).build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction02);

		TransactionTO transaction03 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 7, 22))
				.withProductsIds(ProductMapper.map2TOsId(productsList03)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction03);

		TransactionTO transaction04 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 7, 22))
				.withProductsIds(ProductMapper.map2TOsId(productsList04)).withStatus(EXPECTED_STATUS).build();
		clientService.addTransactionToClient(savedClient02.getId(), transaction04);

		// when
		BigDecimal cost = clientService.findTotalCostByStatus(EXPECTED_STATUS);

		// then
		assertEquals(EXPECTED_COST, cost);
	}

	@Test
	@Transactional
	public void shouldReturnProfitByPeriod() throws Exception {
		// given
		TestData data = new TestData();
		data.initialize();
		
		final BigDecimal EXPECTED_PROFIT = new BigDecimal("277.49");

		ProductTO savedProduct01 = productService.save(data.getProductById(0));
		ProductTO savedProduct02 = productService.save(data.getProductById(1));
		
		YearMonth startDate = YearMonth.of(2018, 3);
		YearMonth endDate = YearMonth.of(2018, 12);
		
		List<ProductTO> productsList01 = new ArrayList<>();
		productsList01.add(savedProduct01);
		productsList01.add(savedProduct02);
		List<ProductTO> productsList02 = new ArrayList<>();
		productsList02.add(savedProduct01);
		List<ProductTO> productsList03 = new ArrayList<>();
		productsList03.add(savedProduct02);
		ClientTO savedClient01 = clientService.save(data.getClientById(0));

		TransactionTO transaction01 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 0, 1))
				.withProductsIds(ProductMapper.map2TOsId(productsList01)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction01);

		TransactionTO transaction02 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 6, 12))
				.withProductsIds(ProductMapper.map2TOsId(productsList02)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction02);

		TransactionTO transaction03 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 7, 29))
				.withProductsIds(ProductMapper.map2TOsId(productsList03)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction03);

		// when
		BigDecimal profit = clientService.findProfitByPeriod(startDate, endDate);

		// then
		assertEquals(EXPECTED_PROFIT, profit);
	}

	@Test
	@Transactional
	public void shouldReturn3ClientsWithMostExpensiveShoppings() throws Exception {
		// given
		TestData data = new TestData();
		data.initialize();
		
		final YearMonth START_DATE = YearMonth.of(2018, 6);
		final YearMonth END_DATE = YearMonth.of(2018, 8);
		final Long EXPECTED_01_CLIENT_ID = new Long(1);
		final Long EXPECTED_02_CLIENT_ID = new Long(3);
		final Long EXPECTED_03_CLIENT_ID = new Long(2);

		ProductTO savedProduct01 = productService.save(data.getProductById(0));
		ProductTO savedProduct02 = productService.save(data.getProductById(1));
		ProductTO savedProduct03 = productService.save(data.getProductById(2));
		ClientTO savedClient01 = clientService.save(data.getClientById(0));
		ClientTO savedClient02 = clientService.save(data.getClientById(1));
		ClientTO savedClient03 = clientService.save(data.getClientById(2));
		ClientTO savedClient04 = clientService.save(data.getClientById(3));
		
		List<ProductTO> productsList01 = new ArrayList<>();
		productsList01.add(savedProduct01);
		productsList01.add(savedProduct02);
		List<ProductTO> productsList02 = new ArrayList<>();
		productsList02.add(savedProduct03);
		List<ProductTO> productsList03 = new ArrayList<>();
		productsList03.add(savedProduct01);
		productsList03.add(savedProduct03);
		List<ProductTO> productsList04 = new ArrayList<>();
		productsList04.add(savedProduct01);
		productsList04.add(savedProduct02);
		List<ProductTO> productsList05 = new ArrayList<>();
		productsList05.add(savedProduct01);

		TransactionTO transaction01 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 5, 1))
				.withProductsIds(ProductMapper.map2TOsId(productsList01)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction01);

		TransactionTO transaction02 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 6, 12))
				.withProductsIds(ProductMapper.map2TOsId(productsList02)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction02);

		TransactionTO transaction03 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 7, 29))
				.withProductsIds(ProductMapper.map2TOsId(productsList03)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient02.getId(), transaction03);

		TransactionTO transaction04 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 6, 1))
				.withProductsIds(ProductMapper.map2TOsId(productsList04)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient03.getId(), transaction04);

		TransactionTO transaction05 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 0, 1))
				.withProductsIds(ProductMapper.map2TOsId(productsList05)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient04.getId(), transaction05);

		// when
		List<ClientEntity> selectedClients = clientService.find3ClientsWithMostExpensiveShoppings(START_DATE, END_DATE);

		// then
		assertTrue(!selectedClients.isEmpty());
		assertEquals(EXPECTED_01_CLIENT_ID, selectedClients.get(0).getId());
		assertEquals(EXPECTED_02_CLIENT_ID, selectedClients.get(1).getId());
		assertEquals(EXPECTED_03_CLIENT_ID, selectedClients.get(2).getId());
	}

	@Test
	@Transactional
	public void shouldReturnTransactionsByCriteria() throws Exception {
		// given
		TestData data = new TestData();
		data.initialize();
		
		final Calendar START_DATE = new GregorianCalendar(2018, 5, 1);
		final Calendar END_DATE = new GregorianCalendar(2018, 7, 31);
		final Long EXPECTED_01_TRANSACTION_ID = new Long(2);
		final Long EXPECTED_02_TRANSACTION_ID = new Long(3);
		final BigDecimal LOWER_COST = new BigDecimal("200.0");
		final BigDecimal UPPER_COST = new BigDecimal("10000.0");

		ProductTO savedProduct01 = productService.save(data.getProductById(0));
		ProductTO savedProduct02 = productService.save(data.getProductById(2));
		ProductTO savedProduct03 = productService.save(data.getProductById(1));
		ClientTO savedClient01 = clientService.save(data.getClientById(0));
		ClientTO savedClient02 = clientService.save(data.getClientById(1));

		TransactionSearchCriteriaTO criteria = new TransactionSearchCriteriaTO(savedClient02.getLastName(), START_DATE,
				END_DATE, savedProduct02.getId(), LOWER_COST, UPPER_COST);
				
		List<ProductTO> productsList01 = new ArrayList<>();
		productsList01.add(savedProduct02);
		List<ProductTO> productsList02 = new ArrayList<>();
		productsList02.add(savedProduct02);
		productsList02.add(savedProduct03);
		List<ProductTO> productsList03 = new ArrayList<>();
		productsList03.add(savedProduct01);
		productsList03.add(savedProduct02);
		List<ProductTO> productsList04 = new ArrayList<>();
		productsList04.add(savedProduct01);
		productsList04.add(savedProduct02);
		productsList04.add(savedProduct03);
		List<ProductTO> productsList05 = new ArrayList<>();
		productsList05.add(savedProduct01);
		productsList05.add(savedProduct03);
		List<ProductTO> productsList06 = new ArrayList<>();
		productsList06.add(savedProduct01);
		productsList06.add(savedProduct02);
		productsList06.add(savedProduct03);

		TransactionTO transaction01 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 5, 1))
				.withProductsIds(ProductMapper.map2TOsId(productsList01)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient02.getId(), transaction01);

		TransactionTO transaction02 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 6, 12))
				.withProductsIds(ProductMapper.map2TOsId(productsList02)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient02.getId(), transaction02);

		TransactionTO transaction03 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 6, 20))
				.withProductsIds(ProductMapper.map2TOsId(productsList03)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient02.getId(), transaction03);

		TransactionTO transaction04 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 0, 1))
				.withProductsIds(ProductMapper.map2TOsId(productsList04)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient02.getId(), transaction04);

		TransactionTO transaction05 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 6, 1))
				.withProductsIds(ProductMapper.map2TOsId(productsList05)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient02.getId(), transaction05);

		TransactionTO transaction06 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 7, 1))
				.withProductsIds(ProductMapper.map2TOsId(productsList06)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction06);

		// when
		List<TransactionEntity> selectedTransactions = clientService.findTransactionsByCriteria(criteria);

		// then
		assertTrue(!selectedTransactions.isEmpty());
		assertEquals(EXPECTED_01_TRANSACTION_ID, selectedTransactions.get(0).getId());
		assertEquals(EXPECTED_02_TRANSACTION_ID, selectedTransactions.get(1).getId());
	}

	@Test
	@Transactional
	public void shouldReturnTransactionsBy2Criteria() throws Exception {
		// given
		TestData data = new TestData();
		data.initialize();
		
		final Calendar START_DATE = new GregorianCalendar(2018, 5, 1);
		final Calendar END_DATE = new GregorianCalendar(2018, 7, 31);
		final Long EXPECTED_01_TRANSACTION_ID = new Long(2);
		final Long EXPECTED_02_TRANSACTION_ID = new Long(3);
		final Long EXPECTED_03_TRANSACTION_ID = new Long(5);
		final Long EXPECTED_04_TRANSACTION_ID = new Long(6);
		final BigDecimal LOWER_COST = new BigDecimal("200.0");
		final BigDecimal UPPER_COST = new BigDecimal("10000.0");

		ProductTO savedProduct01 = productService.save(data.getProductById(0));
		ProductTO savedProduct02 = productService.save(data.getProductById(2));
		ProductTO savedProduct03 = productService.save(data.getProductById(1));
		ClientTO savedClient01 = clientService.save(data.getClientById(0));
		ClientTO savedClient02 = clientService.save(data.getClientById(1));

		TransactionSearchCriteriaTO criteria = new TransactionSearchCriteriaTO(null, START_DATE,
				END_DATE, null, LOWER_COST, UPPER_COST);
				
		List<ProductTO> productsList01 = new ArrayList<>();
		productsList01.add(savedProduct02);
		List<ProductTO> productsList02 = new ArrayList<>();
		productsList02.add(savedProduct02);
		productsList02.add(savedProduct03);
		List<ProductTO> productsList03 = new ArrayList<>();
		productsList03.add(savedProduct01);
		productsList03.add(savedProduct02);
		List<ProductTO> productsList04 = new ArrayList<>();
		productsList04.add(savedProduct01);
		productsList04.add(savedProduct02);
		productsList04.add(savedProduct03);
		List<ProductTO> productsList05 = new ArrayList<>();
		productsList05.add(savedProduct01);
		productsList05.add(savedProduct03);
		List<ProductTO> productsList06 = new ArrayList<>();
		productsList06.add(savedProduct01);
		productsList06.add(savedProduct02);
		productsList06.add(savedProduct03);

		TransactionTO transaction01 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 5, 1))
				.withProductsIds(ProductMapper.map2TOsId(productsList01)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient02.getId(), transaction01);

		TransactionTO transaction02 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 6, 12))
				.withProductsIds(ProductMapper.map2TOsId(productsList02)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient02.getId(), transaction02);

		TransactionTO transaction03 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 6, 20))
				.withProductsIds(ProductMapper.map2TOsId(productsList03)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient02.getId(), transaction03);

		TransactionTO transaction04 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 0, 1))
				.withProductsIds(ProductMapper.map2TOsId(productsList04)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient02.getId(), transaction04);

		TransactionTO transaction05 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 6, 1))
				.withProductsIds(ProductMapper.map2TOsId(productsList05)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient02.getId(), transaction05);

		TransactionTO transaction06 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 7, 1))
				.withProductsIds(ProductMapper.map2TOsId(productsList06)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction06);

		// when
		List<TransactionEntity> selectedTransactions = clientService.findTransactionsByCriteria(criteria);

		// then
		assertTrue(!selectedTransactions.isEmpty());
		assertEquals(EXPECTED_01_TRANSACTION_ID, selectedTransactions.get(0).getId());
		assertEquals(EXPECTED_02_TRANSACTION_ID, selectedTransactions.get(1).getId());
		assertEquals(EXPECTED_03_TRANSACTION_ID, selectedTransactions.get(2).getId());
		assertEquals(EXPECTED_04_TRANSACTION_ID, selectedTransactions.get(3).getId());
	}

	@Test
	@Transactional
	public void shouldReturnTransactionsCriteriaNull() throws Exception {
		// given
		TestData data = new TestData();
		data.initialize();

		ProductTO savedProduct01 = productService.save(data.getProductById(0));
		ProductTO savedProduct02 = productService.save(data.getProductById(2));
		ProductTO savedProduct03 = productService.save(data.getProductById(1));
		ClientTO savedClient01 = clientService.save(data.getClientById(0));
		ClientTO savedClient02 = clientService.save(data.getClientById(1));

		TransactionSearchCriteriaTO criteria = new TransactionSearchCriteriaTO(null, null, null, null, null, null);
				
		List<ProductTO> productsList01 = new ArrayList<>();
		productsList01.add(savedProduct02);
		List<ProductTO> productsList02 = new ArrayList<>();
		productsList02.add(savedProduct02);
		productsList02.add(savedProduct03);
		List<ProductTO> productsList03 = new ArrayList<>();
		productsList03.add(savedProduct01);
		productsList03.add(savedProduct02);
		List<ProductTO> productsList04 = new ArrayList<>();
		productsList04.add(savedProduct01);
		productsList04.add(savedProduct02);
		productsList04.add(savedProduct03);
		List<ProductTO> productsList05 = new ArrayList<>();
		productsList05.add(savedProduct01);
		productsList05.add(savedProduct03);
		List<ProductTO> productsList06 = new ArrayList<>();
		productsList06.add(savedProduct01);
		productsList06.add(savedProduct02);
		productsList06.add(savedProduct03);

		TransactionTO transaction01 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 5, 1))
				.withProductsIds(ProductMapper.map2TOsId(productsList01)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient02.getId(), transaction01);

		TransactionTO transaction02 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 6, 12))
				.withProductsIds(ProductMapper.map2TOsId(productsList02)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient02.getId(), transaction02);

		TransactionTO transaction03 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 6, 20))
				.withProductsIds(ProductMapper.map2TOsId(productsList03)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient02.getId(), transaction03);

		TransactionTO transaction04 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 0, 1))
				.withProductsIds(ProductMapper.map2TOsId(productsList04)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient02.getId(), transaction04);

		TransactionTO transaction05 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 6, 1))
				.withProductsIds(ProductMapper.map2TOsId(productsList05)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient02.getId(), transaction05);

		TransactionTO transaction06 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 7, 1))
				.withProductsIds(ProductMapper.map2TOsId(productsList06)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction06);

		// when
		List<TransactionEntity> selectedTransactions = clientService.findTransactionsByCriteria(criteria);

		// then
		assertTrue(selectedTransactions.isEmpty());
	}

}
