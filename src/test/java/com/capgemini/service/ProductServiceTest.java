package com.capgemini.service;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.capgemini.types.ProductTO;
import com.capgemini.types.ProductTO.ProductTOBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTest {

	@Autowired
	private ProductService productService;
	
	@Test
	@Transactional
	public void shouldAddProduct(){
		// given
		final long EXPECTED_INITIAL_PRODUCTS = 0;
		final long EXPECTED_FINAL_PRODUCTS = 1;
		
		long initalProducts = productService.findProductsNo();
		// when
		ProductTO product01 = new ProductTOBuilder().withName("klocki").withUnitPrice(new BigDecimal("12.45")).withMargin(30).withWeigth(new BigDecimal("0.23")).build();
		productService.save(product01);
		long finalProducts = productService.findProductsNo();
		
		// then
		assertEquals(EXPECTED_INITIAL_PRODUCTS, initalProducts);
		assertEquals(EXPECTED_FINAL_PRODUCTS, finalProducts);
	}
	
}
