package org.demointernetshop0505.repository;

import jakarta.transaction.Transactional;
import org.demointernetshop0505.entity.ConfirmationCode;
import org.demointernetshop0505.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConfirmationCodeRepository extends JpaRepository<ConfirmationCode, Integer> {

   Optional<ConfirmationCode> findByCode(String code);

   Optional<ConfirmationCode> findByCodeAndExpireDataTimeAfter(String code, LocalDateTime currentDataTime);

   List<ConfirmationCode> findByUser(User user);

   @Modifying
   @Transactional
   @Query(value = "TRUNCATE TABLE confirmation_code", nativeQuery = true)
   void truncateAndResetAutoIncrement();
}
