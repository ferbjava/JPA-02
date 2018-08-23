package com.capgemini.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capgemini.dao.ProductDao;
import com.capgemini.domain.ProductEntity;
import com.capgemini.mappers.ProductMapper;
import com.capgemini.service.ProductService;
import com.capgemini.types.ProductTO;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	ProductDao productDao;

	@Override
	public ProductTO findById(Long id) {
		ProductEntity entity = productDao.findById(id);
		return ProductMapper.toProductTO(entity);
	}

	@Override
	public ProductTO save(ProductTO productTO) {
		ProductEntity entity = productDao.save(ProductMapper.toProductEntity(productTO));
		return ProductMapper.toProductTO(entity);
	}

	@Override
	public long findProductsNo() {
		return productDao.count();
	}

}
