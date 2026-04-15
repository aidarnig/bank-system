/**
### **2.1. Класс** `BankAccount`

- Поля:
    - `accountNumber` (String): уникальный номер счета.
    - `balance` (BigDecimal): текущий баланс счета.
    - `owner` (User): владелец счета.
    - `transactions` (List<Transaction>): история транзакций.
- Методы:
    - `deposit(BigDecimal amount)`: пополнение счета.
    - `withdraw(BigDecimal amount)`: снятие средств со счета (с проверкой на достаточность средств).
    - `getBalance()`: возвращает текущий баланс.
    - `addTransaction(Transaction transaction)`: добавляет транзакцию в историю.
 */

package org.example.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class BankAccount {

    // не по условию. Для вывода в историю транзакций
    // уникальный номер счета
    @NonNull
    @Getter
    private final String accountNumber;

    // возвращает текущий баланс
    // текущий баланс счета
    @Getter
    private BigDecimal balance = BigDecimal.ZERO;

    // владелец счета
    private User owner;

    // не по условию. Получение истории транзакций
    // история транзакций
    @Getter
    private final List<Transaction> transactions = new ArrayList<>();

    // пополнение счета
    public void deposit(BigDecimal amount) {
        balance = balance.add(amount);
    }

    // снятие средств со счета
    public void withdraw(BigDecimal amount) {
        balance = balance.subtract(amount);
    }

    // добавляет транзакцию в историю
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

}
