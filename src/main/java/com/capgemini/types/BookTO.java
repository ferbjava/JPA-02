package com.capgemini.types;

import java.util.HashSet;
import java.util.Set;

import org.springframework.util.CollectionUtils;

public class BookTO {

	private String title;
	private Set<AuthorTO> authors;
	private Long id;

	public BookTO(String title, Set<AuthorTO> authors) {
		super();
		this.title = title;
		this.authors = authors;
	}

	public BookTO(String title, Set<AuthorTO> authors, Long id) {
		super();
		this.title = title;
		this.authors = authors;
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public Set<AuthorTO> getAuthors() {
		return authors;
	}

	public Long getId() {
		return id;
	}

	public static BookTOBuilder builder() {
		return new BookTOBuilder();
	}

	public static class BookTOBuilder {

		private String title;
		private Set<AuthorTO> authors = new HashSet<>();
		private Long id;

		public BookTOBuilder() {
			super();
		}

		public BookTOBuilder withTitle(String title) {
			this.title = title;
			return this;
		}

		public BookTOBuilder withAuthor(AuthorTO author) {
			this.authors.add(author);
			return this;
		}

		public BookTOBuilder withAuthors(Set<AuthorTO> authorsToBeAdded) {
			this.authors.addAll(authorsToBeAdded);
			return this;
		}

		public BookTOBuilder withId(Long id) {
			this.id = id;
			return this;
		}

		public BookTO build() {
			checkBeforeBuild(title, authors);
			return new BookTO(title, authors, id);
		}

		private void checkBeforeBuild(String title, Set<AuthorTO> authors) {
			if (CollectionUtils.isEmpty(authors) || title == null || title.isEmpty()) {
				throw new RuntimeException("Incorrect book to be created");
			}

		}
	}

	@Override
	public String toString() {
		return "BookTO [title=" + title + ", authors=" + authors + ", id=" + id + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((authors == null) ? 0 : authors.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BookTO other = (BookTO) obj;
		if (authors == null) {
			if (other.authors != null)
				return false;
		} else if (!authors.equals(other.authors))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

}
