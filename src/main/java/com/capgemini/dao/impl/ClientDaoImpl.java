package com.capgemini.dao.impl;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.capgemini.dao.ClientDao;
import com.capgemini.dao.CustomClientDao;
import com.capgemini.domain.ClientEntity;
import com.capgemini.domain.QClientEntity;
import com.capgemini.domain.QProductEntity;
import com.capgemini.domain.QTransactionEntity;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class ClientDaoImpl extends AbstractDao<ClientDao, Long> implements CustomClientDao {

	@Override
	public List<ClientEntity> find3ClientsWithMostExpensiveShoppings(YearMonth start, YearMonth end) {
		Calendar startDate = new GregorianCalendar(start.getYear(), start.getMonthValue()-1, 1);
		Calendar endDate = new GregorianCalendar(end.getYear(), end.getMonthValue()-1, end.lengthOfMonth());
		
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		QClientEntity client = QClientEntity.clientEntity;
		QProductEntity product = QProductEntity.productEntity;
		QTransactionEntity transaction = QTransactionEntity.transactionEntity;
		NumberPath<BigDecimal> totalCost = Expressions.numberPath(BigDecimal.class, "c");
		List<ClientEntity> selectedClients = new ArrayList<>();
		
		List<Tuple> clientsByCosts = queryFactory.select(client, product.price.sum().as(totalCost))
						.from(client)
						.join(client.transactions,transaction)
						.join(transaction.products,product)
						.where(transaction.date.between(startDate, endDate))
						.groupBy(client.id)
						.orderBy(totalCost.desc())
						.limit(3)
						.fetch();

		for(Tuple t:clientsByCosts){
			selectedClients.add(t.get(client));
		}
		
		return selectedClients;
	}

}
