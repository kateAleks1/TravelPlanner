package org.example.Dal.Repository;

import org.example.entity.Destination;
import org.example.entity.Review;
import org.example.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review,Integer> {


}
