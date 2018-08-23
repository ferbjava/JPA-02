package com.capgemini.mappers;

import java.util.List;
import java.util.stream.Collectors;

import com.capgemini.domain.TransactionEntity;
import com.capgemini.types.TransactionTO;
import com.capgemini.types.TransactionTO.TransactionTOBuilder;

public class TransactionMapper {

	public static TransactionTO toTransactionTO(TransactionEntity entity) {
		if (entity == null){
			return null;
		}
		return new TransactionTOBuilder().withId(entity.getId()).withClientId(entity.getClient().getId())
				.withDate(entity.getDate()).withStatus(entity.getStatus())
				.withProductsIds(ProductMapper.map2Ids(entity.getProducts())).build();
	}

	public static TransactionEntity toTransactionEntity(TransactionTO transactionTO) {
		if (transactionTO == null){
			return null;
		}
		return new TransactionEntity(transactionTO.getId(), transactionTO.getDate(), transactionTO.getStatus());
	}
	
	public static List<Long> map2TOsId(List<TransactionEntity> entities){
		if(entities == null){
			return null;
		}
		return entities.stream().map(TransactionEntity::getId).collect(Collectors.toList());
	}
}
