package org.demointernetshop0505.security.service;

import lombok.RequiredArgsConstructor;
import org.demointernetshop0505.security.dto.AuthRequest;
import org.demointernetshop0505.service.exception.NotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailService customUserDetailService;
    private final JwtTokenProvider jwtTokenProvider;

    public String generateJwt(AuthRequest request){

        // Проверим сами наличие пользователя в БД

        try {
            customUserDetailService.loadUserByUsername(request.getUsername());
        } catch (UsernameNotFoundException e){
            throw new NotFoundException("Пользователь с email: " + request.getUsername() + " в системе не зарегистрирован");
        }


        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.createToken(request.getUsername());
        return jwt;
    }

}
