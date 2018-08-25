package com.capgemini.dao;

import java.util.List;

import com.capgemini.domain.ProductEntity;

public interface CustomProductDao {

	List<ProductEntity> find10BestSellingProducts();

}
