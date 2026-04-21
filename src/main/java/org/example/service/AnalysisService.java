package org.example.service;

import org.example.entity.BankAccount;
import org.example.entity.Transaction;
import org.example.entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class AnalysisService {

    private boolean isWithinLastMonth(LocalDateTime dateEnd, LocalDateTime dateStart) {
        return dateEnd.isAfter(dateStart.minusMonths(1)) && dateEnd.isBefore(dateStart.plusDays(1));
    }

    private Stream<Transaction> getAllPaymentTransactions(User user) {
        if (user == null) {
            return Stream.empty();
        }
        return user.getAccounts().stream()
                .flatMap(account -> account.getTransactions().stream())
                .filter(transaction -> transaction.getType() == Transaction.Type.PAYMENT);
    }

    public BigDecimal getMonthlySpendingByCategory(BankAccount bankAccount, String category) {
        /**
         * Метод 1: Вывод суммы потраченных средств на категорию X за последний месяц со счета Y
         * - **Описание**: Метод возвращает сумму потраченных средств на указанную категорию за последний месяц.
         * - **Условия**:
         *     - Учитываются только транзакции типа `PAYMENT`.
         *     - Если категория не существует или счет равен `null`, метод возвращает `BigDecimal.ZERO`.
         *     - Транзакции старше одного месяца от текущей даты не учитываются.
         */

        if (bankAccount == null) {
            return BigDecimal.ZERO;
        }

        return bankAccount.getTransactions().stream()
                .filter(t -> t.getType() == Transaction.Type.PAYMENT)
                .filter(t -> t.getCategory().equals(category))
                .filter(t -> isWithinLastMonth(t.getDate(), LocalDateTime.now()))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        /* старая реализация
        for (Transaction transaction : bankAccount.getTransactions()) {
            if ((transaction.getType() == Transaction.Type.PAYMENT) && transaction.getCategory().equals(category) && transaction.getDate().isAfter(LocalDateTime.now().minusMonths(1)) && transaction.getDate().isBefore(LocalDateTime.now().plusDays(1))) {

                totalSpending = totalSpending.add(transaction.getAmount());
            }
        }

         */

    }

    public Map<String, BigDecimal> getMonthlySpendingByCategories(User user, Set<String> categories) {
        /**
         * Метод 2: Вывод информации о том, сколько было потрачено средств на N категорий за последний месяц со всех счетов
         * - **Описание**: Метод возвращает `Map`, где ключом является категория, а значением — сумма потраченных средств за последний месяц.
         * - **Условия**:
         *     - Учитываются только транзакции типа `PAYMENT`.
         *     - Если пользователь равен `null` или категории не существуют, метод возвращает пустую `Map`.
         */

        if (user == null || categories == null || categories.isEmpty()) {
            return new HashMap<>();
        }

        return getAllPaymentTransactions(user)
                .filter(t -> categories.contains(t.getCategory()))
                .filter(t -> isWithinLastMonth(t.getDate(),  LocalDateTime.now()))
                .collect(Collectors.groupingBy(Transaction::getCategory, Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)));

/* старая реализация
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

 */


    }

    public LinkedHashMap<String, List<Transaction>> getTransactionHistorySortedByAmount(User user) {
        /**
         * 3.2.3. Метод 3: Вывод истории операций по всем счетам и по всем категориям от наибольшей к наименьшей
         * - **Описание** : Метод возвращает `LinkedHashMap`, где ключом является категория, а значением — список транзакций, отсортированных по сумме от наибольшей к наименьшей.
         * - **Условия**:
         *     - Учитываются только транзакции типа `PAYMENT`.
         *     - Если пользователь равен `null`, метод возвращает пустую `TreeMap`.
         */

        // List<Transaction> allPayments = new ArrayList<>();

       // Map<String, List<Transaction>> allPaymentsByCategory = new HashMap<>();

        if (user == null) {
            return new LinkedHashMap<>();
        }

        return getAllPaymentTransactions(user)
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        LinkedHashMap::new,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    list.sort(Comparator.comparing(Transaction::getAmount).reversed());
                                    return list;
                                }
                        )
                ));


/* старая реализация
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

 */


    }

    public List<Transaction> getLastNTransactions(User user, int n) {
        /**
         * 3.2.4. Метод 4: Вывод последних N транзакций пользователя
         * - **Описание**: Метод возвращает последние N транзакций пользователя.
         * - **Условия**:
         *     - Если транзакций меньше `N`, возвращается их фактическое количество.
         *     - Если транзакций нет или пользователь равен `null`, метод возвращает пустой список.
         */

        if (user == null || n <= 0) {
            return Collections.emptyList();
        }

        return user.getAccounts().stream()
                .flatMap(bankAccount -> bankAccount.getTransactions().stream())
                .sorted(Comparator.comparing(Transaction::getDate).reversed())
                .limit(n)
                .collect(Collectors.toList());
/*  старая реализация
        for (BankAccount bankAccount : user.getAccounts()) {
            transactions.addAll(bankAccount.getTransactions());
        }
        transactions.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));

 */

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

        if (user == null || n <= 0) {
            return new PriorityQueue<>();
        }

        return getAllPaymentTransactions(user)
                .sorted(Comparator.comparing(Transaction::getAmount).reversed())
                .limit(n)
                .collect(Collectors.toCollection(() -> new PriorityQueue<>(Comparator.comparing(Transaction::getAmount).reversed())));
/* старая реализация
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

 */

    }

}
