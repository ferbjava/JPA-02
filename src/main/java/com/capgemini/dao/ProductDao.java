package com.capgemini.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.capgemini.domain.ProductEntity;

@Repository
public interface ProductDao extends CrudRepository<ProductEntity, Long>, CustomProductDao {
	
	ProductEntity findById(Long id);
	
}
