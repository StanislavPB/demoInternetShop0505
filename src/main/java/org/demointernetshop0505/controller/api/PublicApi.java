package org.demointernetshop0505.controller.api;

import jakarta.validation.Valid;
import org.demointernetshop0505.dto.UserRequestDto;
import org.demointernetshop0505.dto.UserResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/public")
public interface PublicApi {

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> userRegistration(@Valid @RequestBody UserRequestDto request);

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmRegistration(@RequestParam String code);
}
