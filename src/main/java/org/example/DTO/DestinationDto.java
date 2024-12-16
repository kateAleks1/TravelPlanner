package org.example.DTO;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.Review;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DestinationDto {
    @JsonProperty("destination_id")
    private Integer destinationId;
    private String name;
    private int city;
    private String description;
    private int country_id;
    private String image_url;
private List<Review> reviewList;
}
