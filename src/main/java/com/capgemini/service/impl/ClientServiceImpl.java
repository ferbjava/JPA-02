package com.capgemini.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capgemini.dao.ClientDao;
import com.capgemini.dao.ProductDao;
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

	@Autowired
	ProductDao productDao;

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
		ClientEntity clientEntity = clientDao.findById(clientId);
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
		if (verifyIfBelow3Transactions(clientId, transaction)) {
			throw new TransactionHistoryException();
		}
		if (verifyIfOver5ExpensiveItems(transaction)) {
			throw new HighPrizeException();
		}
	}

	private boolean verifyIfBelow3Transactions(Long clientId, TransactionTO transaction) {
		List<Long> productList = transaction.getProductsId();
		BigDecimal totalPrize = new BigDecimal("0.0");
		for (Long i : productList) {
			totalPrize = totalPrize.add(productDao.findById(i).getUnitPrice());
		}
		System.out.println("Client transactions: "+findClientTransactionsNo(clientId));
		if (findClientTransactionsNo(clientId) < 3 && totalPrize.compareTo(new BigDecimal("5000.0")) == 1) {
			return true;
		}
		return false;
	}

	private boolean verifyIfOver5ExpensiveItems(TransactionTO transaction) {
		List<Long> productsList = transaction.getProductsId();
		for (Long i : productsList) {
			if (Collections.frequency(productsList, i) > 5
					&& productDao.findById(i).getUnitPrice().compareTo(new BigDecimal("7000.0")) == 1) {
				return true;
			}
		}
		return false;
	}

}
