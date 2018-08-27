package com.capgemini.dao;

import org.springframework.data.repository.CrudRepository;

import com.capgemini.domain.TransactionEntity;

public interface TransactionDao extends CrudRepository<TransactionEntity, Long>, CustomTransactionDao {
	
	TransactionEntity findById(Long id);
	
}
