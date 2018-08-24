package com.capgemini.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capgemini.dao.ClientDao;
import com.capgemini.dao.ProductDao;
import com.capgemini.dao.TransactionDao;
import com.capgemini.domain.ClientEntity;
import com.capgemini.domain.ProductEntity;
import com.capgemini.exceptions.HighPrizeException;
import com.capgemini.exceptions.TransactionHistoryException;
import com.capgemini.mappers.ClientMapper;
import com.capgemini.mappers.TransactionMapper;
import com.capgemini.service.ClientService;
import com.capgemini.types.ClientTO;
import com.capgemini.types.TransactionTO;
import com.capgemini.types.TransactionTO.TransactionTOBuilder;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

	private static final BigDecimal MAX_PRIZE_FOR_EXPENSIVE_PRODUCTS = new BigDecimal("7000.00");
	private static final BigDecimal MAX_TOTAL_COST_BELOW_3_TRANSATIONS = new BigDecimal("5000.00");
	private static final Integer MAX_EXPENSIVE_PRODUCTS = 5;
	private static final long MIN_TRANSACTIONS_TO_BUY_OVER_MAX_TOTAL_COST = 2;
	private static final Double MAX_TRANSACTION_PRODUCTS_WEIGHT = 25.0;

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
		List<TransactionTO> transactions = verifyWeigth(transaction);
		ClientEntity clientEntity = clientDao.findById(clientId);
		for (TransactionTO veryfiedTransaction : transactions) {
			clientEntity.addTransaction(TransactionMapper.toTransactionEntity(veryfiedTransaction));
		}
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
		if (findClientTransactionsNo(clientId) < MIN_TRANSACTIONS_TO_BUY_OVER_MAX_TOTAL_COST
				&& totalPrize.compareTo(MAX_TOTAL_COST_BELOW_3_TRANSATIONS) == 1) {
			return true;
		}
		return false;
	}

	private boolean verifyIfOver5ExpensiveItems(TransactionTO transaction) {
		List<Long> productsList = transaction.getProductsId();
		Set<Long> productsSet = productsList.stream().collect(Collectors.toSet());
		for (Long i : productsSet) {
			if (Collections.frequency(productsList, i) > MAX_EXPENSIVE_PRODUCTS
					&& productDao.findById(i).getUnitPrice().compareTo(MAX_PRIZE_FOR_EXPENSIVE_PRODUCTS) == 1) {
				return true;
			}
		}
		return false;
	}

	private List<TransactionTO> verifyWeigth(TransactionTO transaction) {
		List<Long> productsId = transaction.getProductsId();
		List<TransactionTO> verifiedTransactions = new ArrayList<>();
		List<Long> temporaryProductsId = new ArrayList<>();
		Double tempTransactionWeight = 0.0;
		Iterator<Long> selectedProductId = productsId.iterator();
		while (selectedProductId.hasNext()) {
			ProductEntity selectedProduct = productDao.findById(selectedProductId.next());
			if (tempTransactionWeight + selectedProduct.getWeigth().doubleValue() > MAX_TRANSACTION_PRODUCTS_WEIGHT
					|| !selectedProductId.hasNext()) {
				TransactionTO temporaryTransaction = new TransactionTOBuilder().withClientId(transaction.getClientId())
						.withDate(transaction.getDate()).withStatus(transaction.getStatus())
						.withProductsIds(temporaryProductsId).build();
				verifiedTransactions.add(temporaryTransaction);
				temporaryProductsId.clear();
				tempTransactionWeight = 0.0;
			} else {
				temporaryProductsId.add(selectedProductId.next());
				tempTransactionWeight += selectedProduct.getWeigth().doubleValue();
			}
		}
		return verifiedTransactions;
	}

}
