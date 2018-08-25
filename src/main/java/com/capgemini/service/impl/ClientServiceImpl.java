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
import com.capgemini.domain.TransactionEntity;
import com.capgemini.exceptions.HighPrizeException;
import com.capgemini.exceptions.TransactionHistoryException;
import com.capgemini.mappers.ClientMapper;
import com.capgemini.mappers.ProductMapper;
import com.capgemini.mappers.TransactionMapper;
import com.capgemini.service.ClientService;
import com.capgemini.types.ClientTO;
import com.capgemini.types.ProductTO;
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
		for (TransactionTO veryfiedTransactionTO : transactions) {
			TransactionEntity veryfiedTransaction = TransactionMapper.toTransactionEntity(veryfiedTransactionTO);
			veryfiedTransaction.setClient(clientEntity);
			List<ProductEntity> products = veryfiedTransactionTO.getProductsId().stream().map(productDao::findById)
					.collect(Collectors.toList());
			veryfiedTransaction.addProducts(products);
			products.stream().forEach(p -> p.addTransaction(veryfiedTransaction));
			TransactionEntity savedTransaction = transactionDao.save(veryfiedTransaction);
			clientEntity.addTransaction(savedTransaction);
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

	@Override
	public BigDecimal findCostOfTransactionsByClient(Long id) {
		return transactionDao.findCostOfTransactionsByClient(id);
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
			totalPrize = totalPrize.add(productDao.findById(i).getPrice());
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
					&& productDao.findById(i).getPrice().compareTo(MAX_PRIZE_FOR_EXPENSIVE_PRODUCTS) == 1) {
				return true;
			}
		}
		return false;
	}

	private List<TransactionTO> verifyWeigth(TransactionTO transaction) {
		List<Long> productsId = transaction.getProductsId();
		List<TransactionTO> verifiedTransactions = new ArrayList<>();
		List<ProductTO> temporaryProducts = new ArrayList<>();
		Double tempTransactionWeight = 0.0;
		Iterator<Long> selectedProductIterator = productsId.iterator();
		while (selectedProductIterator.hasNext()) {
			Long selectedProductId = selectedProductIterator.next();
			ProductEntity selectedProduct = productDao.findById(selectedProductId);
			if (tempTransactionWeight < MAX_TRANSACTION_PRODUCTS_WEIGHT && selectedProductIterator.hasNext()) {
				temporaryProducts.add(ProductMapper.toProductTO(selectedProduct));
				tempTransactionWeight += selectedProduct.getWeigth().doubleValue();
			} else {
				if (!selectedProductIterator.hasNext()) {
					temporaryProducts.add(ProductMapper.toProductTO(selectedProduct));
				}
				TransactionTO temporaryTransaction = new TransactionTOBuilder().withClientId(transaction.getClientId())
						.withDate(transaction.getDate()).withStatus(transaction.getStatus())
						.withProductsIds(ProductMapper.map2TOsId(temporaryProducts)).build();
				verifiedTransactions.add(temporaryTransaction);
				temporaryProducts.clear();
				tempTransactionWeight = 0.0;
				temporaryProducts.add(ProductMapper.toProductTO(selectedProduct));
				tempTransactionWeight += selectedProduct.getWeigth().doubleValue();
			}
		}
		return verifiedTransactions;
	}

}
