package org.example.exception;

import org.example.entity.Transaction;

public class InvalidTransactionException extends RuntimeException {
    public InvalidTransactionException(Transaction.Type type) {
        super(String.format("Неверный формат транзакции:" +
                type));
    }
}
