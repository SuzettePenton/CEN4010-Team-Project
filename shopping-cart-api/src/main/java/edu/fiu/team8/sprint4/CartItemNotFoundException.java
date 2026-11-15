package edu.fiu.team8.sprint4;

public class CartItemNotFoundException extends RuntimeException {
  public CartItemNotFoundException(long userId, long bookId) {
    super("Book " + bookId + " is not in the cart for user " + userId);
  }
}

