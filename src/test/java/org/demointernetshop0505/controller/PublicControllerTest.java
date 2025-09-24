package org.demointernetshop0505.controller;

import org.demointernetshop0505.entity.ConfirmationCode;
import org.demointernetshop0505.entity.User;
import org.demointernetshop0505.repository.ConfirmationCodeRepository;
import org.demointernetshop0505.repository.UserRepository;
import org.demointernetshop0505.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PublicControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
        // сперва дочерние таблицы, потом родительские
        confirmationCodeRepository.truncateAndResetAutoIncrement();
        userRepository.truncateAndResetAutoIncrement();
    }

    @Test
    void testRegisterUserSuccess() throws Exception {
        String newUserJson = """
                {
                "firstName":"user1",
                "lastName":"user1",
                "email":"user1@company.com",
                "hashPassword":"Pass11111"
                }
                """;

        String requestPath = "/api/public/registration";

        mockMvc.perform(post(requestPath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("user1@company.com"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.id").value("2"));

        // можно еще проверить, количество записей в БД - их должно быть 2
        // почему не можем проверить id?
        // так как мы модем запускать тесты несколько раз, то при каждом тесте в БД
        // будет добавлять новая первоначальная запись и потом после завершения тест
        // БД будет очищаться. НО!!! Порядковый номер записи (id) не будет "сбрасываться" на 1.
        // То есть мы не можем заранее сказать, а какой id будет у нашей записи для нового пользователя.


    }

}