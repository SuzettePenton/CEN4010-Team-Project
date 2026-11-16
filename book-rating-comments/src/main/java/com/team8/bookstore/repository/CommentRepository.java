package com.team8.bookstore.repository;

import com.team8.bookstore.model.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // All comments for one book, oldest first
    List<Comment> findByBookIdOrderByDatestampAsc(String bookId);
}
