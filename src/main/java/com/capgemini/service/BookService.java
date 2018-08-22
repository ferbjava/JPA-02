package com.capgemini.service;

import java.util.List;

import com.capgemini.types.BookTO;

public interface BookService {
	List<BookTO> findAllBooks();

	List<BookTO> findBooksByTitle(String title);

	List<BookTO> findBooksByAuthor(Long authorId);

	BookTO findBookById(Long id);

	BookTO saveBook(BookTO book);
}
