    package org.example.entity;



    import com.fasterxml.jackson.annotation.JsonBackReference;
    import com.fasterxml.jackson.annotation.JsonIgnore;
    import com.fasterxml.jackson.annotation.JsonManagedReference;
    import com.fasterxml.jackson.annotation.JsonProperty;
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
    @Table(name = "users")
    @Entity
    public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;
        @JsonProperty("email")
        private String email;
        @JsonProperty("login")
        private String login;
        private String password;
        @Temporal(TemporalType.DATE)
        @Column(name="created_at")
        private Date createdAt;
 @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
 private Set<TripPartcipants> trips = new HashSet<>();
        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,orphanRemoval = true)
        private Set<Review> review;


    }
