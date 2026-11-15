package edu.fiu.team8.sprint4;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
  List<CartItemEntity> findByUserIdOrderByIdAsc(long userId);
  boolean existsByUserIdAndBookId(long userId, long bookId);
  long deleteByUserIdAndBookId(long userId, long bookId);
}

