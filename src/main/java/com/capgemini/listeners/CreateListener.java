package com.capgemini.listeners;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.PrePersist;

import com.capgemini.domain.AbstractEntity;

public class CreateListener {

	@PrePersist
	public void setCreatedAt(final AbstractEntity entity) {
		Date date = new Date();
		entity.setCreatedAt(new Timestamp(date.getTime()));
	}

}
