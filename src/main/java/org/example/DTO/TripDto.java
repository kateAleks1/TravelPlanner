package org.example.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripDto {
    private Integer tripId;
    private Date startDate;
    private Date endDate;
    private String status;
    private Integer destinationId;
    private Integer userId;
}
