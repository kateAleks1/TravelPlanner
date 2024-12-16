package org.example.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.example.entity.Destination;
import org.example.entity.User;

import java.util.Date;

public class ReviewDto {


    private int reviewId;
    @NotBlank(message = "Review description cannot be blank")
    @Size(max = 255, message = "Review description must not exceed 255 characters")
    private String reviewDesc;
    @PastOrPresent(message = "Creating date cannot be in the future")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creatingDate;
    @Min(value = 1, message = "Review rating must be at least 1")
    @Max(value = 5, message = "Review rating must not exceed 5")
    private int reviewRating;

    private int userId;

    private int destinationId;
}
