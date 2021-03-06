package com.capgemini.service.impl;

import java.math.BigDecimal;
import java.time.YearMonth;
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
import com.capgemini.types.TransactionSearchCriteriaTO;
import com.capgemini.types.TransactionTO;
import com.capgemini.types.TransactionTO.TransactionTOBuilder;

@Service
@Transactional(readOnly = true)
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
	@Transactional(readOnly = false)
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
	public TransactionTO findTransactionById(Long id) {
		TransactionEntity entity = transactionDao.findById(id);
		return TransactionMapper.toTransactionTO(entity);
	}

	@Override
	@Transactional(readOnly = false)
	public TransactionTO updateTransaction(Long clientId, TransactionTO transactionTO) {
		TransactionEntity entity = TransactionMapper.toTransactionEntity(transactionTO);
		ClientEntity clientEntity = clientDao.findById(clientId);
		entity.setClient(clientEntity);
		for(Long i : transactionTO.getProductsId()){
			entity.addProduct(productDao.findById(i));
		}
		TransactionEntity savedEntity = transactionDao.save(entity);
		return TransactionMapper.toTransactionTO(savedEntity);
	}

	@Override
	@Transactional(readOnly = false)
	public ClientTO addTransactionToClient(Long clientId, TransactionTO transaction) throws TransactionHistoryException, HighPrizeException {
		verifyTransaction(clientId, transaction);
		List<TransactionTO> transactions = verifyWeigth(transaction);
		ClientEntity clientEntity = clientDao.findById(clientId);
		for (TransactionTO veryfiedTransactionTO : transactions) {
			TransactionEntity veryfiedTransaction = TransactionMapper.toTransactionEntity(veryfiedTransactionTO);
			veryfiedTransaction.setClient(clientEntity);
			List<ProductEntity> products = veryfiedTransactionTO.getProductsId().stream().map(productDao::findById)
					.collect(Collectors.toList());
			veryfiedTransaction.addProducts(products);
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
	@Transactional(readOnly = false)
	public void removeClient(Long id) {
		clientDao.delete(id);
	}

	@Override
	public long findTransactionsNo() {
		return transactionDao.count();
	}
	
	/**
	 * Find value of the all transactions for the selected client
	 * 
	 * @param id id number of the selected client
	 * @return value of the all transactions for selected client
	 */
	@Override
	public BigDecimal findTotalCostByClient(Long id) {
		return transactionDao.findTotalCostByClient(id);
	}
	
	/**
	 * Find profit from the all transactions in selected period of time
	 * 
	 * @param startPeriod
	 *            beginning of the selected period of time (year and month)
	 * @param endPeriod
	 *            end of the selected period of time (year and month)
	 * @return value of the profit based on unit price and margin for all products
	 */
	@Override
	public BigDecimal findProfitByPeriod(YearMonth startPeriod, YearMonth endPeriod) {
		return transactionDao.findProfitByPeriod(startPeriod, endPeriod);
	}
	
	/**
	 * Find 3 clients with the highest value of all theirs transactions in
	 * selected period of time
	 * 
	 * @param startDate
	 *            beginning of the selected period of time (year and month)
	 * @param endDate
	 *            end of the selected period of time (year and month)
	 * @return list of the 3 client in order of the vale of theirs transactions
	 */
	@Override
	public List<ClientEntity> find3ClientsWithMostExpensiveShoppings(YearMonth startDate, YearMonth endDate) {
		return clientDao.find3ClientsWithMostExpensiveShoppings(startDate, endDate);
	}
	
	/**
	 * Find total cost of all transaction with gives status for selected client
	 * @param id of the selected client
	 * @param status status name which is the filter during searching
	 * @return value of transactions which meet the condition
	 */
	@Override
	public BigDecimal findCostByClientAndStatus(Long id, String status) {
		return transactionDao.findCostByClientAndStatus(id, status);
	}
	
	/**
	 * Find total cost of all transaction with gives status
	 * @param status status name which is the filter during searching
	 * @return value of transactions which meet the condition
	 */
	@Override
	public BigDecimal findTotalCostByStatus(String status) {
		return transactionDao.findTotalCostByStatus(status);
	}
	
	/**
	 * Find list transactions with following criteria:
	 * <p> - transaction belong to selected client
	 * <p> - transaction contains selected product
	 * <p> - transaction was done in selected period of time
	 * <p> - total cost of transaction is in selected range
	 * <p> - not all criteria should be defined
	 * @param criteria 
	 * @return list of transactions which meet the criteria or emty list if all criteria are null
	 * 
	 */
	@Override
	public List<TransactionEntity> findTransactionsByCriteria(TransactionSearchCriteriaTO criteria) {
		return transactionDao.findByCriteria(criteria);
	}

	// private methods
	/**
	 * Verify if client wants to buy products with total price over 5000 zl
	 * having below 3 transactions, and transaction contains 5 products with
	 * unit price over 7000 zl
	 * 
	 * @param clientId
	 * @param transaction
	 *            transactions which should be verified
	 * @throws TransactionHistoryException
	 * @throws HighPrizeException
	 */
	private void verifyTransaction(Long clientId, TransactionTO transaction)
			throws TransactionHistoryException, HighPrizeException {
		if (verifyIfBelow3Transactions(clientId, transaction)) {
			throw new TransactionHistoryException();
		}
		if (verifyIfOver5ExpensiveItems(transaction)) {
			throw new HighPrizeException();
		}
	}
	
	/**
	 * Verify if client wants to buy products with total price over 5000 zl
	 * having below 3 transactions, and transaction contains 5 products with unit price over 7000 zl
	 * 
	 * @param transaction
	 *            which should be verified
	 * @return false if transaction is correct
	 */
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
	
	/**
	 * Verify if transaction contains 5 products with unit price over 7000 zl
	 * 
	 * @param transaction
	 *            which should be verified
	 * @return false if transaction is correct
	 */
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

	/**
	 * Separate oversized transaction (total weight of all products > 25 kg)
	 * into suitable number of smaller packages
	 * 
	 * @param transaction
	 *            which should be verified by weight of products
	 * @return list of transactions with right weight
	 */
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
