package org.example.DTO;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DestinationDto {
    @JsonProperty("destination_id")
    private Integer destinationId;
    private String name;
    private String country;
    private String city;
    private String description;
    private int country_id;
}
