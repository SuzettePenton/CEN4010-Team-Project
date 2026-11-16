package com.geektext.wishlist;

import com.geektext.wishlist.repository.WishListRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Automated tests for the Wish List API (Sprint 4, story 6).
 * They run against an in-memory H2 database, so MySQL is not needed.
 * Each test uses its own user ID so tests do not affect each other.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class WishListApiTest {

    @Autowired MockMvc mvc;
    @Autowired WishListRepository lists;

    private void createList(long userId, String name, int expectedStatus) throws Exception {
        mvc.perform(post("/api/wishlists").contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":" + userId + ",\"name\":\"" + name + "\"}"))
                .andExpect(status().is(expectedStatus));
    }

    /** Finds the id of the first list that belongs to this user. */
    private long listIdFor(long userId) {
        return lists.findAll().stream()
                .filter(l -> l.getUserId() == userId)
                .findFirst().orElseThrow().getId();
    }

    // ---- Create wish list ----

    @Test
    void createListWithUniqueNameReturns201() throws Exception {
        createList(100, "Summer Reading", 201);
    }

    @Test
    void duplicateNameForSameUserReturns409() throws Exception {
        createList(101, "Favorites", 201);
        createList(101, "Favorites", 409);
        createList(102, "Favorites", 201); // a different user may reuse the name
    }

    @Test
    void exactlyThreeListsAreAllowed() throws Exception {
        createList(108, "A", 201);
        createList(108, "B", 201);
        createList(108, "C", 201);
    }

    @Test
    void fourthListReturns400() throws Exception {
        createList(103, "A", 201);
        createList(103, "B", 201);
        createList(103, "C", 201);
        createList(103, "D", 400);
    }

    @Test
    void blankNameReturns400() throws Exception {
        createList(104, "   ", 400);
    }

    @Test
    void missingUserIdReturns400() throws Exception {
        mvc.perform(post("/api/wishlists").contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"No User\"}"))
                .andExpect(status().isBadRequest());
    }

    // ---- Add book ----

    @Test
    void addBooksThenListThem() throws Exception {
        createList(105, "Tech", 201);
        long listId = listIdFor(105);
        mvc.perform(post("/api/wishlists/" + listId + "/books/501")).andExpect(status().isCreated());
        mvc.perform(post("/api/wishlists/" + listId + "/books/502")).andExpect(status().isCreated());
        mvc.perform(get("/api/wishlists/" + listId + "/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].bookId").value(501));
    }

    @Test
    void duplicateBookReturns409() throws Exception {
        createList(106, "Dupes", 201);
        long listId = listIdFor(106);
        mvc.perform(post("/api/wishlists/" + listId + "/books/700")).andExpect(status().isCreated());
        mvc.perform(post("/api/wishlists/" + listId + "/books/700")).andExpect(status().isConflict());
    }

    @Test
    void addBookToUnknownListReturns404() throws Exception {
        mvc.perform(post("/api/wishlists/999999/books/700")).andExpect(status().isNotFound());
    }

    // ---- List books ----

    @Test
    void emptyListReturnsEmptyArray() throws Exception {
        createList(107, "Empty", 201);
        long listId = listIdFor(107);
        mvc.perform(get("/api/wishlists/" + listId + "/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void listBooksForUnknownListReturns404() throws Exception {
        mvc.perform(get("/api/wishlists/999999/books")).andExpect(status().isNotFound());
    }

    @Test
    void nonNumericListIdReturns400() throws Exception {
        mvc.perform(get("/api/wishlists/abc/books")).andExpect(status().isBadRequest());
    }
}
