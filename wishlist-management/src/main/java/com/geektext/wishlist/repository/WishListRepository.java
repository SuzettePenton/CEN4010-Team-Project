package com.geektext.wishlist.repository;

import com.geektext.wishlist.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListRepository extends JpaRepository<WishList, Long> {
    long countByUserId(Long userId);
    boolean existsByUserIdAndName(Long userId, String name);
}
