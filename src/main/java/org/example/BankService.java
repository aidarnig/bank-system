/*
- Методы:
    1. `createAccount(User user, String accountNumber)`: создает новый счет для пользователя.
    2. `transfer(BankAccount source, BankAccount target, BigDecimal amount)`: переводит средства между счетами (с проверкой на достаточность средств).
    3. `getTransactionHistory(BankAccount account)`: возвращает историю транзакций для указанного счета.
    4. `getTotalBalance(User user)`: возвращает общий баланс всех счетов пользователя.
 */

package org.example;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BankService {
    // создает новый счет для пользователя.
    public void createAccount(User user, String accountNumber){
        BankAccount account = new BankAccount(accountNumber);
        user.addAccount(account);
    }

    // Переводит средства между счетами
    public void transfer(BankAccount source, BankAccount target, BigDecimal amount){

        BankAccount sourceAccount = (BankAccount) source;
        Transaction transactionSource = new Transaction("id3", amount, "TRANSFER", LocalDateTime.now(), source, target);
        BankAccount targetAccount = (BankAccount) target;
        Transaction transactionTarget = new Transaction("id3", amount, "TRANSFER", LocalDateTime.now(), source, target);

        // проверка на достаточность средств
        if (amount.compareTo(sourceAccount.balance) > 0){
            System.out.println("Недостаточно средств!");
        } else {
            sourceAccount.withdraw(amount);
            sourceAccount.addTransaction(transactionSource);
            targetAccount.deposit(amount);
            targetAccount.addTransaction(transactionTarget); }
    }

    // возвращает историю транзакций для указанного счета.
    public void getTransactionHistory(BankAccount account){
        System.out.println(account.getTransactions());
    }

    // возвращает общий баланс всех счетов пользователя.
    public BigDecimal getTotalBalance(User user){
        BigDecimal balance = new BigDecimal("0");

        // цикл для подсчета всех средств
        for (BankAccount account : user.getAccounts()) {
            balance = balance.add(account.getBalance());
        }
        return balance;
    }

    // не по условию, внес в сервис операции по пополнению и снятию
    public void deposit(BigDecimal amount, BankAccount account){
        account.deposit(amount);
        Transaction transaction = new Transaction("id2", amount, "DEPOSIT", LocalDateTime.now());
        account.addTransaction(transaction);
    }

    public void withdraw(BigDecimal amount, BankAccount account){
        // проверка на достаточность средств
        if (amount.compareTo(account.balance) > 0){
            System.out.println("Недостаточно средств!");
        } else {
            account.withdraw(amount);
            Transaction transaction = new Transaction("id1", amount, "WITHDRAW", LocalDateTime.now());
            account.addTransaction(transaction); }
    }
}
