package com.capgemini.dao.impl;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.capgemini.dao.CustomTransactionDao;
import com.capgemini.domain.QClientEntity;
import com.capgemini.domain.QProductEntity;
import com.capgemini.domain.QTransactionEntity;
import com.capgemini.domain.TransactionEntity;
import com.capgemini.types.TransactionSearchCriteriaTO;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class TransactionDaoImpl extends AbstractDao<TransactionEntity, Long> implements CustomTransactionDao {

	@Override
	public List<TransactionEntity> findByCriteria(TransactionSearchCriteriaTO criteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal findCostByClient(Long id) {
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
