/*
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

package org.example;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    // уникальный идентификатор транзакции
    String id;

    // сумма транзакции
    BigDecimal amount = new BigDecimal(0);

    // тип транзакции
    String type;

    // дата выполнения транзакции
    LocalDateTime date;

    // источник транзакции (если применимо)
    BankAccount sourceAccount;

    // получатель транзакции (если применимо)
    BankAccount targetAccount;

    // конструктор для снятия и пополнения
    public Transaction(String id, BigDecimal amount, String type, LocalDateTime date) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.date = date;
    }

    // конструктор для переводов (с целями и источниками)
    public Transaction(String id, BigDecimal amount, String type, LocalDateTime date, BankAccount sourceAccount, BankAccount targetAccount) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.date = date;
        this.sourceAccount = sourceAccount;
        this.targetAccount = targetAccount;
    }

    // переписал метод toString для корректного вывода истории транзакций
    @Override
    public  String toString() {
            return "Transaction{" +
                    "amount=" + amount +
                    ", type=" + type +
                    ", source=" + (sourceAccount != null ? sourceAccount.getAccountNumber() : "N/A") +
                    ", target=" + (targetAccount != null ? targetAccount.getAccountNumber() : "N/A") +
                    "}" + "\n";

    }
}