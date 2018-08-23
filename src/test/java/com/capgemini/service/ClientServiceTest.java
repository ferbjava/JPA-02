package com.capgemini.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

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
	public void shouldAddClient(){
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
	public void shouldFailOptimisticLocking(){
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
//		selectedClient01_01.setFirstName("Jarogniew");
		ClientTO updatedClient01_01 = clientService.save(selectedClient01_01);
		long clientsAfterUpdate = clientService.findClientsNo();
		int versionUpdated01_01 = updatedClient01_01.getVersion();
		
		boolean isException = false;
		try{
			clientService.save(selectedClient01_02);
		}catch (OptimisticLockingFailureException ex){
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
	public void shouldAddTransactionToClient() {
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
		TransactionTO transaction = new TransactionTOBuilder().withClientId(savedClient01.getId())
				.withDate(new GregorianCalendar(2018, 7, 23)).withProductsIds(ProductMapper.map2TOsId(productsList))
				.withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction);
		long finalTransactions = clientService.findClientTransactionsNo(savedClient01.getId());

		// then
		assertEquals(EXPECTED_INITIAL_TRANSACTIONS, initialTransactions);
		assertEquals(EXPECTED_FINAL_TRANSACTIONS, finalTransactions);
	}
	
	@Test
	@Transactional
	public void shouldRemoveClientCascadeTest() {
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
	
}
