package com.capgemini.mappers;

import java.util.Set;
import java.util.stream.Collectors;

import com.capgemini.domain.AuthorEntity;
import com.capgemini.types.AuthorTO;
import com.capgemini.types.AuthorTO.AuthorTOBuilder;

public class AuthorMapper {

	public static AuthorTO toAuthorTO(AuthorEntity authorEntity) {
		if (authorEntity == null)
			return null;

		return new AuthorTOBuilder().withFirstName(authorEntity.getFirstName()).withLastName(authorEntity.getLastName())
				.withId(authorEntity.getId()).build();

	}

	public static AuthorEntity toAuthorEntity(AuthorTO authorTO) {
		if (authorTO == null)
			return null;
		AuthorEntity authorEntity = new AuthorEntity();
		authorEntity.setFirstName(authorTO.getFirstName());
		authorEntity.setLastName(authorTO.getLastName());
		return authorEntity;
	}

	public static Set<AuthorTO> map2TOs(Set<AuthorEntity> authorEntities) {
		return authorEntities.stream().map(AuthorMapper::toAuthorTO).collect(Collectors.toSet());
	}

	public static Set<AuthorEntity> map2Entities(Set<AuthorTO> productTOs) {
		return productTOs.stream().map(AuthorMapper::toAuthorEntity).collect(Collectors.toSet());
	}

}
