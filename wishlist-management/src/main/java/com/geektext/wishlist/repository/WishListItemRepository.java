package com.geektext.wishlist.repository;

import com.geektext.wishlist.model.WishListItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishListItemRepository extends JpaRepository<WishListItem, Long> {
    List<WishListItem> findByWishListId(Long wishListId);
    boolean existsByWishListIdAndBookId(Long wishListId, Long bookId);
}
