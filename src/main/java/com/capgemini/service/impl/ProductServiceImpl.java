package com.capgemini.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capgemini.dao.ProductDao;
import com.capgemini.dao.TransactionDao;
import com.capgemini.domain.ProductEntity;
import com.capgemini.mappers.ProductMapper;
import com.capgemini.service.ProductService;
import com.capgemini.types.ProductTO;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	ProductDao productDao;
	
	@Autowired
	TransactionDao transactionDao;

	@Override
	public ProductTO findById(Long id) {
		ProductEntity entity = productDao.findById(id);
		return ProductMapper.toProductTO(entity);
	}

	@Override
	public ProductTO save(ProductTO productTO) {
		ProductEntity entity = ProductMapper.toProductEntity(productTO);
		for (Long i : productTO.getTransactionsId()) {
			entity.addTransaction(transactionDao.findById(i));
		}
		ProductEntity savedEntity = productDao.save(entity);
		return ProductMapper.toProductTO(savedEntity);
	}

	@Override
	public long findProductsNo() {
		return productDao.count();
	}

	@Override
	public List<ProductTO> find10BestSellingProducts() {
		return ProductMapper.map2TOs(productDao.find10BestSellingProducts());
	}

}
