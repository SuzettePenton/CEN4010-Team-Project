package edu.fiu.team8.sprint4;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class CartController {
  private final CartService cartService;

  public CartController(CartService cartService) {
    this.cartService = cartService;
  }

  @PostMapping
  public ResponseEntity<Void> addBook(
      @RequestParam long userId,
      @RequestParam long bookId) {
    cartService.addBook(userId, bookId);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping
  public List<Book> getBooks(@RequestParam long userId) {
    return cartService.getBooks(userId);
  }

  @GetMapping("/subtotal")
  public CartSubtotalResponse getSubtotal(@RequestParam long userId) {
    return new CartSubtotalResponse(userId, cartService.getSubtotal(userId));
  }

  @DeleteMapping
  public ResponseEntity<Void> deleteBook(
      @RequestParam long userId,
      @RequestParam long bookId) {
    cartService.deleteBook(userId, bookId);
    return ResponseEntity.noContent().build();
  }
}

