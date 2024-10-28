package org.example.DTO;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;


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
}
