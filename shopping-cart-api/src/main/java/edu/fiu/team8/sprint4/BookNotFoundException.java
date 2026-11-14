package edu.fiu.team8.sprint4;

public class BookNotFoundException extends RuntimeException {
  public BookNotFoundException(long bookId) {
    super("Book " + bookId + " was not found");
  }
}

