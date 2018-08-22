package com.capgemini.mappers;

import com.capgemini.domain.ClientEntity;
import com.capgemini.types.ClientTO;
import com.capgemini.types.ClientTO.ClientTOBuilder;

public class ClientMapper {

	public static ClientTO toClientTO(ClientEntity entity) {
		if (entity == null)
			return null;
		return new ClientTOBuilder().withId(entity.getId()).withFirstName(entity.getFirstName())
				.withLastName(entity.getLastName()).withEmail(entity.getEmail())
				.withPhoneNumber(entity.getPhoneNumber()).withDateBirth(entity.getDateBirth()).build();
	}

	public static ClientEntity toClientEntity(ClientTO clientTO) {
		if (clientTO == null)
			return null;
		return new ClientEntity(clientTO.getId(), clientTO.getFirstName(), clientTO.getLastName(), clientTO.getEmail(),
				clientTO.getPhoneNumber(), clientTO.getAdress(), clientTO.getDateBirth());
	}

}
