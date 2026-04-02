package org.example.service;

import org.example.entity.BankAccount;
import org.example.entity.Transaction;
import org.example.entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class AnalysisService {

    public BigDecimal getMonthlySpendingByCategory(BankAccount bankAccount, String category) {
        /**
         * Метод 1: Вывод суммы потраченных средств на категорию X за последний месяц со счета Y
         * - **Описание**: Метод возвращает сумму потраченных средств на указанную категорию за последний месяц.
         * - **Условия**:
         *     - Учитываются только транзакции типа `PAYMENT`.
         *     - Если категория не существует или счет равен `null`, метод возвращает `BigDecimal.ZERO`.
         *     - Транзакции старше одного месяца от текущей даты не учитываются.
         */
        BigDecimal totalSpending = BigDecimal.ZERO;

        if (bankAccount == null || category == null) {
            return totalSpending;
        }

        for (Transaction transaction : bankAccount.getTransactions()) {
            if ((transaction.getType() == Transaction.Type.PAYMENT) && transaction.getCategory().equals(category) && transaction.getDate().isAfter(LocalDateTime.now().minusMonths(1)) && transaction.getDate().isBefore(LocalDateTime.now().plusDays(1))) {

                totalSpending = totalSpending.add(transaction.getAmount());
            }
        }
        return totalSpending;
    }

    public Map<String, BigDecimal> getMonthlySpendingByCategories(User user, Set<String> categories) {
        /**
         * Метод 2: Вывод информации о том, сколько было потрачено средств на N категорий за последний месяц со всех счетов
         * - **Описание**: Метод возвращает `Map`, где ключом является категория, а значением — сумма потраченных средств за последний месяц.
         * - **Условия**:
         *     - Учитываются только транзакции типа `PAYMENT`.
         *     - Если пользователь равен `null` или категории не существуют, метод возвращает пустую `Map`.
         */

        Map<String, BigDecimal> monthlySpending = new HashMap<>();

        if (user == null || categories == null || categories.isEmpty()) {
            return monthlySpending;
        }

        for (BankAccount bankAccount : user.getAccounts()) {
            for (Transaction transaction : bankAccount.getTransactions()) {
                if ((transaction.getType() == Transaction.Type.PAYMENT) && transaction.getDate().isAfter(LocalDateTime.now().minusMonths(1)) && transaction.getDate().isBefore(LocalDateTime.now().plusDays(1)) && categories.contains(transaction.getCategory())) {
                    // если категория уже есть в мапе - суммируем, если нет - добавляем
                    if (!monthlySpending.containsKey(transaction.getCategory())) {
                        monthlySpending.put(transaction.getCategory(), transaction.getAmount());
                    } else {
                        monthlySpending.put(transaction.getCategory(), monthlySpending.get(transaction.getCategory()).add(transaction.getAmount()));
                    }

                }
            }
        }
        return monthlySpending;

    }

    public LinkedHashMap<String, List<Transaction>> getTransactionHistorySortedByAmount(User user) {
        /**
         * 3.2.3. Метод 3: Вывод истории операций по всем счетам и по всем категориям от наибольшей к наименьшей
         * - **Описание** : Метод возвращает `LinkedHashMap`, где ключом является категория, а значением — список транзакций, отсортированных по сумме от наибольшей к наименьшей.
         * - **Условия**:
         *     - Учитываются только транзакции типа `PAYMENT`.
         *     - Если пользователь равен `null`, метод возвращает пустую `TreeMap`.
         */

        LinkedHashMap<String, List<Transaction>> transactionsHistory = new LinkedHashMap<>();
        List<Transaction> allPayments = new ArrayList<>();

        if (user == null) {
            return transactionsHistory;
        }

        for (BankAccount bankAccount : user.getAccounts()) {
            for (Transaction transaction : bankAccount.getTransactions()) {
                if ((transaction.getType() == Transaction.Type.PAYMENT)) {
                    allPayments.add(transaction);
                }
            }
        }

        Map<String, List<Transaction>> grouped = new HashMap<>();
        for (Transaction transaction : allPayments) {
            if (!grouped.containsKey(transaction.getCategory())) {
                grouped.put(transaction.getCategory(), new ArrayList<>());
            }
            grouped.get(transaction.getCategory()).add(transaction);
        }

        for (String category : grouped.keySet()) {
            grouped.get(category).sort(Comparator.comparing(Transaction::getAmount).reversed());
        }

        transactionsHistory.putAll(grouped);
        return transactionsHistory;

    }

    public List<Transaction> getLastNTransactions(User user, int n) {
        /**
         * 3.2.4. Метод 4: Вывод последних N транзакций пользователя
         * - **Описание**: Метод возвращает последние N транзакций пользователя.
         * - **Условия**:
         *     - Если транзакций меньше `N`, возвращается их фактическое количество.
         *     - Если транзакций нет или пользователь равен `null`, метод возвращает пустой список.
         */
        List<Transaction> transactions = new ArrayList<>();

        if (user == null || n <= 0) {
            return transactions;
        }

        for (BankAccount bankAccount : user.getAccounts()) {
            transactions.addAll(bankAccount.getTransactions());
        }
        transactions.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));

        return new ArrayList<>(transactions.subList(0, Math.min(n, transactions.size())));

    }

    public PriorityQueue<Transaction> getTopNLargestTransactions(User user, int n) {
        /**
         * 3.2.5. Метод 5: Вывод топ-N самых больших платежных транзакций пользователя
         * - **Описание** : Метод возвращает `PriorityQueue`, содержащую топ-`N` самых больших транзакций.
         * - **Условия**:
         *     - Учитываются только транзакции типа `PAYMENT`.
         *     - Если транзакций меньше `N`, возвращаются все доступные.
         *     - Если транзакций нет или пользователь равен `null`, метод возвращает пустую очередь.
         */

        PriorityQueue<Transaction> pq = new PriorityQueue<>(
                (t1, t2) -> t2.getAmount().compareTo(t1.getAmount())
        );

        if (user == null || n <= 0) {
            return pq;
        }

        for (BankAccount bankAccount : user.getAccounts()) {
            for (Transaction transaction : bankAccount.getTransactions()) {
                if ((transaction.getType() == Transaction.Type.PAYMENT)) {
                    pq.add(transaction);
                }
            }
        }

        PriorityQueue<Transaction> result = new PriorityQueue<>(pq.comparator()); // копируем компаратор
        int limit = Math.min(n, pq.size());

        for (int i = 0; i < limit; i++) {
            result.add(pq.poll());
        }
        return result;

    }

}
