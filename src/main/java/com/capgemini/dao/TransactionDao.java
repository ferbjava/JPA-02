package com.capgemini.dao;

import java.math.BigDecimal;
import java.util.Calendar;

import org.springframework.data.repository.CrudRepository;

import com.capgemini.domain.TransactionEntity;

public interface TransactionDao extends CrudRepository<TransactionEntity, Long>, CustomTransactionDao {
	
	TransactionEntity findById(Long id);
	
}
