package org.demointernetshop0505.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.demointernetshop0505.entity.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDto {

    @NotBlank(message = "First name is required and must be not blank)")
    @Size(min = 3, max = 15)
    private String firstName;

    @NotBlank(message = "Last name is required and must be not blank)")
    @Size(min = 3, max = 25)
    private String lastName;

    @Email
    private String email;

    private String hashPassword;


}
