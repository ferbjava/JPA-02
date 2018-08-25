package com.capgemini.service;

import java.util.List;

import com.capgemini.types.ProductTO;

public interface ProductService {

	ProductTO findById(Long id);
	ProductTO save(ProductTO productTO);

	long findProductsNo();

	List<ProductTO> find10BestSellingProducts();

}
