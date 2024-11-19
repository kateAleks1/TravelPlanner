package org.example.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

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
            private int users;
            private int cityId;
        }
