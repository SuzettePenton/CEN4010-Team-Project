// Save as Comment.java
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "book_id", nullable = false)
    private String bookId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String commentText;

    @Column(nullable = false)
    private LocalDateTime datestamp;
    
    // Getters and Setters omitted for brevity
}