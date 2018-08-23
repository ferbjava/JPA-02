package com.capgemini.service;

import com.capgemini.types.ProductTO;

public interface ProductService {

	ProductTO findById(Long id);
	ProductTO save(ProductTO productTO);
	long findProductsNo();
	
}
