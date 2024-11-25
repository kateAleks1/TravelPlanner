package org.example.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
    @Entity
    @Table(name = "trips")
    public class Trip {

        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "trip_id")
        @Id
        private int tripId;

    private Integer price;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "trip_destinations",
            joinColumns = @JoinColumn(name = "trip_id"),
            inverseJoinColumns = @JoinColumn(name = "destination_id"))
    private List<Destination> destinations;
        @Column(name = "start_date")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date startDate;
        @Column(name = "end_date")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date endDate;
    @JsonIgnoreProperties()
    @ManyToOne()
    @JoinColumn(name = "status_id")
    private Trip_Status statusTrip;
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("trip")
    private Set<TripPartcipants> participants = new HashSet<>();
        @ManyToOne
        @JoinColumn(name = "city_id")
        @JsonManagedReference
        private Cities city;


    }