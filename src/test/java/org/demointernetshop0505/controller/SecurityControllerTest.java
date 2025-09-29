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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SecurityControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfirmationCodeRepository confirmationCodeRepository;

    @Autowired
    private UserService userService;

    private Integer userId;

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

        this.userId = savedUser.getId();

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
//        confirmationCodeRepository.truncateAndResetAutoIncrement();
//        userRepository.truncateAndResetAutoIncrement();

        confirmationCodeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testWhenNoAuthenticationThenReturn403Users() throws Exception {
        String requestPath = "/api/users";

        mockMvc.perform(get(requestPath)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testWhenNoAuthenticationThenReturn403Admin() throws Exception {
        String requestPath = "/api/admin/users/fulldetails";

        mockMvc.perform(get(requestPath)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testWhenNoAuthorizeRoleThenReturn403Admin() throws Exception {
        String requestPath = "/api/admin/users/fulldetails";

        mockMvc.perform(get(requestPath)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testWhenReturn2003Admin() throws Exception {
        String requestPath = "/api/admin/users/fulldetails";

        mockMvc.perform(get(requestPath)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}