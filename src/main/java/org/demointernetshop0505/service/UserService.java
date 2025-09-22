package org.demointernetshop0505.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.demointernetshop0505.dto.UserRequestDto;
import org.demointernetshop0505.dto.UserResponseDto;
import org.demointernetshop0505.dto.UserUpdateRequestDto;
import org.demointernetshop0505.entity.User;
import org.demointernetshop0505.repository.UserRepository;
import org.demointernetshop0505.service.exception.AlreadyExistException;
import org.demointernetshop0505.service.exception.NotFoundException;
import org.demointernetshop0505.service.util.Converter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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


    @Transactional
    public UserResponseDto confirmationEmail(String code){

        User user = codeConfirmationService.changeConfirmationStatusByCode(code);

        user.setStatus(User.Status.CONFIRMED);

        repository.save(user);

        return converter.toDto(user);
    }


    @Transactional
    public UserResponseDto updateUser(UserUpdateRequestDto updateRequest){

        if (updateRequest.getEmail() == null || updateRequest.getEmail().isBlank()){
            throw new IllegalArgumentException("Email must be provided to update user");
        }

        String userEmail = updateRequest.getEmail();

        // найдем пользователя по email
        User userByEmail = repository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User with email: " + userEmail + " not found"));

        // обновляем все доступные поля
        // мы заранее НЕ ЗНАЕМ, а какие именно поля пользователь захочет поменять
        // то есть в JSON (в теде запроса) будут находится ТОЛЬКО те поля (со значением)
        // которые пользователь хочет менять (не обязательно все)
        if (updateRequest.getFirstName() != null && !updateRequest.getFirstName().isBlank()) {
            userByEmail.setFirstName(updateRequest.getFirstName());
        }

        if (updateRequest.getLastName() != null && !updateRequest.getLastName().isBlank()) {
            userByEmail.setLastName(updateRequest.getLastName());
        }

        if (updateRequest.getHashPassword() != null && !updateRequest.getHashPassword().isBlank()) {
            userByEmail.setHashPassword(updateRequest.getHashPassword());
        }

        // сохраняем (обновляем) пользователя
        repository.save(userByEmail);

        return converter.toDto(userByEmail);
        // или вручную создать UserResponseDto из данных, которые хранятся в userByEmail
    };

    @Transactional
    public boolean deleteUser(Integer id){

        // проверим, что такой id существует
        // и если нет - то сразу возвращаем false и ничего даже не пытаемся удалить

        if (!repository.existsById(id)){
            return false;
        }

        // если существует, то
        // вариант 1 - удаляем сразу по id

        repository.deleteById(id);

        // вариант 2 - сперва найдем объект по этому номеру id

//        User userForDelete = repository.findById(id).get();
//
//        repository.delete(userForDelete);

        return true;

    }


    @Transactional
    public boolean renewCode(String email){

        User user = repository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email: " + email + " not found"));

        codeConfirmationService.confirmationCodeManager(user);
        return true;
    }



}
