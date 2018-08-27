package com.capgemini.dao;

import java.util.List;

import com.capgemini.domain.ProductEntity;
import com.capgemini.types.SelectedProductTO;

public interface CustomProductDao {

	List<ProductEntity> find10BestSellingProducts();
	List<SelectedProductTO> findProductsInImplementation();

}
