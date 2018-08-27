package com.capgemini.dao;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import com.capgemini.domain.TransactionEntity;
import com.capgemini.types.TransactionSearchCriteriaTO;

public interface CustomTransactionDao {
	
	List<TransactionEntity> findByCriteria(TransactionSearchCriteriaTO criteria);

	BigDecimal findTransactionsCostByClient(Long id);
	BigDecimal findProfitByPeriod(Calendar startPeriod, Calendar endPeriod);
	
}
