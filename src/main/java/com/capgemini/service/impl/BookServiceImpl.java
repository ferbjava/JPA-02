package com.capgemini.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capgemini.dao.BookDao;
import com.capgemini.domain.BookEntity;
import com.capgemini.mappers.BookMapper;
import com.capgemini.service.BookService;
import com.capgemini.types.BookTO;

@Service
@Transactional(readOnly = true)
public class BookServiceImpl implements BookService {
	@Autowired
	private BookDao bookRepository;

	@Override
	public List<BookTO> findAllBooks() {
		List<BookEntity> allBooks = bookRepository.findAll();
		return BookMapper.map2TOs(allBooks);
	}

	@Override
	public List<BookTO> findBooksByTitle(String title) {
		List<BookEntity> booksByTitle = bookRepository.findBookByTitle(title);
		return BookMapper.map2TOs(booksByTitle);
	}

	@Override
	public List<BookTO> findBooksByAuthor(Long authorId) {
		List<BookEntity> booksByAuthor = bookRepository.findBooksByAuthor(authorId);
		return BookMapper.map2TOs(booksByAuthor);
	}

	@Override
	public BookTO findBookById(Long id) {
		BookEntity book = bookRepository.findOne(id);
		return BookMapper.toBookTO(book);
	}

	@Override
	@Transactional(readOnly = false)
	public BookTO saveBook(BookTO book) {
		BookEntity bookEntity = bookRepository.save(BookMapper.toBookEntity(book));
		return BookMapper.toBookTO(bookEntity);
	}

}
