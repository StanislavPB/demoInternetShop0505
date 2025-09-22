package org.demointernetshop0505.service;

import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.demointernetshop0505.dto.UserRequestDto;
import org.demointernetshop0505.dto.UserResponseDto;
import org.demointernetshop0505.entity.User;
import org.demointernetshop0505.repository.UserRepository;
import org.demointernetshop0505.service.exception.AlreadyExistException;
import org.demointernetshop0505.service.exception.NotFoundException;
import org.demointernetshop0505.service.util.Converter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Data
@RequiredArgsConstructor
@Service
public class UserService{

    private final UserRepository repository;
    private final Converter converter;
    private final CodeConfirmationService codeConfirmationService;

    @Transactional
    public UserResponseDto registration(UserRequestDto request){

        if (repository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistException("User with email: " + request.getEmail() + " is already exist");
        }

        // ----- а вот если такого пользователя еще нет ------
        LocalDateTime now = LocalDateTime.now();

        User newUser = converter.fromDto(request);

        newUser.setRole(User.Role.USER);// по умолчанию роль USER
        newUser.setStatus(User.Status.NOT_CONFIRM);
        newUser.setCreateDate(now);
        newUser.setUpdateDate(now);

        repository.save(newUser);
        // после создания нового пользователя необходимо создать
        // неовый код подтверждения для него и отправить ему на почту

        codeConfirmationService.confirmationCodeManager(newUser);

        return converter.toDto(newUser);

    }

    public List<UserResponseDto> getAllUsers(){
        return converter.fromUsers(repository.findAll());
    }

    public UserResponseDto getUserById(Integer id){
        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id = " + id + " not found"));

        return converter.toDto(user);
    }


    public List<User> getAllUsersFullDetails(){
        return repository.findAll();
    }

    public UserResponseDto getUserByEmail(String email){
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email: " + email + " not found"));

        return converter.toDto(user);
    }

}
