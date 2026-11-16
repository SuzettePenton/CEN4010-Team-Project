package com.geektext.wishlist.controller;

import com.geektext.wishlist.model.WishListItem;
import com.geektext.wishlist.service.WishListService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlists")
public class WishListController {

    private final WishListService service;

    public WishListController(WishListService service) {
        this.service = service;
    }

    /** POST /api/wishlists  body: {"userId":1,"name":"Summer Reading"} -> 201, no body */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody CreateWishListRequest request) {
        service.createWishList(request.userId(), request.name());
    }

    /** POST /api/wishlists/{wishListId}/books/{bookId} -> 201, no body */
    @PostMapping("/{wishListId}/books/{bookId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addBook(@PathVariable Long wishListId, @PathVariable Long bookId) {
        service.addBook(wishListId, bookId);
    }

    /** GET /api/wishlists/{wishListId}/books -> 200, JSON list */
    @GetMapping("/{wishListId}/books")
    public List<WishListItem> getBooks(@PathVariable Long wishListId) {
        return service.getBooks(wishListId);
    }
}
