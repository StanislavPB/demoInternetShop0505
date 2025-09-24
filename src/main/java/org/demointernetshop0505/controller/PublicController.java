package org.demointernetshop0505.controller;

import lombok.RequiredArgsConstructor;
import org.demointernetshop0505.controller.api.PublicApi;
import org.demointernetshop0505.dto.UserRequestDto;
import org.demointernetshop0505.dto.UserResponseDto;
import org.demointernetshop0505.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PublicController implements PublicApi {

    private final UserService service;


    @Override
    public ResponseEntity<UserResponseDto> userRegistration(UserRequestDto request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.registration(request));
    }

    @Override
    public ResponseEntity<String> confirmRegistration(String code) {
        return ResponseEntity.ok(service.confirmationEmail(code));
    }
}
