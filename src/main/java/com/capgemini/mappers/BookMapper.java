package com.capgemini.mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.capgemini.domain.AuthorEntity;
import com.capgemini.domain.BookEntity;
import com.capgemini.types.AuthorTO;
import com.capgemini.types.BookTO;
import com.capgemini.types.BookTO.BookTOBuilder;;

public class BookMapper {

	public static BookTO toBookTO(BookEntity bookEntity) {
		if (bookEntity == null)
			return null;

		Set<AuthorTO> authorTOs = AuthorMapper.map2TOs(bookEntity.getAuthors());
		return new BookTOBuilder().withAuthors(authorTOs).withTitle(bookEntity.getTitle()).withId(bookEntity.getId())
				.build();

	}

	public static BookEntity toBookEntity(BookTO bookTO) {
		if (bookTO == null)
			return null;
		BookEntity bookEntity = new BookEntity();
		Set<AuthorEntity> authors = AuthorMapper.map2Entities(bookTO.getAuthors());
		bookEntity.setAuthors(authors);
		bookEntity.setTitle(bookTO.getTitle());
		return bookEntity;
	}

	public static List<BookTO> map2TOs(List<BookEntity> bookEntities) {
		return bookEntities.stream().map(BookMapper::toBookTO).collect(Collectors.toList());
	}

	public static List<BookEntity> map2Entities(List<BookTO> bookTOs) {
		return bookTOs.stream().map(BookMapper::toBookEntity).collect(Collectors.toList());
	}

}
