package com.team8.bookstore.controller;

import com.team8.bookstore.model.Comment;
import com.team8.bookstore.model.Rating;
import com.team8.bookstore.repository.CommentRepository;
import com.team8.bookstore.repository.RatingRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookRatingCommentController {

    private final RatingRepository ratingRepository;
    private final CommentRepository commentRepository;

    public BookRatingCommentController(RatingRepository ratingRepository, CommentRepository commentRepository) {
        this.ratingRepository = ratingRepository;
        this.commentRepository = commentRepository;
    }

    @PostMapping("/ratings")
    public ResponseEntity<Void> createRating(@RequestBody RatingRequest request) {
        if (isBlank(request.getUserId()) || isBlank(request.getBookId())) {
            return ResponseEntity.badRequest().build();
        }
        if (request.getRating() < 1 || request.getRating() > 5) {
            return ResponseEntity.badRequest().build();
        }
        request.setDatestamp(LocalDateTime.now());

        Rating rating = new Rating();
        rating.setUserId(request.getUserId());
        rating.setBookId(request.getBookId());
        rating.setRating(request.getRating());
        rating.setDatestamp(request.getDatestamp());
        ratingRepository.save(rating);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/comments")
    public ResponseEntity<Void> createComment(@RequestBody CommentRequest request) {
        if (isBlank(request.getUserId()) || isBlank(request.getBookId()) || isBlank(request.getComment())) {
            return ResponseEntity.badRequest().build();
        }
        request.setDatestamp(LocalDateTime.now());

        Comment comment = new Comment();
        comment.setUserId(request.getUserId());
        comment.setBookId(request.getBookId());
        comment.setCommentText(request.getComment());
        comment.setDatestamp(request.getDatestamp());
        commentRepository.save(comment);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{bookId}/comments")
    public ResponseEntity<List<CommentRequest>> getComments(@PathVariable String bookId) {
        List<CommentRequest> comments = new ArrayList<>();

        for (Comment saved : commentRepository.findByBookIdOrderByDatestampAsc(bookId)) {
            CommentRequest item = new CommentRequest();
            item.setUserId(saved.getUserId());
            item.setBookId(saved.getBookId());
            item.setComment(saved.getCommentText());
            item.setDatestamp(saved.getDatestamp());
            comments.add(item);
        }

        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{bookId}/average-rating")
    public ResponseEntity<Double> getAverageRating(@PathVariable String bookId) {
        Double average = ratingRepository.findAverageRatingByBookId(bookId);
        double averageRating = (average == null) ? 0.0 : average;

        return ResponseEntity.ok(averageRating);
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public static class RatingRequest {
        private String userId;
        private String bookId;
        private int rating;
        private LocalDateTime datestamp;

        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getBookId() { return bookId; }
        public void setBookId(String bookId) { this.bookId = bookId; }
        public int getRating() { return rating; }
        public void setRating(int rating) { this.rating = rating; }
        public LocalDateTime getDatestamp() { return datestamp; }
        public void setDatestamp(LocalDateTime datestamp) { this.datestamp = datestamp; }
    }

    public static class CommentRequest {
        private String userId;
        private String bookId;
        private String comment;
        private LocalDateTime datestamp;

        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getBookId() { return bookId; }
        public void setBookId(String bookId) { this.bookId = bookId; }
        public String getComment() { return comment; }
        public void setComment(String comment) { this.comment = comment; }
        public LocalDateTime getDatestamp() { return datestamp; }
        public void setDatestamp(LocalDateTime datestamp) { this.datestamp = datestamp; }
    }
}
