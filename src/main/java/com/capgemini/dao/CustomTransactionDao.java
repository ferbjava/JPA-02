package com.capgemini.dao;

import java.util.List;

import com.capgemini.domain.TransactionEntity;
import com.capgemini.types.TransactionSearchCriteriaTO;

public interface CustomTransactionDao {
	
	List<TransactionEntity> findByCriteria(TransactionSearchCriteriaTO criteria);
	
}
