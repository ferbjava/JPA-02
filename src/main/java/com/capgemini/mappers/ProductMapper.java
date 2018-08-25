package com.capgemini.mappers;

import java.util.List;
import java.util.stream.Collectors;

import com.capgemini.domain.ProductEntity;
import com.capgemini.types.ProductTO;
import com.capgemini.types.ProductTO.ProductTOBuilder;

public class ProductMapper {

	public static ProductTO toProductTO(ProductEntity entity) {
		if (entity == null)
			return null;
		return new ProductTOBuilder().withId(entity.getId()).withName(entity.getName())
				.withPrice(entity.getPrice()).withMargin(entity.getMargin()).withWeigth(entity.getWeigth())
				.withTransactiosId(TransactionMapper.map2TOsId(entity.getTransactions())).build();
	}

	public static ProductEntity toProductEntity(ProductTO productTO) {
		if (productTO == null)
			return null;
		return new ProductEntity(productTO.getId(), productTO.getName(), productTO.getPrice(),
				productTO.getMargin(), productTO.getWeigth());
	}

	public static List<ProductTO> map2TOs(List<ProductEntity> entities) {
		return entities.stream().map(ProductMapper::toProductTO).collect(Collectors.toList());
	}

	public static List<Long> map2Ids(List<ProductEntity> entities) {
		return entities.stream().map(ProductEntity::getId).collect(Collectors.toList());
	}

	public static List<Long> map2TOsId(List<ProductTO> productTOs) {
		return productTOs.stream().map(ProductTO::getId).collect(Collectors.toList());
	}

}
