package edu.fiu.team8.sprint4;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {
  private final BookCatalog bookCatalog;
  private final CartItemRepository cartItemRepository;

  public CartService(BookCatalog bookCatalog, CartItemRepository cartItemRepository) {
    this.bookCatalog = bookCatalog;
    this.cartItemRepository = cartItemRepository;
  }

  @Transactional
  public void addBook(long userId, long bookId) {
    validateUserId(userId);
    bookCatalog.findById(bookId);
    if (!cartItemRepository.existsByUserIdAndBookId(userId, bookId)) {
      cartItemRepository.save(new CartItemEntity(userId, bookId));
    }
  }

  @Transactional(readOnly = true)
  public List<Book> getBooks(long userId) {
    validateUserId(userId);
    return cartItemRepository.findByUserIdOrderByIdAsc(userId).stream()
        .map(CartItemEntity::getBookId)
        .map(bookCatalog::findById)
        .toList();
  }

  @Transactional(readOnly = true)
  public BigDecimal getSubtotal(long userId) {
    return getBooks(userId).stream()
        .map(Book::price)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  @Transactional
  public void deleteBook(long userId, long bookId) {
    validateUserId(userId);
    if (cartItemRepository.deleteByUserIdAndBookId(userId, bookId) == 0) {
      throw new CartItemNotFoundException(userId, bookId);
    }
  }

  @Transactional
  void clearAll() {
    cartItemRepository.deleteAll();
  }

  private void validateUserId(long userId) {
    if (userId < 1) {
      throw new IllegalArgumentException("User ID must be greater than zero");
    }
  }
}

