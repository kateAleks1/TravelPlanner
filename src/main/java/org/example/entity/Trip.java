package org.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @Column(name = "start_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    @Column(name = "end_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;
    @ManyToOne
    @JoinColumn(name = "status_id")
    private Trip_Status statusTrip;
private Integer price;
    @ManyToMany
    @JoinTable(name = "trip_destinations",
            joinColumns = @JoinColumn(name = "trip_id"),
            inverseJoinColumns = @JoinColumn(name = "destination_id"))
    @JsonManagedReference
    private List<Destination> destinations = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "trip_participants",
            joinColumns = @JoinColumn(name = "trip_id"), // Исправьте здесь
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonManagedReference
    private Set<User> users = new HashSet<>();
}