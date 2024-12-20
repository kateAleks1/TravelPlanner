package org.example.Dal.Repository;

import org.example.entity.Destination;
import org.example.entity.Review;
import org.example.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
   @Query("SELECT r FROM Review r WHERE r.destination.destinationId = :destinationId AND r.user.id = :userId")
   Optional<Review> findReviewByDestinationIdAndUserId(@Param("destinationId") int destinationId, @Param("userId") int userId);
}
