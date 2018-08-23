package com.capgemini.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.capgemini.domain.ClientEntity;

@Repository
public interface ClientDao extends CrudRepository<ClientEntity, Long> {

	ClientEntity findById(Long id);
}
