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
import com.capgemini.types.SelectedProductTO;
import com.capgemini.types.TransactionTO;
import com.capgemini.types.TransactionTO.TransactionTOBuilder;
import com.capgemini.utils.TestData;


@RunWith(SpringRunner.class)
@SpringBootTest (properties = "spring.profiles.active=hsql")
public class ProductServiceTest {

	@Autowired
	private ProductService productService;

	@Autowired
	private ClientService clientService;
	
	@Test
	@Transactional
	public void shouldAddProduct(){
		// given
		TestData data = new TestData();
		data.initialize();
		
		final long EXPECTED_INITIAL_PRODUCTS = 0;
		final long EXPECTED_FINAL_PRODUCTS = 1;
		
		long initalProducts = productService.findProductsNo();
		// when
		productService.save(data.getProductById(0));
		long finalProducts = productService.findProductsNo();
		
		// then
		assertEquals(EXPECTED_INITIAL_PRODUCTS, initalProducts);
		assertEquals(EXPECTED_FINAL_PRODUCTS, finalProducts);
	}
	
	@Test(expected = OptimisticLockingFailureException.class)
	@Transactional
	public void shouldFailProductOptimisticLocking(){
		// given
		TestData data = new TestData();
		data.initialize();
		
		ProductTO savedProduct = productService.save(data.getProductById(0));
		ProductTO selectedProduct01_01 = productService.findById(savedProduct.getId());
		ProductTO selectedProduct01_02 = productService.findById(savedProduct.getId());
		
		// when
		selectedProduct01_01.setName("Biokukla");
		selectedProduct01_02.setName("Wakuola");
		
		productService.save(selectedProduct01_01);
		productService.findProductsNo();
		productService.save(selectedProduct01_02);
	}
	
	@Test
	@Transactional
	public void shouldFind10BestSellingProducts() throws Exception {
		// given
		TestData data = new TestData();
		data.initialize();
		final int EXPECTED_RETURN_PRODUCTS = 10;
		
		ClientTO savedClient01 = clientService.save(data.getClientById(0));
		clientService.save(data.getClientById(1));
		
		ProductTO savedProduct01 = productService.save(data.getProductById(0));
		ProductTO savedProduct02 = productService.save(data.getProductById(1));
		ProductTO savedProduct03 = productService.save(data.getProductById(2));
		ProductTO savedProduct04 = productService.save(data.getProductById(3));
		ProductTO savedProduct05 = productService.save(data.getProductById(4));
		ProductTO savedProduct06 = productService.save(data.getProductById(5));
		ProductTO savedProduct07 = productService.save(data.getProductById(6));
		ProductTO savedProduct08 = productService.save(data.getProductById(7));
		ProductTO savedProduct09 = productService.save(data.getProductById(8));
		ProductTO savedProduct10 = productService.save(data.getProductById(9));
		ProductTO savedProduct11 = productService.save(data.getProductById(10));
		productService.save(data.getProductById(11));
		
		final Long EXPECTED_1_BESTSELLER_ID = savedProduct01.getId();
		final Long EXPECTED_2_BESTSELLER_ID = savedProduct02.getId();
		final Long EXPECTED_3_BESTSELLER_ID = savedProduct03.getId();
		final Long EXPECTED_4_BESTSELLER_ID = savedProduct09.getId();
		final Long EXPECTED_5_BESTSELLER_ID = savedProduct05.getId();
		final Long EXPECTED_6_BESTSELLER_ID = savedProduct06.getId();
		
		List<ProductTO> productsList01 = new ArrayList<>();
		productsList01.add(savedProduct01);
		
		List<ProductTO> productsList02 = new ArrayList<>();
		productsList02.add(savedProduct01);
		productsList02.add(savedProduct02);
		
		List<ProductTO> productsList03 = new ArrayList<>();
		productsList03.add(savedProduct01);
		productsList03.add(savedProduct02);
		productsList03.add(savedProduct03);
		
		List<ProductTO> productsList04 = new ArrayList<>();
		productsList04.add(savedProduct01);
		productsList04.add(savedProduct02);
		productsList04.add(savedProduct03);
		productsList04.add(savedProduct05);
		productsList04.add(savedProduct06);
		productsList04.add(savedProduct07);
		productsList04.add(savedProduct08);
		productsList04.add(savedProduct09);
		productsList04.add(savedProduct10);
		productsList04.add(savedProduct11);
		
		List<ProductTO> productsList05 = new ArrayList<>();
		productsList05.add(savedProduct01);
		productsList05.add(savedProduct02);
		productsList05.add(savedProduct03);
		productsList05.add(savedProduct04);
		productsList05.add(savedProduct05);
		productsList05.add(savedProduct06);
		productsList05.add(savedProduct07);
		productsList05.add(savedProduct08);
		productsList05.add(savedProduct09);
		productsList05.add(savedProduct09);
		productsList05.add(savedProduct10);
		productsList05.add(savedProduct11);
		
		TransactionTO transaction01 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 7, 21))
				.withProductsIds(ProductMapper.map2TOsId(productsList01)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction01);
		
		TransactionTO transaction02 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 7, 22))
				.withProductsIds(ProductMapper.map2TOsId(productsList02)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction02);
		
		TransactionTO transaction03 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 7, 23))
				.withProductsIds(ProductMapper.map2TOsId(productsList03)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction03);
		
		TransactionTO transaction04 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 7, 24))
				.withProductsIds(ProductMapper.map2TOsId(productsList04)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction04);
		
		TransactionTO transaction05 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 7, 25))
				.withProductsIds(ProductMapper.map2TOsId(productsList05)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction05);
		
		// when
		List<ProductTO>bestSellingProducts = productService.find10BestSellingProducts();
		
		// then
		assertEquals(EXPECTED_RETURN_PRODUCTS, bestSellingProducts.size());
		assertEquals(EXPECTED_1_BESTSELLER_ID, bestSellingProducts.get(0).getId());
		assertEquals(EXPECTED_2_BESTSELLER_ID, bestSellingProducts.get(1).getId());
		assertEquals(EXPECTED_3_BESTSELLER_ID, bestSellingProducts.get(2).getId());
		assertEquals(EXPECTED_4_BESTSELLER_ID, bestSellingProducts.get(3).getId());
		assertEquals(EXPECTED_5_BESTSELLER_ID, bestSellingProducts.get(4).getId());
		assertEquals(EXPECTED_6_BESTSELLER_ID, bestSellingProducts.get(5).getId());
	}

	@Test
	@Transactional
	public void shouldReturnProductsInImplementation() throws Exception {
		// given
		TestData data = new TestData();
		data.initialize();
		
		final int EXPECTED_PRODUCT_NO = 3;
		final Long EXPECTED_PRODUCT01_NO = new Long(2);
		final Long EXPECTED_PRODUCT02_NO = new Long(1);
		final Long EXPECTED_PRODUCT03_NO = new Long(1);
		final String STATUS_IN_IMPLEMENTATION = "In implementation";

		ProductTO savedProduct01 = productService.save(data.getProductById(0));
		ProductTO savedProduct02 = productService.save(data.getProductById(1));
		ProductTO savedProduct03 = productService.save(data.getProductById(2));
		
		List<ProductTO> productsList01 = new ArrayList<>();
		productsList01.add(savedProduct01);
		productsList01.add(savedProduct02);
		productsList01.add(savedProduct03);
		List<ProductTO> productsList02 = new ArrayList<>();
		productsList02.add(savedProduct01);
		productsList02.add(savedProduct02);
		List<ProductTO> productsList03 = new ArrayList<>();
		productsList03.add(savedProduct01);
		productsList03.add(savedProduct03);
		ClientTO savedClient01 = clientService.save(data.getClientById(0));
		
		final SelectedProductTO product01 = new SelectedProductTO(savedProduct01.getName(), EXPECTED_PRODUCT01_NO);
		final SelectedProductTO product02 = new SelectedProductTO(savedProduct02.getName(), EXPECTED_PRODUCT02_NO);
		final SelectedProductTO product03 = new SelectedProductTO(savedProduct03.getName(), EXPECTED_PRODUCT03_NO);

		TransactionTO transaction01 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 0, 1))
				.withProductsIds(ProductMapper.map2TOsId(productsList01)).withStatus("Completed").build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction01);

		TransactionTO transaction02 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 6, 12))
				.withProductsIds(ProductMapper.map2TOsId(productsList02)).withStatus(STATUS_IN_IMPLEMENTATION).build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction02);

		TransactionTO transaction03 = new TransactionTOBuilder().withDate(new GregorianCalendar(2018, 7, 29))
				.withProductsIds(ProductMapper.map2TOsId(productsList03)).withStatus(STATUS_IN_IMPLEMENTATION).build();
		clientService.addTransactionToClient(savedClient01.getId(), transaction03);

		// when
		List<SelectedProductTO> selectedProducts = productService.findProductsInImplementation();

		// then
		assertEquals(EXPECTED_PRODUCT_NO, selectedProducts.size());
		assertTrue(selectedProducts.contains(product01));
		assertTrue(selectedProducts.contains(product02));
		assertTrue(selectedProducts.contains(product03));
		assertEquals(selectedProducts.get(0).getAmount(), product01.getAmount());
		assertEquals(selectedProducts.get(1).getAmount(), product02.getAmount());
		assertEquals(selectedProducts.get(2).getAmount(), product03.getAmount());
	}
	
}
