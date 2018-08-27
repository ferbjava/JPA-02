package com.capgemini.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.capgemini.dao.CustomProductDao;
import com.capgemini.domain.ProductEntity;
import com.capgemini.domain.QProductEntity;
import com.capgemini.domain.QTransactionEntity;
import com.capgemini.types.SelectedProductTO;
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

	@Override
	public List<SelectedProductTO> findProductsInImplementation() {
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		QProductEntity product = QProductEntity.productEntity;
		QTransactionEntity transaction = QTransactionEntity.transactionEntity;
		NumberPath<Long> count = Expressions.numberPath(Long.class, "c");
		String status = "In implementation";
		List<SelectedProductTO> selectedProducts = new ArrayList<>();
		
		List<Tuple> productsTuple = queryFactory.select(product.name, product.id.count().as(count))
										.from(product)
										.join(product.transactions,transaction)
										.where(transaction.status.eq(status))
										.groupBy(product.name)
										.fetch();
		
		for(Tuple t:productsTuple){
			selectedProducts.add(new SelectedProductTO(t.get(product.name), t.get(count)));
		}
		
		return selectedProducts;
	}

}
