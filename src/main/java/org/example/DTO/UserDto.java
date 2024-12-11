package org.example.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {
    private  int id;
    @NotBlank(message = "email must not be blank")
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank(message = "Login must not be blank")
    @Size(min = 3, max = 15, message = "Login must be between 3 and 15 characters")
    private String login;
    @NotBlank(message = "Login must not be blank")
    @Size(min = 3, max = 15, message = "Password must be between 3 and 15 characters")
    private String password;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
}
