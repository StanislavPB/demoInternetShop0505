package org.demointernetshop0505.controller;

import lombok.RequiredArgsConstructor;
import org.demointernetshop0505.controller.api.AdminApi;
import org.demointernetshop0505.dto.UserResponseDto;
import org.demointernetshop0505.entity.ConfirmationCode;
import org.demointernetshop0505.entity.User;
import org.demointernetshop0505.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminController implements AdminApi {

    private final UserService service;


    @Override
    public ResponseEntity<List<User>> findAllFullDetails() {
        return ResponseEntity.ok(service.getAllUsersFullDetails()) ;
    }

    @Override
    public ResponseEntity<List<UserResponseDto>> findAll() {
        return ResponseEntity.ok(service.getAllUsers());
    }

    @Override
    public ResponseEntity<List<ConfirmationCode>> findAllCodes(String email) {
        return ResponseEntity.ok(service.findCodesByUser(email));
    }
}
