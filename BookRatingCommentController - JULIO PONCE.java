import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookRatingCommentController {

    @PostMapping("/ratings")
    public ResponseEntity<Void> createRating(@RequestBody RatingRequest request) {
        if (request.getRating() < 1 || request.getRating() > 5) {
            return ResponseEntity.badRequest().build(); 
        }
        request.setDatestamp(LocalDateTime.now());
        
        // TODO: Save to database
        
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/comments")
    public ResponseEntity<Void> createComment(@RequestBody CommentRequest request) {
        request.setDatestamp(LocalDateTime.now());
        
        // TODO: Save to database
        
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{bookId}/comments")
    public ResponseEntity<List<CommentRequest>> getComments(@PathVariable String bookId) {
        List<CommentRequest> comments = new ArrayList<>(); 
        
        // TODO: Fetch from database
        
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{bookId}/average-rating")
    public ResponseEntity<Double> getAverageRating(@PathVariable String bookId) {
        double averageRating = 0.0; 
        
        // TODO: Fetch from database
        
        return ResponseEntity.ok(averageRating);
    }
}

class RatingRequest {
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

class CommentRequest {
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