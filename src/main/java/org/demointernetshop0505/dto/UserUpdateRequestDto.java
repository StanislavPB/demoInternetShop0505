package org.demointernetshop0505.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateRequestDto {

    private String email;

    @NotBlank(message = "First name is required and must be not blank)")
    @Size(min = 3, max = 15)
    private String firstName;

    @NotBlank(message = "Last name is required and must be not blank)")
    @Size(min = 3, max = 25)
    private String lastName;

    private String hashPassword;


}
