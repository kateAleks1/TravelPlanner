    package org.example.entity;



    import com.fasterxml.jackson.annotation.JsonBackReference;
    import com.fasterxml.jackson.annotation.JsonIgnore;
    import com.fasterxml.jackson.annotation.JsonProperty;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import java.util.ArrayList;
    import java.util.HashSet;
    import java.util.List;
    import java.util.Set;

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

 @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
 @JsonIgnore
 private Set<TripPartcipants> trips = new HashSet<>();


    }

