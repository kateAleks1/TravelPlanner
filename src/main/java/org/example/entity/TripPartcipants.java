package org.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="trip_participants")
public class TripPartcipants {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip tripId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "StartDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    @Column(name = "EndDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;
    @ManyToOne
    @JoinColumn(name = "status_trip")
    private Trip_Status statusTrip;

}
