package org.demointernetshop0505.dto;

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


    private String firstName;

    private String lastName;

    private String email;

    private String hashPassword;


}
