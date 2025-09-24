package org.demointernetshop0505.controller;

import lombok.RequiredArgsConstructor;
import org.demointernetshop0505.controller.api.UserApi;
import org.demointernetshop0505.dto.UserResponseDto;
import org.demointernetshop0505.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService service;


    @Override
    public ResponseEntity<UserResponseDto> getUserById(int userId) {
        return ResponseEntity.ok(service.getUserById(userId));
    }


}
