package com.team8.bookstore.repository;

import com.team8.bookstore.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    // Average of all ratings for one book; null when the book has none yet
    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.bookId = :bookId")
    Double findAverageRatingByBookId(@Param("bookId") String bookId);
}
