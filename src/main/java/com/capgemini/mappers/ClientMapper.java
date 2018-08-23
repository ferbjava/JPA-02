package com.capgemini.mappers;

import java.util.List;
import java.util.stream.Collectors;

import com.capgemini.domain.ClientEntity;
import com.capgemini.domain.TransactionEntity;
import com.capgemini.types.ClientTO;
import com.capgemini.types.ClientTO.ClientTOBuilder;

public class ClientMapper {

	public static ClientTO toClientTO(ClientEntity entity) {
		if (entity == null)
			return null;
		return new ClientTOBuilder().withId(entity.getId()).withVersion(entity.getVersion()).withFirstName(entity.getFirstName())
				.withLastName(entity.getLastName()).withEmail(entity.getEmail())
				.withPhoneNumber(entity.getPhoneNumber()).withAdress(entity.getAdress()).withDateBirth(entity.getDateBirth())
				.withTransactionsId(TransactionMapper.map2TOsId(entity.getTransactions())).build();
	}

	public static ClientEntity toClientEntity(ClientTO clientTO) {
		if (clientTO == null)
			return null;
		return new ClientEntity(clientTO.getId(), clientTO.getFirstName(), clientTO.getLastName(), clientTO.getEmail(),
				clientTO.getPhoneNumber(), clientTO.getAdress(), clientTO.getDateBirth());
	}

	public static List<Long> toClientTransactionIDs(ClientEntity entity) {
		if (entity == null)
			return null;
		return entity.getTransactions().stream().map(TransactionEntity::getId).collect(Collectors.toList());
	}
	
}
