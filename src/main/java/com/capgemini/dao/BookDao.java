package com.capgemini.dao;

import java.util.List;

import com.capgemini.domain.BookEntity;

public interface BookDao extends Dao<BookEntity, Long> {

    List<BookEntity> findBookByTitle(String title);

	List<BookEntity> findBooksByAuthor(Long authorId);
}
