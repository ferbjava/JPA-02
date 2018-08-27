package com.capgemini.dao.impl;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.capgemini.dao.CustomTransactionDao;
import com.capgemini.domain.QClientEntity;
import com.capgemini.domain.QProductEntity;
import com.capgemini.domain.QTransactionEntity;
import com.capgemini.domain.TransactionEntity;
import com.capgemini.types.TransactionSearchCriteriaTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class TransactionDaoImpl extends AbstractDao<TransactionEntity, Long> implements CustomTransactionDao {

	@Override
	public List<TransactionEntity> findByCriteria(TransactionSearchCriteriaTO criteria) {
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		JPAQueryFactory queryPrice = new JPAQueryFactory(entityManager);
		QTransactionEntity transaction = QTransactionEntity.transactionEntity;
		QProductEntity product = QProductEntity.productEntity;
		QClientEntity client = QClientEntity.clientEntity;
		List<TransactionEntity> transactionsByTotalPrice = new ArrayList<>();
		
		BooleanBuilder options = new BooleanBuilder();
		
		if(criteria.getLastName()==null&&criteria.getProductId()==null&&criteria.getStartDate()==null&&criteria.getEndDate()==null&&criteria.getLowerCost()==null&&criteria.getUpperCost()==null){
			return new ArrayList<>();
		}
		
		if (criteria.getLastName() != null) {
			options.and(client.lastName.eq(criteria.getLastName()));
		}

		if (criteria.getProductId() != null) {
			options.and(product.id.eq(criteria.getProductId()));
		}

		if (criteria.getStartDate() != null && criteria.getEndDate() != null) {
			options.and(transaction.date.between(criteria.getStartDate(), criteria.getEndDate()));
		}
		
		if (criteria.getLowerCost() != null && criteria.getUpperCost() != null) {
			List<Tuple> tupleByPrice = queryPrice.select(transaction, product.price.sum())
					.from(transaction)
					.join(transaction.products,product)
					.groupBy(transaction.id)
					.having(product.price.sum().between(criteria.getLowerCost(), criteria.getUpperCost()))
					.fetch();
			for(Tuple t:tupleByPrice){
				transactionsByTotalPrice.add(t.get(transaction));
			}
			options.and(transaction.in(transactionsByTotalPrice));
		}
		
		List<TransactionEntity> transactions = queryFactory.select(transaction)
				.from(transaction)
				.join(transaction.products,product)
				.join(transaction.client, client)
				.where(options)
				.groupBy(transaction.id)
				.fetch();
		
		return transactions;
	}
	
	@Override
	public BigDecimal findTotalCostByClient(Long id) {
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		QTransactionEntity transaction = QTransactionEntity.transactionEntity;
		QProductEntity product = QProductEntity.productEntity;
		QClientEntity client = QClientEntity.clientEntity;
		
		BigDecimal clientCost = queryFactory.select(product.price.sum())
				.from(product)
				.join(product.transactions,transaction)
				.join(transaction.client, client)
				.where(client.id.eq(id))
				.fetch()
				.get(0);
		return clientCost;
	}

	@Override
	public BigDecimal findProfitByPeriod(YearMonth start, YearMonth end) {
		Calendar startDate = new GregorianCalendar(start.getYear(), start.getMonthValue()-1, 1);
		Calendar endDate = new GregorianCalendar(end.getYear(), end.getMonthValue()-1, end.lengthOfMonth());
		
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		QTransactionEntity transaction = QTransactionEntity.transactionEntity;
		QProductEntity product = QProductEntity.productEntity;
		
		BigDecimal profit = 
				queryFactory.select(product.price.multiply(product.margin.divide(100)).sum())
				.from(product)
				.join(product.transactions,transaction)
				.where(transaction.date.between(startDate, endDate))
				.fetch()
				.get(0)
				;
		
		return profit;
	}

	@Override
	public BigDecimal findCostByClientAndStatus(Long id, String status) {
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		QTransactionEntity transaction = QTransactionEntity.transactionEntity;
		QProductEntity product = QProductEntity.productEntity;
		QClientEntity client = QClientEntity.clientEntity;
		
		BigDecimal clientCost = 
				queryFactory.select(product.price.sum())
				.from(product)
				.join(product.transactions,transaction)
				.join(transaction.client,client)
				.where(client.id.eq(id).and(transaction.status.eq(status)))
				.fetch()
				.get(0);
		
		return clientCost;
	}

	@Override
	public BigDecimal findTotalCostByStatus(String status) {
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		QTransactionEntity transaction = QTransactionEntity.transactionEntity;
		QProductEntity product = QProductEntity.productEntity;
		
		BigDecimal clientCost =  
				queryFactory.select(product.price.sum())
				.from(product)
				.join(product.transactions,transaction)
				.where(transaction.status.eq(status))
				.fetch()
				.get(0);
		
		return clientCost;
	}

}
