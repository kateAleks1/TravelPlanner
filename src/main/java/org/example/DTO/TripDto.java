package org.example.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
        @NoArgsConstructor
        @AllArgsConstructor
        public class TripDto {
            @JsonProperty("trip_id")
            private int trip_id;
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            private Date start_date;
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            private Date   end_date;
            private int price;
            private int  status_id;
            private int idDestination;

    private List<Integer> usersListId = new ArrayList<>();
    private int cityId;
    private boolean isGroup;
    private int userOrganizerId;
    private boolean isOrganizer;
    private List<Integer> userIds;
    private Set<User> usersSet;
    private  int OrganizerId;// Идентификаторы пользователей

}
