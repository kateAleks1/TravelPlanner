    package org.example.entity;



    import com.fasterxml.jackson.annotation.JsonBackReference;
    import com.fasterxml.jackson.annotation.JsonIgnore;
    import com.fasterxml.jackson.annotation.JsonProperty;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import java.util.List;



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
        @JsonIgnore
        @ManyToMany(mappedBy = "users")
        @JsonBackReference
        private List<Trip> trips;
    }