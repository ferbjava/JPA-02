package com.capgemini.dao.impl;

import java.time.YearMonth;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.capgemini.dao.ClientDao;
import com.capgemini.dao.CustomClientDao;
import com.capgemini.domain.ClientEntity;

public class ClientDaoImpl extends AbstractDao<ClientDao, Long> implements CustomClientDao {

	@Override
	public List<ClientEntity> find3ClientsWithMostExpensiveShoppings(YearMonth start, YearMonth end) {
		Calendar startDate = new GregorianCalendar(start.getYear(), start.getMonthValue()-1, 1);
		Calendar endDate = new GregorianCalendar(end.getYear(), end.getMonthValue()-1, end.lengthOfMonth());
		
		return null;
	}

}
