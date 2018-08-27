package com.capgemini.dao;

import java.time.YearMonth;
import java.util.List;

import com.capgemini.domain.ClientEntity;

public interface CustomClientDao {

	List<ClientEntity> find3ClientsWithMostExpensiveShoppings(YearMonth startDate, YearMonth endDate);

}
