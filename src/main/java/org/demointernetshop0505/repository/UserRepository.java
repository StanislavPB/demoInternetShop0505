package org.demointernetshop0505.repository;

import org.demointernetshop0505.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByEmail(String email);
    // для регистрации нового пользователя
    
    Optional<User> findByEmail(String email);
    
    List<User> findByLastName(String lastName);
}
