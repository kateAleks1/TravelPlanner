package org.example.DTO;


import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class HeaderDto {
    private String login;
    private Date createdAt;
    private Date expiredAt;

}
