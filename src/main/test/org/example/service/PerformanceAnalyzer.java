/**
 * 2. **Реализовать логику с параллельными стримами и узнать их производительность**:
 * - Создайте метод `analyzePerformance`, который будет выполнять несколько различных операций с транзакциями
 * (например, фильтрация, сортировка, подсчёт) и замерять время выполнения для обычных и параллельных стримов.
 * Сравните результаты и сделайте выводы.
 */

package org.example.service;

import org.example.entity.BankAccount;
import org.example.entity.Transaction;
import org.example.entity.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class PerformanceAnalyzer {
    public static void main(String[] args) {
        PerformanceAnalyzer performanceAnalyzer = new PerformanceAnalyzer();
        performanceAnalyzer.analyzePerformance();
    }

    public  void analyzePerformance() {
        User user = generateTestUser(1000, 10000);

        // Операция 1. Подсчет трат по категории
        long start = System.nanoTime();
        long count = user.getAccounts().stream()
                .flatMap(a -> a.getTransactions().stream())
                .filter(t -> t.getType() == Transaction.Type.PAYMENT)
                .filter(t -> t.getCategory().equals("FOOD"))
                .filter(t -> isWithinLastMonth(t.getDate()))
                .count();
        long end = System.nanoTime();

        // Параллельный стрим (то же самое, но с parallelStream)
        long startParallel = System.nanoTime();
        long countParallel = user.getAccounts().parallelStream()
                .flatMap(a -> a.getTransactions().parallelStream())
                .filter(t -> t.getType() == Transaction.Type.PAYMENT)
                .filter(t -> t.getCategory().equals("FOOD"))
                .filter(t -> isWithinLastMonth(t.getDate()))
                .count();
        long endParallel = System.nanoTime();

        System.out.println("Обычный стрим (полная процедура): " + (end - start) / 1_000_000 + " ms");
        System.out.println("Параллельный стрим (полная процедура): " + (endParallel - startParallel) / 1_000_000 + " ms");

        // Операция 2. Один фильтр
        start = System.nanoTime();
        count = user.getAccounts().stream()
                .flatMap(a -> a.getTransactions().stream())
                .filter(t -> t.getType() == Transaction.Type.PAYMENT)

                .count();
        end = System.nanoTime();

        // Параллельный стрим (то же самое, но с parallelStream)
        startParallel = System.nanoTime();
        countParallel = user.getAccounts().parallelStream()
                .flatMap(a -> a.getTransactions().parallelStream())
                .filter(t -> t.getType() == Transaction.Type.PAYMENT)

                .count();
        endParallel = System.nanoTime();

        System.out.println("");
        System.out.println("Обычный стрим (flatMap filter Count): " + (end - start) / 1_000_000 + " ms");
        System.out.println("Параллельный стрим (flatMap filter Count): " + (endParallel - startParallel) / 1_000_000 + " ms");

        // Операция 3. flatMap
        start = System.nanoTime();
        count = user.getAccounts().stream()
                .flatMap(a -> a.getTransactions().stream())
                .count();
        end = System.nanoTime();

        // Параллельный стрим (то же самое, но с parallelStream)
        startParallel = System.nanoTime();
        countParallel = user.getAccounts().parallelStream()
                .flatMap(a -> a.getTransactions().parallelStream())
                .count();
        endParallel = System.nanoTime();

        System.out.println("");
        System.out.println("Обычный стрим (flatMap): " + (end - start) / 1_000_000 + " ms");
        System.out.println("Параллельный стрим (flatMap): " + (endParallel - startParallel) / 1_000_000 + " ms");

    }

    private User generateTestUser(int accountCount, int transactionsPerAccount) {
        User user = new User("Test id", "Test User");

        for (int i = 0; i < accountCount; i++) {
            BankAccount bankAccount = new BankAccount("ACC" + i);

            for (int j = 0; j < transactionsPerAccount; j++) {
                Transaction transaction = new Transaction(
                        "ID_" + j,
                        BigDecimal.valueOf(Math.random() * 10000),
                        Transaction.Type.PAYMENT,
                        randomCategory(),
                        LocalDate.now().minusDays((int) (Math.random() * 60)).atStartOfDay()
                );
                bankAccount.addTransaction(transaction);
            }
            user.addAccount(bankAccount);

        }
        return user;
    }

    private String randomCategory() {
        List<String> categories = Arrays.asList(
                "TRANSPORT", "FOOD", "SHOPPING", "RESTAURANT", "FUN", "HOBBY"
        );

        Random random = new Random();
        int randomIndex = random.nextInt(categories.size());
        return categories.get(randomIndex);
    }

    private boolean isWithinLastMonth(LocalDateTime date) {
        return date.isAfter(LocalDateTime.now().minusMonths(1)) && date.isBefore(LocalDateTime.now().plusDays(1));
    }
}


