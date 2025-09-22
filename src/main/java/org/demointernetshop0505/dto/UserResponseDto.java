package org.demointernetshop0505.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {


    private Integer id;

    private String firstName;

    private String lastName;

    private String email;

    private String role;

}
