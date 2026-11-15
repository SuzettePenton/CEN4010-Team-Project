package edu.fiu.team8.sprint4;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "cart_items",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_cart_user_book",
        columnNames = {"user_id", "book_id"}
    )
)
public class CartItemEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", nullable = false)
  private long userId;

  @Column(name = "book_id", nullable = false)
  private long bookId;

  protected CartItemEntity() { }

  public CartItemEntity(long userId, long bookId) {
    this.userId = userId;
    this.bookId = bookId;
  }

  public Long getId() { return id; }
  public long getUserId() { return userId; }
  public long getBookId() { return bookId; }
}

