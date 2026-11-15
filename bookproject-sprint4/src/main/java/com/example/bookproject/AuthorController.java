package com.example.bookproject;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public AuthorController(AuthorRepository authorRepository,
                            BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    // Feature 4 requirement: create an author. No response body.
    @PostMapping
    public ResponseEntity<Void> createAuthor(@RequestBody Author author) {
        if (author.getFirstName() == null || author.getFirstName().isBlank()
                || author.getLastName() == null || author.getLastName().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        author.setId(null); // Let MySQL generate the ID.
        Author saved = authorRepository.save(author);
        return ResponseEntity.created(URI.create("/authors/" + saved.getId())).build();
    }

    // Feature 4 requirement: list all books associated with an author ID.
    @GetMapping("/{authorId}/books")
    public ResponseEntity<List<Book>> booksByAuthor(@PathVariable Long authorId) {
        if (!authorRepository.existsById(authorId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bookRepository.findByAuthorId(authorId));
    }
}
