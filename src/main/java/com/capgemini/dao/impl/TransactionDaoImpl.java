package com.capgemini.dao.impl;

import java.util.List;

import com.capgemini.dao.CustomTransactionDao;
import com.capgemini.domain.TransactionEntity;
import com.capgemini.types.TransactionSearchCriteriaTO;

public class TransactionDaoImpl extends AbstractDao<TransactionEntity, Long> implements CustomTransactionDao {

	@Override
	public List<TransactionEntity> findByCriteria(TransactionSearchCriteriaTO criteria) {
		/*TODO*/
		return null;
	}

}
