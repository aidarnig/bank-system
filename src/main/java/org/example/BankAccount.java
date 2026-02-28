/*
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

package org.example;

import java.math.BigDecimal;
import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BankAccount {

    // уникальный номер счета
    String accountNumber;

    // текущий баланс счета
    BigDecimal balance = new BigDecimal(0);

    // владелец счета
    User owner;

    // история транзакций
    List<Transaction> transactions = new ArrayList<>();

    // пополнение счета
    public void deposit(BigDecimal amount){
        balance = balance.add(amount);
    }

    // снятие средств со счета
    public void withdraw(BigDecimal amount){
        balance = balance.subtract(amount);
    }

    // возвращает текущий баланс
    public BigDecimal getBalance() {
        return balance;
    }

    // добавляет транзакцию в историю
    public void addTransaction(Transaction transaction){
        transactions.add(transaction);
    }

    // не по условию. Получение истории транзакций
    public List<Transaction> getTransactions() {
        return transactions;
    }

    // не по условию. Для вывода в историю транзакций
    public String getAccountNumber() {
        return accountNumber;
    }

    // не по условию. Конструктор для сохранения номера
    public BankAccount(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
