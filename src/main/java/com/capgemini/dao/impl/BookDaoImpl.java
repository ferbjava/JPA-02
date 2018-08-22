package com.capgemini.dao.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.capgemini.dao.BookDao;
import com.capgemini.domain.BookEntity;

@Repository
public class BookDaoImpl extends AbstractDao<BookEntity, Long> implements BookDao {

    @Override
    public List<BookEntity> findBookByTitle(String title) {
        TypedQuery<BookEntity> query = entityManager.createQuery(
                "select book from BookEntity book where upper(book.title) like concat(upper(:title), '%')", BookEntity.class);
        query.setParameter("title", title);
        return query.getResultList();
    }

	@Override
	public List<BookEntity> findBooksByAuthor(Long authorId) {
		TypedQuery<BookEntity> query = entityManager.createNamedQuery("books.findBooksByAuthor", BookEntity.class);
		query.setParameter("authorId", authorId);
		return query.getResultList();
	}
}
