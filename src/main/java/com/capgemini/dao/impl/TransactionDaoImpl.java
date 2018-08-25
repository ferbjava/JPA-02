package com.capgemini.dao.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.TypedQuery;

import com.capgemini.dao.CustomTransactionDao;
import com.capgemini.domain.ClientEntity;
import com.capgemini.domain.TransactionEntity;
import com.capgemini.types.TransactionSearchCriteriaTO;

public class TransactionDaoImpl extends AbstractDao<TransactionEntity, Long> implements CustomTransactionDao {

	@Override
	public List<TransactionEntity> findByCriteria(TransactionSearchCriteriaTO criteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal findCostOfTransactionsByClient(Long id) {
		ClientEntity client = entityManager.getReference(ClientEntity.class, id);
		TypedQuery<BigDecimal> query = entityManager.createQuery(
				"SELECT SUM(tp.price) FROM TransactionEntity t"
				+ " JOIN t.products tp"
				+ " WHERE :client MEMBER OF t.client",
				BigDecimal.class);
		return query.setParameter("client", client).getSingleResult();
	}

}
