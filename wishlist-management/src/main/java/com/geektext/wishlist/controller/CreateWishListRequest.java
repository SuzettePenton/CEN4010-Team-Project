package com.geektext.wishlist.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** JSON body for POST /api/wishlists */
public record CreateWishListRequest(
        @NotNull(message = "userId is required") Long userId,
        @NotBlank(message = "name is required")
        @Size(max = 100, message = "name must be 100 characters or fewer") String name) {
}
