package com.geektext.wishlist.service;

import com.geektext.wishlist.model.WishList;
import com.geektext.wishlist.model.WishListItem;
import com.geektext.wishlist.repository.WishListItemRepository;
import com.geektext.wishlist.repository.WishListRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WishListService {

    public static final int MAX_LISTS_PER_USER = 3;

    private final WishListRepository wishLists;
    private final WishListItemRepository items;

    public WishListService(WishListRepository wishLists, WishListItemRepository items) {
        this.wishLists = wishLists;
        this.items = items;
    }

    /** Feature action 1: create a wish list with a unique name for a user. */
    @Transactional
    public void createWishList(Long userId, String name) {
        String cleanName = name.trim();
        if (wishLists.countByUserId(userId) >= MAX_LISTS_PER_USER) {
            throw new WishListException(HttpStatus.BAD_REQUEST,
                    "A user can have at most " + MAX_LISTS_PER_USER + " wish lists.");
        }
        if (wishLists.existsByUserIdAndName(userId, cleanName)) {
            throw new WishListException(HttpStatus.CONFLICT,
                    "This user already has a wish list named '" + cleanName + "'.");
        }
        wishLists.save(new WishList(userId, cleanName));
    }

    /** Feature action 2: add a book to a wish list. */
    @Transactional
    public void addBook(Long wishListId, Long bookId) {
        requireWishList(wishListId);
        if (items.existsByWishListIdAndBookId(wishListId, bookId)) {
            throw new WishListException(HttpStatus.CONFLICT,
                    "Book " + bookId + " is already in wish list " + wishListId + ".");
        }
        items.save(new WishListItem(wishListId, bookId));
    }

    /** Feature action 4: list the books in a wish list (empty list is valid). */
    @Transactional(readOnly = true)
    public List<WishListItem> getBooks(Long wishListId) {
        requireWishList(wishListId);
        return items.findByWishListId(wishListId);
    }

    private void requireWishList(Long wishListId) {
        if (!wishLists.existsById(wishListId)) {
            throw new WishListException(HttpStatus.NOT_FOUND,
                    "Wish list " + wishListId + " was not found.");
        }
    }
}
