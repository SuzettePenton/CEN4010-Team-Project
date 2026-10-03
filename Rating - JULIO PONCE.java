// Save as Rating.java
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ratings")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "book_id", nullable = false)
    private String bookId;

    @Column(nullable = false)
    private int rating; // 1-5 scale

    @Column(nullable = false)
    private LocalDateTime datestamp;
    
    // Getters and Setters omitted for brevity
}