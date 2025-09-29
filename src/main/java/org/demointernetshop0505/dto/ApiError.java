package org.demointernetshop0505.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class ApiError {
    // общее описание ошибки ("Validation failed", "User not found")
    private String error;

    // Детализированное сообщение ("Пользователь с ID = 2 не найденЭ)
    private String message;

    // Название параметра, если ошибка связана с ним ("userId")
    private String parameter;

    // Значение, которое вызвало ошибку
    private Object rejectedValue;

    // Список ошибок валидации (если есть)
    private List<Map<String,Object>> errors;

    // Время ошибки
    private LocalDateTime timestamp;

}
