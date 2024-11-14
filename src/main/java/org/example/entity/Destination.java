package org.example.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.Countries;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "destinations")
public class Destination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "destination_id")
    private Integer destinationId;
    private String name;
    private String city;
    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Countries country;
    private String description;
    @JsonIgnore
    @ManyToMany(mappedBy = "destinations", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Trip> trips;


}

