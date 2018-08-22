package com.capgemini.listeners;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.capgemini.domain.AbstractEntity;

public class UpdateListener {

	@PrePersist
	@PreUpdate
	public void setUpdatedAt(final AbstractEntity entity) {
		Date date = new Date();
		entity.setUpdatedAt(new Timestamp(date.getTime()));
	}

}
