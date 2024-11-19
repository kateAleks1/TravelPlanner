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

private Integer price;
    @ManyToMany
    @JoinTable(name = "trip_destinations",
            joinColumns = @JoinColumn(name = "trip_id"),
            inverseJoinColumns = @JoinColumn(name = "destination_id"))
    @JsonManagedReference
    private List<Destination> destinations = new ArrayList<>();
    @Column(name = "start_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    @Column(name = "end_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;
    @ManyToOne
    @JoinColumn(name = "status_id")
    private Trip_Status statusTrip;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "trip_participants",
            joinColumns = @JoinColumn(name = "trip_id"), // Исправьте здесь
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonManagedReference
    private Set<User> users = new HashSet<>();

    @ManyToOne  // Связь с городом "многие к одному"
    @JoinColumn(name = "city_id")
    @JsonManagedReference
    private Cities city;
}