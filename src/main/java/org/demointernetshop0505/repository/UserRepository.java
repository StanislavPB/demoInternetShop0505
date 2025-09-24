package org.demointernetshop0505.repository;

import jakarta.transaction.Transactional;
import org.demointernetshop0505.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByEmail(String email);
    // для регистрации нового пользователя
    
    Optional<User> findByEmail(String email);
    
    List<User> findByLastName(String lastName);

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE account", nativeQuery = true)
    void truncateAndResetAutoIncrement();
}
