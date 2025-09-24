package org.demointernetshop0505.controller.api;

import org.demointernetshop0505.dto.UserResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/users")
public interface UserApi {

    @GetMapping("/{userdId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable int userId);

    // можно добавить DeleteUser, UpdateUser
    // @PutMapping("/{userId}")

}
