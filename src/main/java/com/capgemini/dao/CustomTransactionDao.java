package com.capgemini.dao;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

import com.capgemini.domain.TransactionEntity;
import com.capgemini.types.TransactionSearchCriteriaTO;

public interface CustomTransactionDao {
	
	List<TransactionEntity> findByCriteria(TransactionSearchCriteriaTO criteria);

	BigDecimal findTotalCostByClient(Long id);
	BigDecimal findCostByClientAndStatus(Long id, String status);
	BigDecimal findTotalCostByStatus(String status);
	BigDecimal findProfitByPeriod(YearMonth startPeriod, YearMonth endPeriod);
	
}
