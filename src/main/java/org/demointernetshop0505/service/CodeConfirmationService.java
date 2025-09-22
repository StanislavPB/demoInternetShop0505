package org.demointernetshop0505.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.demointernetshop0505.entity.ConfirmationCode;
import org.demointernetshop0505.entity.User;
import org.demointernetshop0505.repository.ConfirmationCodeRepository;
import org.demointernetshop0505.service.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@RequiredArgsConstructor
@Service
public class CodeConfirmationService {

    private final ConfirmationCodeRepository repository;

    private final int EXPIRATION_PERIOD = 1;

    private final String LINK_PATH = "localhost:8080/api/users/code/confirmation?code=";

    public void confirmationCodeManager(User user){
        String code = generateCode();
        saveConfirmationCode(code, user);
        sendCodeByEmail(code, user);
    }

    private void sendCodeByEmail(String code, User user) {

        String linkToSend = LINK_PATH + code;

        // туту будет отправка пользователю письма с кодом

        System.out.printf("Код подтверждения: " + linkToSend);


    }

    private void saveConfirmationCode(String generatedCode, User user) {
        ConfirmationCode newCode = ConfirmationCode.builder()
                .code(generatedCode)
                .user(user)
                .expireDataTime(LocalDateTime.now().plusDays(EXPIRATION_PERIOD))
                .isConfirmed(false)
                .build();

        repository.save(newCode);
    }

    private String generateCode() {

        // universal uniq identifier
        // формат 128 bit
        // xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
        // где каждый символ 'x' - это либо цифра либо символ от a-f
        // 3f29c3b2-9fc2-11ed-a8fc-0242ac120002

        String code = UUID.randomUUID().toString();
        return code;

    }

    public User changeConfirmationStatusByCode(String code){
        ConfirmationCode confirmationCode = repository.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Confirmation code: " + code + " not found"));

        User user = confirmationCode.getUser();

        confirmationCode.setConfirmed(true);

        repository.save(confirmationCode);

        return user;
    }

}
