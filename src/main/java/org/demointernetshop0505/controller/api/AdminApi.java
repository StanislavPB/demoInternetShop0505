package org.demointernetshop0505.controller.api;

import org.demointernetshop0505.dto.UserResponseDto;
import org.demointernetshop0505.entity.ConfirmationCode;
import org.demointernetshop0505.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/api/admin")
public interface AdminApi {

    @GetMapping("/users/fulldetails")
    public ResponseEntity<List<User>> findAllFullDetails();

    @GetMapping("/users/details")
    public ResponseEntity<List<UserResponseDto>> findAll();

    @GetMapping("/users/allCodes")
    ResponseEntity<List<ConfirmationCode>> findAllCodes(@RequestParam String email);


}
