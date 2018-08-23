package com.capgemini.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capgemini.dao.ClientDao;
import com.capgemini.dao.TransactionDao;
import com.capgemini.domain.ClientEntity;
import com.capgemini.exceptions.HighPrizeException;
import com.capgemini.exceptions.TransactionHistoryException;
import com.capgemini.mappers.ClientMapper;
import com.capgemini.mappers.TransactionMapper;
import com.capgemini.service.ClientService;
import com.capgemini.types.ClientTO;
import com.capgemini.types.TransactionTO;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

	@Autowired
	ClientDao clientDao;

	@Autowired
	TransactionDao transactionDao;

	@Override
	public ClientTO findClientById(Long id) {
		ClientEntity entity = clientDao.findById(id);
		return ClientMapper.toClientTO(entity);
	}

	@Override
	public ClientTO save(ClientTO clientTO) {
		ClientEntity entity = ClientMapper.toClientEntity(clientTO);
		for (Long i : clientTO.getTransactionsId()) {
			entity.addTransaction(transactionDao.findById(i));
		}
		ClientEntity savedEntity = clientDao.save(entity);
		return ClientMapper.toClientTO(savedEntity);
	}

	@Override
	public long findClientsNo() {
		return clientDao.count();
	}

	@Override
	public ClientTO addTransactionToClient(Long clientId, TransactionTO transaction) {
		verifyTransaction(clientId, transaction);
		ClientEntity clientEntity = ClientMapper.toClientEntity(findClientById(clientId));
		clientEntity.addTransaction(TransactionMapper.toTransactionEntity(transaction));
		ClientEntity updatedClient = clientDao.save(clientEntity);
		return ClientMapper.toClientTO(updatedClient);
	}

	@Override
	public long findClientTransactionsNo(Long id) {
		ClientTO client = findClientById(id);
		long no = client.getTransactionsId().size();
		return no;
	}

	@Override
	public void removeClient(Long id) {
		clientDao.delete(id);
	}

	@Override
	public long findTransactionsNo() {
		return transactionDao.count();
	}

	// private methods
	private void verifyTransaction(Long clientId, TransactionTO transaction) {
		if(verifyIfBelow3Transactions(clientId, transaction)){
			BigDecimal prize = new BigDecimal("0");
			throw new TransactionHistoryException(prize);
		}
		if(verifyIfOver5ExpensiveItems(transaction)){
			Long items = 0L;
			throw new HighPrizeException(items);
		}
	}

	private boolean verifyIfBelow3Transactions(Long clientId, TransactionTO transaction) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean verifyIfOver5ExpensiveItems(TransactionTO transaction) {
		// TODO Auto-generated method stub
		return false;
	}

}
