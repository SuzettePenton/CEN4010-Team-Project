package edu.fiu.team8.sprint4;

import java.math.BigDecimal;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Temporary adapter for the team Book feature. Replace this component with the
 * team's Book repository/service when the feature is integrated.
 */
@Component
public class BookCatalog {
  private final Map<Long, Book> books = Map.of(
      1L, new Book(1, "Clean Code", "Robert C. Martin", new BigDecimal("34.99")),
      2L, new Book(2, "The Pragmatic Programmer", "David Thomas and Andrew Hunt", new BigDecimal("39.99")),
      3L, new Book(3, "Design Patterns", "Erich Gamma et al.", new BigDecimal("54.99"))
  );

  public Book findById(long bookId) {
    Book book = books.get(bookId);
    if (book == null) {
      throw new BookNotFoundException(bookId);
    }
    return book;
  }
}

