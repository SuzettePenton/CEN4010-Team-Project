
package com.example.bookproject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @PostMapping
    public Book createBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<Book> getBook(@PathVariable String isbn) {
        return ResponseEntity.of(bookRepository.findById(isbn));
    }

    // NEW SPRINT 3 FEATURE
    @PutMapping("/{isbn}")
    public ResponseEntity<Book> updateBook(
            @PathVariable String isbn,
            @RequestBody Book updatedBook) {

        return bookRepository.findById(isbn)
                .map(book -> {
                    book.setTitle(updatedBook.getTitle());
                    book.setAuthor(updatedBook.getAuthor());
                    return ResponseEntity.ok(bookRepository.save(book));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
