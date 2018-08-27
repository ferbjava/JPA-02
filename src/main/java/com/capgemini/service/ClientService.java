package com.capgemini.service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

import com.capgemini.domain.ClientEntity;
import com.capgemini.domain.TransactionEntity;
import com.capgemini.types.ClientTO;
import com.capgemini.types.TransactionSearchCriteriaTO;
import com.capgemini.types.TransactionTO;

public interface ClientService {
	
	ClientTO findClientById(Long id);
	ClientTO save(ClientTO clientTO);
	ClientTO addTransactionToClient(Long clientId, TransactionTO transaction) throws Exception;
	TransactionTO findTransactionById(Long id);
	TransactionTO saveTransaction(Long clientId, TransactionTO transactionTO);
	
	long findClientsNo();
	long findClientTransactionsNo(Long id);
	long findTransactionsNo();
	
	void removeClient(Long id);
	
	BigDecimal findProfitByPeriod(YearMonth startPeriod, YearMonth endPeriod);
	BigDecimal findCostByClient(Long id);
	BigDecimal findCostByClientAndStatus(Long id, String status);
	BigDecimal findTotalCostByStatus(String status);
	
	List<ClientEntity> find3ClientsWithMostExpensiveShoppings(YearMonth startDate, YearMonth endDate);
	List<TransactionEntity> findTransactionsByCriteria(TransactionSearchCriteriaTO criteria);
	
}
