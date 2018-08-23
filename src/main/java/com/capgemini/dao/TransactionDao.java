package com.capgemini.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.capgemini.domain.TransactionEntity;

@Repository
public interface TransactionDao extends CrudRepository<TransactionEntity, Long> {
	
	TransactionEntity findById(Long id);
	
}
