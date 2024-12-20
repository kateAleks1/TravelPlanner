package org.example.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.Destination;
import org.example.entity.User;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
    @AllArgsConstructor
    public class ReviewDto {
    
    
        private int reviewId;
        private String reviewDesc;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date creatingDate;
        private Integer reviewRating;
        private int userId;
        private int destinationId;
    }
