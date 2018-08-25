package com.capgemini.dao.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import com.capgemini.dao.CustomProductDao;
import com.capgemini.domain.ProductEntity;

public class ProductDaoImpl extends AbstractDao<ProductEntity, Long> implements CustomProductDao {

	@Override
	public List<ProductEntity> find10BestSellingProducts() {
		TypedQuery<ProductEntity> query = entityManager.createQuery(
				"SELECT p FROM ProductEntity p"
				+ " JOIN FETCH p.transactions pt"
				+ " WHERE COUNT(p.transactions) > 2",
				
//				"SELECT p FROM ProductEntity p"
//				+ " WHERE COUNT(p.transactions) > 2",
				 ProductEntity.class);
		return query.getResultList();
	}

}
