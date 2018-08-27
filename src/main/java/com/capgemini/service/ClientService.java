package com.capgemini.service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.List;

import com.capgemini.domain.ClientEntity;
import com.capgemini.types.ClientTO;
import com.capgemini.types.TransactionTO;

public interface ClientService {
	
	ClientTO findClientById(Long id);
	ClientTO save(ClientTO clientTO);
	ClientTO addTransactionToClient(Long clientId, TransactionTO transaction) throws Exception;
	
	long findClientsNo();
	long findClientTransactionsNo(Long id);
	long findTransactionsNo();
	
	void removeClient(Long id);
	BigDecimal findCostOfTransactionsByClient(Long id);
	BigDecimal findProfitByPeriod(Calendar startPeriod, Calendar endPeriod);
	List<ClientEntity> find3ClientsWithMostExpensiveShoppings(YearMonth startDate, YearMonth endDate);
	
}
