package org.example.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripSortingCountries {
    private String cityName;
    private Long cityCount;
    private String imageUrl;
}
