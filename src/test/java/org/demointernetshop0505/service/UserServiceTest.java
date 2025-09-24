package org.demointernetshop0505.service;

import org.demointernetshop0505.dto.UserRequestDto;
import org.demointernetshop0505.entity.ConfirmationCode;
import org.demointernetshop0505.entity.User;
import org.demointernetshop0505.repository.ConfirmationCodeRepository;
import org.demointernetshop0505.repository.UserRepository;
import org.demointernetshop0505.service.exception.AlreadyExistException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.rmi.AlreadyBoundException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.yml")
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfirmationCodeRepository confirmationCodeRepository;

    @Autowired
    private UserService userService;


    @BeforeEach
    void setUp(){
        LocalDateTime now = LocalDateTime.now();

        User testUser = User.builder()
                .firstName("userTest")
                .lastName("userTest")
                .email("userTest@company.com")
                .hashPassword("Pass12345")
                .role(User.Role.USER)
                .status(User.Status.NOT_CONFIRM)
                .createDate(now)
                .updateDate(now)
                .build();

        User savedUser = userRepository.save(testUser);

        ConfirmationCode confirmationCode = ConfirmationCode.builder()
                .code("someConfirmationCode")
                .user(savedUser)
                .expireDataTime(now.plusDays(1))
                .build();

        confirmationCodeRepository.save(confirmationCode);

    }

    @AfterEach
    void dropDatabase(){
        confirmationCodeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testWhenDuplicatedEmail(){
        UserRequestDto request = UserRequestDto.builder()
                .firstName("user")
                .lastName("user")
                .email("userTest@company.com")
                .hashPassword("Password111")
                .build();

        assertThrows(AlreadyExistException.class, () -> userService.registration(request));
    }


}