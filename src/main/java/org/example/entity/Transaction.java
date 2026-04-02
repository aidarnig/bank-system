/**
### **2.3. Класс** `Transaction`

- Поля:
    - `id` (String): уникальный идентификатор транзакции.
    - `amount` (BigDecimal): сумма транзакции.
    - `type` (String): тип транзакции (`DEPOSIT`, `WITHDRAWAL`, `TRANSFER`).
    - `date` (LocalDateTime): дата выполнения транзакции.
    - `sourceAccount` (BankAccount): источник транзакции (если применимо).
    - `targetAccount` (BankAccount): получатель транзакции (если применимо).
- Методы:
    - Конструктор для создания транзакции.
 */

package org.example.entity;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public class Transaction {
    // уникальный идентификатор транзакции
    private final String id;

    // тип транзакции
    @Getter
    private final Type type;

    // категория (домашка 2 недели)
    @Getter
    private String category;

    // сумма транзакции
    @Getter
    private BigDecimal amount = BigDecimal.ZERO;

    // дата выполнения транзакции
    @Getter
    private final LocalDateTime date;

    // источник транзакции (если применимо)
    private BankAccount sourceAccount;

    // получатель транзакции (если применимо)
    private BankAccount targetAccount;

    // тип транзакции
    public enum Type {
        TRANSFER,
        DEPOSIT,
        WITHDRAW,
        PAYMENT
    }

    // конструктор для снятия и пополнения
    public Transaction(String id, BigDecimal amount, Type type, LocalDateTime date) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.date = date;
    }

    // конструктор для снятия и пополнения c КАТЕГОРИЯМИ (2 неделя)
    public Transaction(String id, BigDecimal amount, Type type, String category, LocalDateTime date) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.date = date;
    }

    // конструктор для переводов (с целями и источниками)
    public Transaction(String id, BigDecimal amount, Type type, LocalDateTime date, BankAccount sourceAccount, BankAccount targetAccount) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.date = date;
        this.sourceAccount = sourceAccount;
        this.targetAccount = targetAccount;
    }

    // переписал метод toString для корректного вывода истории транзакций
    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", amount=" + amount +
                ", type=" + type +
                ", source=" + (sourceAccount != null ? sourceAccount.getAccountNumber() : "N/A") +
                ", target=" + (targetAccount != null ? targetAccount.getAccountNumber() : "N/A") +
                "}" + "\n";

    }
}