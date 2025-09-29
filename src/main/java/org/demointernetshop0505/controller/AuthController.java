package org.demointernetshop0505.controller;

import lombok.RequiredArgsConstructor;
import org.demointernetshop0505.controller.api.AuthApi;
import org.demointernetshop0505.security.dto.AuthRequest;
import org.demointernetshop0505.security.dto.AuthResponse;
import org.demointernetshop0505.security.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;

    @Override
    public ResponseEntity<AuthResponse> authenticate(AuthRequest request) {

        String jwt = authService.generateJwt(request);

        return new ResponseEntity<>(new AuthResponse(jwt), HttpStatus.OK);
    }
}
