package com.capgemini.dao.impl;

import java.math.BigDecimal;
import java.util.Calendar;
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
	public BigDecimal findTransactionsCostByClient(Long id) {
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
	public BigDecimal findProfitByPeriod(Calendar startPeriod, Calendar endPeriod) {
		// TODO Auto-generated method stub
		return null;
	}

}
