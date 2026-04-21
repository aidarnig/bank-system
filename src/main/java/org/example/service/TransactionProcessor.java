package org.example.service;

import org.example.entity.BankAccount;
import org.example.entity.Transaction;
import org.example.entity.User;

import java.util.Collections;
import java.util.List;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TransactionProcessor {
    /**
    1. Создайте метод filterTransactions, который принимает User и Predicate<Transaction>, и возвращает список транзакций, удовлетворяющих условию.
     */
    public List<Transaction> filterTransactions(User user, Predicate<Transaction> predicate) {
        if (user == null || predicate == null) {
            return Collections.emptyList();
        }
        return getAllTransactions(user)
                .filter(predicate)
                .collect(Collectors.toList());
    }

    /**
    2. Создайте метод transformTransactions, который принимает User и Function<Transaction, String>, и возвращает список строковых представлений транзакций.
     */
    public List<String> transformTransactions(User user, Function<Transaction, String> function) {
        if (user == null || function == null) {
            return Collections.emptyList();
        }

        return getAllTransactions(user)
                .map(function)
                .collect(Collectors.toList());
    }

    /**
    3. Создайте метод processTransactions, который принимает User и Consumer<Transaction>, и применяет переданную функцию к каждой транзакции.
     */
    public void processTransactions(User user, Consumer<Transaction> consumer) {
        if (user == null || consumer == null) {
            return;
        }

        for (BankAccount bankAccount : user.getAccounts()) {
            for (Transaction transaction : bankAccount.getTransactions()) {
                consumer.accept(transaction);
            }
        }
    }

    /**
    4. Создайте метод createTransactionList, который принимает Supplier<List<Transaction>> и возвращает созданный список транзакций.
     */
    public List<Transaction> createTransactionList(Supplier<List<Transaction>> supplier) {
        if (supplier == null) {
            return Collections.emptyList();
        }
        return supplier.get();
    }

    /**
    5. Создайте метод mergeTransactionLists, который принимает два списка транзакций и BiFunction<List<Transaction>, List<Transaction>, List<Transaction>>,
     и возвращает объединённый список транзакций.
     */
    public List<Transaction> mergeTransactionList( List<Transaction> list1,
                                                   List<Transaction> list2,
                                                   BiFunction<List<Transaction>, List<Transaction>, List<Transaction>> merger) {
        if (merger == null) {
            return Collections.emptyList();
        }
        List<Transaction> safeList1 = list1 == null ? Collections.emptyList() : list1;
        List<Transaction> safeList2 = list2 == null ? Collections.emptyList() : list2;
        return merger.apply(safeList1, safeList2);
    }

    private Stream<Transaction> getAllTransactions(User user) {
        if (user == null) {
            return Stream.empty();
        }
        return user.getAccounts().stream()
                .flatMap(account -> account.getTransactions().stream());
    }

}
