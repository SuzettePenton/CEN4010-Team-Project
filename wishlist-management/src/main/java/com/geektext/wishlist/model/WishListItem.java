package com.geektext.wishlist.model;

import jakarta.persistence.*;

/**
 * One book saved in one wish list. A book can only appear once per list.
 * bookId is stored as a plain number so this feature does not depend on
 * another teammate's Book class yet (see README for the integration step).
 */
@Entity
@Table(name = "wish_list_items",
       uniqueConstraints = @UniqueConstraint(columnNames = {"wish_list_id", "book_id"}))
public class WishListItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "wish_list_id", nullable = false)
    private Long wishListId;

    @Column(name = "book_id", nullable = false)
    private Long bookId;

    public WishListItem() {}

    public WishListItem(Long wishListId, Long bookId) {
        this.wishListId = wishListId;
        this.bookId = bookId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getWishListId() { return wishListId; }
    public void setWishListId(Long wishListId) { this.wishListId = wishListId; }
    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }
}
