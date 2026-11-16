package com.geektext.wishlist.model;

import jakarta.persistence.*;

/**
 * A named wish list that belongs to one user.
 * The unique constraint enforces "unique name per user" at the database level too.
 */
@Entity
@Table(name = "wish_lists",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "name"}))
public class WishList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, length = 100)
    private String name;

    public WishList() {}

    public WishList(Long userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
