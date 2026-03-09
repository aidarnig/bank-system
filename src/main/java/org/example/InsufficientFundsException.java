package org.example;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException {

    // Конструктор с сообщением
    public InsufficientFundsException(String message) {
        super(message);
    }

    // Конструктор с сообщением и причиной
    public InsufficientFundsException(String message, Throwable cause) {
        super(message, cause);
    }

    // Конструктор с суммой
    public InsufficientFundsException(BigDecimal balance, BigDecimal amount) {
        super(String.format("Недостаточно средств. Баланс: %.2f, запрошено: %.2f",
                balance, amount));
    }
}