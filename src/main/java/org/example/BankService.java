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
import java.util.List;
import java.util.UUID;

public class BankService {
    // создает новый счет для пользователя.
    public void createAccount(User user, String accountNumber) {
        BankAccount account = new BankAccount(accountNumber);
        user.addAccount(account);
    }
/*
    // Переводит средства между счетами
    public void transfer(BankAccount source, BankAccount target, BigDecimal amount) {

        Transaction transactionSource = new Transaction(UUID.randomUUID().toString(), amount, Transaction.Type.TRANSFER, LocalDateTime.now(), source, target);
        Transaction transactionTarget = new Transaction(UUID.randomUUID().toString(), amount, Transaction.Type.TRANSFER, LocalDateTime.now(), source, target);

        // проверка на достаточность средств
        if (amount.compareTo(source.balance) > 0) {
            throw new InsufficientFundsException(source.balance, amount);
        } else {
            source.withdraw(amount);
            source.addTransaction(transactionSource);
            target.deposit(amount);
            target.addTransaction(transactionTarget);
        }
    }

 */

    // возвращает историю транзакций для указанного счета.
    public List<Transaction> getTransactionHistory(BankAccount account) {
        return account.getTransactions();
    }

    // возвращает общий баланс всех счетов пользователя.
    public BigDecimal getTotalBalance(User user) {
        BigDecimal balance = new BigDecimal("0");

        // цикл для подсчета всех средств
        for (BankAccount account : user.getAccounts()) {
            balance = balance.add(account.getBalance());
        }
        return balance;
    }

    // общий метод для списания / начисления
    public void createTransaction(Transaction.Type type, BigDecimal amount, BankAccount source, BankAccount target) {
        Transaction transaction;
        switch (type) {
            case DEPOSIT:
                source.deposit(amount);
                source.addTransaction(new Transaction(UUID.randomUUID().toString(), amount, type, LocalDateTime.now()));
                break;
            case WITHDRAW:
                // проверка на достаточность средств
                if (amount.compareTo(source.balance) > 0) {
                    throw new InsufficientFundsException(source.balance, amount);
                } else {
                    source.withdraw(amount);
                    source.addTransaction(new Transaction(UUID.randomUUID().toString(), amount, type, LocalDateTime.now()));
                }
                break;

            case TRANSFER:
                // проверка на достаточность средств
                if (amount.compareTo(source.balance) > 0) {
                    throw new InsufficientFundsException(source.balance, amount);
                } else {
                    source.withdraw(amount);
                    source.addTransaction(new Transaction(UUID.randomUUID().toString(), amount, Transaction.Type.TRANSFER, LocalDateTime.now(), source, target));
                    target.deposit(amount);
                    target.addTransaction(new Transaction(UUID.randomUUID().toString(), amount, Transaction.Type.TRANSFER, LocalDateTime.now(), source, target));
                }
                break;

            default:
                throw new InvalidTransactionException(type);

        }
    }
/*
    // не по условию, внес в сервис операции по пополнению и снятию
    public void deposit(BigDecimal amount, BankAccount account) {
        account.deposit(amount);
        Transaction transaction = new Transaction(UUID.randomUUID().toString(), amount, Transaction.Type.DEPOSIT, LocalDateTime.now());
        account.addTransaction(transaction);
    }

    public void withdraw(BigDecimal amount, BankAccount account) {
        // проверка на достаточность средств
        if (amount.compareTo(account.balance) > 0) {
            System.out.println("Недостаточно средств!");
        } else {
            account.withdraw(amount);
            Transaction transaction = new Transaction(UUID.randomUUID().toString(), amount, Transaction.Type.WITHDRAW, LocalDateTime.now());
            account.addTransaction(transaction);
        }
    }

 */
}
