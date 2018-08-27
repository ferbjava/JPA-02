package com.capgemini.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.capgemini.dao.CustomProductDao;
import com.capgemini.domain.ProductEntity;
import com.capgemini.domain.QProductEntity;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class ProductDaoImpl extends AbstractDao<ProductEntity, Long> implements CustomProductDao {

	@Override
	public List<ProductEntity> find10BestSellingProducts() {
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		QProductEntity product = QProductEntity.productEntity;
		List<ProductEntity> selectedProducts = new ArrayList<>();
		NumberPath<Long> count = Expressions.numberPath(Long.class, "c");

		List<Tuple>sortedProducts = 				
				queryFactory.select(product, product.id.count().as(count))
							.from(product)
							.join(product.transactions)
							.groupBy(product.id)
							.orderBy(count.desc(), product.id.asc())
							.limit(10)
							.fetch();

		for(Tuple t:sortedProducts){
			selectedProducts.add(t.get(product));
		}
		
		return selectedProducts;
		
	}

}
