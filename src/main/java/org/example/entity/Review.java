package org.example.entity;

import jakarta.persistence.Entity;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "reviews"
)
public class Review {
    @Id
       @Column(name="review_id")
    private int reviewId;
    @Column(name="review_text")
    private String reviewDesc;
    @Temporal(TemporalType.DATE)
    @Column(name="creating_date")
    private Date creatingDate;
    @Column(name="rating")
    private int reviewRating;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "destination_id", referencedColumnName = "destination_id")
    private Destination destination;

}
