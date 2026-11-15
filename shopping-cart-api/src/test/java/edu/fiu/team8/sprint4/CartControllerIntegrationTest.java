package edu.fiu.team8.sprint4;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class CartControllerIntegrationTest {
  @Autowired private MockMvc mockMvc;
  @Autowired private CartService cartService;
  @Autowired private CartItemRepository cartItemRepository;

  @BeforeEach
  void resetCarts() {
    cartService.clearAll();
  }

  @Test
  void persistsAndCompletesTheRequiredCartWorkflow() throws Exception {
    mockMvc.perform(post("/api/cart").param("userId", "7").param("bookId", "1"))
        .andExpect(status().isCreated()).andExpect(content().string(""));
    mockMvc.perform(post("/api/cart").param("userId", "7").param("bookId", "2"))
        .andExpect(status().isCreated());

    if (cartItemRepository.count() != 2) {
      throw new AssertionError("Expected two persisted cart rows");
    }

    mockMvc.perform(get("/api/cart").param("userId", "7"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].title").value("Clean Code"))
        .andExpect(jsonPath("$[1].title").value("The Pragmatic Programmer"));

    mockMvc.perform(get("/api/cart/subtotal").param("userId", "7"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.subtotal").value(74.98));

    mockMvc.perform(delete("/api/cart").param("userId", "7").param("bookId", "1"))
        .andExpect(status().isNoContent());

    mockMvc.perform(get("/api/cart").param("userId", "7"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].id").value(2));
  }

  @Test
  void preventsDuplicateRowsAndSeparatesUsers() throws Exception {
    mockMvc.perform(post("/api/cart").param("userId", "7").param("bookId", "1"))
        .andExpect(status().isCreated());
    mockMvc.perform(post("/api/cart").param("userId", "7").param("bookId", "1"))
        .andExpect(status().isCreated());

    if (cartItemRepository.count() != 1) {
      throw new AssertionError("The same book should not create a duplicate row");
    }

    mockMvc.perform(get("/api/cart").param("userId", "8"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(0));
  }

  @Test
  void returnsUsefulErrorsForInvalidRequests() throws Exception {
    mockMvc.perform(post("/api/cart").param("userId", "7").param("bookId", "999"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error").value("Book 999 was not found"));

    mockMvc.perform(delete("/api/cart").param("userId", "7").param("bookId", "1"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error").value("Book 1 is not in the cart for user 7"));
  }
}

