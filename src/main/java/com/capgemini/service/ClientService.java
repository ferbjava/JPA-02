package com.capgemini.service;

import java.math.BigDecimal;

import com.capgemini.types.ClientTO;
import com.capgemini.types.TransactionTO;

public interface ClientService {
	
	ClientTO findClientById(Long id);
	ClientTO save(ClientTO clientTO);
	ClientTO addTransactionToClient(Long clientId, TransactionTO transaction);
	
	long findClientsNo();
	long findClientTransactionsNo(Long id);
	long findTransactionsNo();
	
	void removeClient(Long id);
	BigDecimal findCostOfTransactionsByClient(Long id);
	
}
