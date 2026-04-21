package org.example.service;

import org.example.entity.BankAccount;
import org.example.entity.Transaction;
import org.example.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class AnalysisServiceTest {
    private BankService bankService;
    private User testUser;
    private BankAccount acc1;
    private BankAccount acc2;
    private AnalysisService analysisService;

    @BeforeEach
    public void setup() {
        bankService = new BankService();
        analysisService = new AnalysisService();
        testUser = new User("1", "Test User");

        bankService.createAccount(testUser, "ACC001");
        bankService.createAccount(testUser, "ACC002");

        List<BankAccount> accounts = testUser.getAccounts();
        acc1 = accounts.get(0);
        acc2 = accounts.get(1);
/*
        acc1.addTransaction(new Transaction(UUID.randomUUID().toString(), BigDecimal.valueOf(500), Transaction.Type.PAYMENT, "FOOD", LocalDateTime.now().minusDays(1)));
        acc1.addTransaction(new Transaction(UUID.randomUUID().toString(), BigDecimal.valueOf(300), Transaction.Type.PAYMENT, "FOOD", LocalDateTime.now().minusDays(5)));
        acc1.addTransaction(new Transaction(UUID.randomUUID().toString(), BigDecimal.valueOf(600), Transaction.Type.PAYMENT, "TRANSPORT", LocalDateTime.now().minusMonths(1)));
        acc1.addTransaction(new Transaction(UUID.randomUUID().toString(), BigDecimal.valueOf(100), Transaction.Type.PAYMENT, "TRANSPORT", LocalDateTime.now().minusMonths(2)));
        acc1.addTransaction(new Transaction(UUID.randomUUID().toString(), BigDecimal.valueOf(250), Transaction.Type.PAYMENT, "CAFE", LocalDateTime.now().minusDays(2)));

        acc2.addTransaction(new Transaction(UUID.randomUUID().toString(), BigDecimal.valueOf(700), Transaction.Type.PAYMENT, "FOOD", LocalDateTime.now().minusDays(8)));
        acc2.addTransaction(new Transaction(UUID.randomUUID().toString(), BigDecimal.valueOf(120), Transaction.Type.PAYMENT, "FOOD", LocalDateTime.now().minusDays(12)));
        acc2.addTransaction(new Transaction(UUID.randomUUID().toString(), BigDecimal.valueOf(990), Transaction.Type.PAYMENT, "TRANSPORT", LocalDateTime.now().minusMonths(3)));
        acc2.addTransaction(new Transaction(UUID.randomUUID().toString(), BigDecimal.valueOf(10), Transaction.Type.PAYMENT, "TRANSPORT", LocalDateTime.now().minusMonths(5)));
        acc2.addTransaction(new Transaction(UUID.randomUUID().toString(), BigDecimal.valueOf(750), Transaction.Type.PAYMENT, "CAFE", LocalDateTime.now().minusDays(1)));

        acc1.addTransaction(new Transaction(UUID.randomUUID().toString(), BigDecimal.valueOf(510), Transaction.Type.WITHDRAW, "FOOD", LocalDateTime.now().minusDays(1)));
        acc1.addTransaction(new Transaction(UUID.randomUUID().toString(), BigDecimal.valueOf(310), Transaction.Type.DEPOSIT, "FOOD", LocalDateTime.now().minusDays(6)));
        acc2.addTransaction(new Transaction(UUID.randomUUID().toString(), BigDecimal.valueOf(610), Transaction.Type.WITHDRAW, "TRANSPORT", LocalDateTime.now().minusMonths(1)));
        acc2.addTransaction(new Transaction(UUID.randomUUID().toString(), BigDecimal.valueOf(110), Transaction.Type.DEPOSIT, "TRANSPORT", LocalDateTime.now().minusMonths(2)));
*/
    }

    @Test
    void testGetMonthlySpendingByCategory_Success() {
        // given
        // Создаём транзакцию PAYMENT категории "Food" за сегодня на 890
        Transaction t1 = new Transaction("1", new BigDecimal("890"), Transaction.Type.PAYMENT,
                "FOOD", LocalDateTime.now());
        acc1.addTransaction(t1);

        // Транзакция старше месяца (не должна учитываться)
        Transaction t2 = new Transaction("2", new BigDecimal("390"), Transaction.Type.PAYMENT,
               "FOOD" , LocalDateTime.now().minusMonths(2));
        acc1.addTransaction(t2);

        // Транзакция другой категории (не должна учитываться)
        Transaction t3 = new Transaction("3", new BigDecimal("200"), Transaction.Type.PAYMENT,
               "TRANSPORT" , LocalDateTime.now());
        acc1.addTransaction(t3);

        // when
        BigDecimal result = analysisService.getMonthlySpendingByCategory(acc1, "FOOD");

        // then
        assertEquals(new BigDecimal("890"), result);
    }

    @Test
    void testGetMonthlySpendingByCategory_NoMatchingTransactions() {
        // given
        Transaction t1 = new Transaction("1", new BigDecimal("100"), Transaction.Type.PAYMENT,
                "TRANSPORT", LocalDateTime.now());
        acc1.addTransaction(t1);

        // when
        BigDecimal result = analysisService.getMonthlySpendingByCategory(acc1, "FOOD");

        // then
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void testGetMonthlySpendingByCategory_NullAccount() {
        // when
        BigDecimal result = analysisService.getMonthlySpendingByCategory(null, "FOOD");

        // then
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void testGetMonthlySpendingByCategory_NullCategory() {
        // given
        Transaction t1 = new Transaction("1", new BigDecimal("100"), Transaction.Type.PAYMENT,
                "FOOD", LocalDateTime.now());
        acc1.addTransaction(t1);

        // when
        BigDecimal result = analysisService.getMonthlySpendingByCategory(acc1, null);

        // then
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void testGetMonthlySpendingByCategory_OnlyPaymentsConsidered() {
        // given
        // DEPOSIT не должен учитываться
        Transaction deposit = new Transaction("1", new BigDecimal("1000"), Transaction.Type.DEPOSIT,
                "FOOD", LocalDateTime.now());
        acc1.addTransaction(deposit);

        // WITHDRAW не должен учитываться
        Transaction withdraw = new Transaction("2", new BigDecimal("200"), Transaction.Type.WITHDRAW,
                "FOOD", LocalDateTime.now());
        acc1.addTransaction(withdraw);

        // PAYMENT должен учитываться
        Transaction payment = new Transaction("3", new BigDecimal("300"), Transaction.Type.PAYMENT,
                "FOOD", LocalDateTime.now());
        acc1.addTransaction(payment);

        // when
        BigDecimal result = analysisService.getMonthlySpendingByCategory(acc1, "FOOD");

        // then
        assertEquals(new BigDecimal("300"), result);
    }


    @Test
    void testGetMonthlySpendingByCategories_Success() {
        // given
        // Счёт 1: транзакции Food и Transport
        Transaction t1 = new Transaction("1", new BigDecimal("500"), Transaction.Type.PAYMENT,
                "FOOD", LocalDateTime.now());
        Transaction t2 = new Transaction("2", new BigDecimal("300"), Transaction.Type.PAYMENT,
                "TRANSPORT", LocalDateTime.now());
        acc1.addTransaction(t1);
        acc1.addTransaction(t2);

        // Счёт 2: транзакция Food
        Transaction t3 = new Transaction("3", new BigDecimal("200"), Transaction.Type.PAYMENT,
                "FOOD", LocalDateTime.now());
        acc2.addTransaction(t3);

        Set<String> categories = Set.of("FOOD", "TRANSPORT", "ENTERTAINMENT");

        // when
        Map<String, BigDecimal> result = analysisService.getMonthlySpendingByCategories(testUser, categories);

        // then
        assertEquals(new BigDecimal("700"), result.get("FOOD"));  // 500 + 200
        assertEquals(new BigDecimal("300"), result.get("TRANSPORT"));
        assertFalse(result.containsKey("ENTERTAINMENT")); // нет трат по этой категории
        assertEquals(2, result.size());
    }

    @Test
    void testGetMonthlySpendingByCategories_OnlyLastMonth() {
        // given
        Transaction recent = new Transaction("1", new BigDecimal("500"), Transaction.Type.PAYMENT,
                "FOOD", LocalDateTime.now());
        Transaction old = new Transaction("2", new BigDecimal("1000"), Transaction.Type.PAYMENT,
                "FOOD", LocalDateTime.now().minusMonths(2));
        acc1.addTransaction(recent);
        acc1.addTransaction(old);

        Set<String> categories = Set.of("FOOD");

        // when
        Map<String, BigDecimal> result = analysisService.getMonthlySpendingByCategories(testUser, categories);

        // then
        assertEquals(new BigDecimal("500"), result.get("FOOD"));
    }

    @Test
    void testGetMonthlySpendingByCategories_NullUser() {
        // given
        Set<String> categories = Set.of("FOOD");

        // when
        Map<String, BigDecimal> result = analysisService.getMonthlySpendingByCategories(null, categories);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetMonthlySpendingByCategories_NullCategories() {
        // when
        Map<String, BigDecimal> result = analysisService.getMonthlySpendingByCategories(testUser, null);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetMonthlySpendingByCategories_EmptyCategories() {
        // when
        Map<String, BigDecimal> result = analysisService.getMonthlySpendingByCategories(testUser, Set.of());

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetMonthlySpendingByCategories_UserWithoutAccounts() {
        // given
        User emptyUser = new User("empty", "Empty User");

        Set<String> categories = Set.of("FOOD");

        // when
        Map<String, BigDecimal> result = analysisService.getMonthlySpendingByCategories(emptyUser, categories);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetTransactionHistorySortedByAmount_Success() {
        // given
        // Транзакции категории Food
        Transaction food1 = new Transaction("1", new BigDecimal("300"), Transaction.Type.PAYMENT,
                "FOOD", LocalDateTime.now());
        Transaction food2 = new Transaction("2", new BigDecimal("500"), Transaction.Type.PAYMENT,
                "FOOD", LocalDateTime.now());
        Transaction food3 = new Transaction("3", new BigDecimal("100"), Transaction.Type.PAYMENT,
                "FOOD", LocalDateTime.now());

        // Транзакции категории Transport
        Transaction trans1 = new Transaction("4", new BigDecimal("400"), Transaction.Type.PAYMENT,
                "TRANSPORT", LocalDateTime.now());
        Transaction trans2 = new Transaction("5", new BigDecimal("200"), Transaction.Type.PAYMENT,
                "TRANSPORT", LocalDateTime.now());

        acc1.addTransaction(food1);
        acc1.addTransaction(food2);
        acc1.addTransaction(food3);
        acc1.addTransaction(trans1);
        acc1.addTransaction(trans2);

        // when
        LinkedHashMap<String, List<Transaction>> result =
                analysisService.getTransactionHistorySortedByAmount(testUser);

        // then
        // Проверяем, что категории присутствуют
        assertTrue(result.containsKey("FOOD"));
        assertTrue(result.containsKey("TRANSPORT"));

        // Проверяем сортировку внутри категории Food (500 → 300 → 100)
        List<Transaction> foodTransactions = result.get("FOOD");
        assertEquals(new BigDecimal("500"), foodTransactions.get(0).getAmount());
        assertEquals(new BigDecimal("300"), foodTransactions.get(1).getAmount());
        assertEquals(new BigDecimal("100"), foodTransactions.get(2).getAmount());

        // Проверяем сортировку внутри категории Transport (400 → 200)
        List<Transaction> transportTransactions = result.get("TRANSPORT");
        assertEquals(new BigDecimal("400"), transportTransactions.get(0).getAmount());
        assertEquals(new BigDecimal("200"), transportTransactions.get(1).getAmount());
    }

    @Test
    void testGetTransactionHistorySortedByAmount_OnlyPaymentsConsidered() {
        // given
        Transaction payment = new Transaction("1", new BigDecimal("500"), Transaction.Type.PAYMENT,
                "FOOD", LocalDateTime.now());
        Transaction deposit = new Transaction("2", new BigDecimal("1000"), Transaction.Type.DEPOSIT,
                "FOOD", LocalDateTime.now());

        acc1.addTransaction(payment);
        acc1.addTransaction(deposit);

        // when
        LinkedHashMap<String, List<Transaction>> result =
                analysisService.getTransactionHistorySortedByAmount(testUser);

        // then
        // Только PAYMENT попал в результат
        assertEquals(1, result.get("FOOD").size());
        assertEquals(new BigDecimal("500"), result.get("FOOD").get(0).getAmount());
    }

    @Test
    void testGetTransactionHistorySortedByAmount_EmptyTransactions() {
        // when
        LinkedHashMap<String, List<Transaction>> result =
                analysisService.getTransactionHistorySortedByAmount(testUser);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetTransactionHistorySortedByAmount_NullUser() {
        // when
        LinkedHashMap<String, List<Transaction>> result =
                analysisService.getTransactionHistorySortedByAmount(null);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetTransactionHistorySortedByAmount_CategoryWithoutPayments() {
        // given
        // Только DEPOSIT, не PAYMENT
        Transaction deposit = new Transaction("1", new BigDecimal("500"), Transaction.Type.DEPOSIT,
                "FOOD", LocalDateTime.now());
        acc1.addTransaction(deposit);

        // when
        LinkedHashMap<String, List<Transaction>> result =
                analysisService.getTransactionHistorySortedByAmount(testUser);

        // then
        assertFalse(result.containsKey("FOOD"));
    }

    @Test
    void testGetLastNTransactions_Success() {
        // given
        // Создаём транзакции с разными датами
        Transaction t1 = new Transaction("1", new BigDecimal("100"), Transaction.Type.PAYMENT,
                "FOOD", LocalDateTime.now().minusDays(10));
        Transaction t2 = new Transaction("2", new BigDecimal("200"), Transaction.Type.PAYMENT,
                "TRANSPORT", LocalDateTime.now().minusDays(5));
        Transaction t3 = new Transaction("3", new BigDecimal("300"), Transaction.Type.DEPOSIT,
                "SALARY", LocalDateTime.now().minusDays(1));
        Transaction t4 = new Transaction("4", new BigDecimal("400"), Transaction.Type.PAYMENT,
                "ENTERTAINMENT", LocalDateTime.now());

        acc1.addTransaction(t1);
        acc1.addTransaction(t2);
        acc1.addTransaction(t3);
        acc1.addTransaction(t4);

        // when
        List<Transaction> result = analysisService.getLastNTransactions(testUser, 3);

        // then
        assertEquals(3, result.size());
        // Самые новые должны быть первыми
        assertEquals(t4, result.get(0));
        assertEquals(t3, result.get(1));
        assertEquals(t2, result.get(2));
    }

    @Test
    void testGetLastNTransactions_LessThanN() {
        // given
        Transaction t1 = new Transaction("1", new BigDecimal("100"), Transaction.Type.PAYMENT,
                "FOOD", LocalDateTime.now());
        Transaction t2 = new Transaction("2", new BigDecimal("200"), Transaction.Type.PAYMENT,
                "FOOD", LocalDateTime.now().minusDays(1));

        acc1.addTransaction(t1);
        acc1.addTransaction(t2);

        // when
        List<Transaction> result = analysisService.getLastNTransactions(testUser, 10);

        // then
        assertEquals(2, result.size());
    }

    @Test
    void testGetLastNTransactions_NoTransactions() {
        // when
        List<Transaction> result = analysisService.getLastNTransactions(testUser, 5);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetLastNTransactions_NullUser() {
        // when
        List<Transaction> result = analysisService.getLastNTransactions(null, 5);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetLastNTransactions_NegativeN() {
        // given
        Transaction t1 = new Transaction("1", new BigDecimal("100"), Transaction.Type.PAYMENT,
                "FOOD", LocalDateTime.now());
        acc1.addTransaction(t1);

        // when
        List<Transaction> result = analysisService.getLastNTransactions(testUser, -1);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetLastNTransactions_ZeroN() {
        // given
        Transaction t1 = new Transaction("1", new BigDecimal("100"), Transaction.Type.PAYMENT,
                "FOOD", LocalDateTime.now());
        acc1.addTransaction(t1);

        // when
        List<Transaction> result = analysisService.getLastNTransactions(testUser, 0);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetLastNTransactions_AllTypesConsidered() {
        // given
        Transaction payment = new Transaction("1", new BigDecimal("100"), Transaction.Type.PAYMENT,
                "FOOD", LocalDateTime.now());
        Transaction deposit = new Transaction("2", new BigDecimal("200"), Transaction.Type.DEPOSIT,
                "SALARY", LocalDateTime.now());
        Transaction withdraw = new Transaction("3", new BigDecimal("300"), Transaction.Type.WITHDRAW,
                "CASH", LocalDateTime.now());

        acc1.addTransaction(payment);
        acc1.addTransaction(deposit);
        acc1.addTransaction(withdraw);

        // when
        List<Transaction> result = analysisService.getLastNTransactions(testUser, 3);

        // then
        assertEquals(3, result.size()); // Все типы должны попасть
    }

    @Test
    void testGetTopNLargestTransactions_Success() {
        // given
        // Создаём транзакции с разными суммами
        Transaction t1 = new Transaction("1", new BigDecimal("500"), Transaction.Type.PAYMENT,
                "FOOD", LocalDateTime.now());
        Transaction t2 = new Transaction("2", new BigDecimal("1000"), Transaction.Type.PAYMENT,
                "TRANSPORT", LocalDateTime.now());
        Transaction t3 = new Transaction("3", new BigDecimal("300"), Transaction.Type.PAYMENT,
                "ENTERTAINMENT", LocalDateTime.now());
        Transaction t4 = new Transaction("4", new BigDecimal("700"), Transaction.Type.PAYMENT,
                "SHOPPING", LocalDateTime.now());

        acc1.addTransaction(t1);
        acc1.addTransaction(t2);
        acc1.addTransaction(t3);
        acc1.addTransaction(t4);

        // when
        PriorityQueue<Transaction> result = analysisService.getTopNLargestTransactions(testUser, 2);

        // then
        assertEquals(2, result.size());
        // Очередь должна отдавать самые большие первыми
        Transaction first = result.poll();
        Transaction second = result.poll();

        assertEquals(new BigDecimal("1000"), first.getAmount());
        assertEquals(new BigDecimal("700"), second.getAmount());
    }

    @Test
    void testGetTopNLargestTransactions_LessThanN() {
        // given
        Transaction t1 = new Transaction("1", new BigDecimal("500"), Transaction.Type.PAYMENT,
                "FOOD", LocalDateTime.now());
        Transaction t2 = new Transaction("2", new BigDecimal("300"), Transaction.Type.PAYMENT,
                "TRANSPORT", LocalDateTime.now());

        acc1.addTransaction(t1);
        acc1.addTransaction(t2);

        // when
        PriorityQueue<Transaction> result = analysisService.getTopNLargestTransactions(testUser, 5);

        // then
        assertEquals(2, result.size());
    }

    @Test
    void testGetTopNLargestTransactions_OnlyPaymentsConsidered() {
        // given
        Transaction payment = new Transaction("1", new BigDecimal("500"), Transaction.Type.PAYMENT,
                "FOOD", LocalDateTime.now());
        Transaction deposit = new Transaction("2", new BigDecimal("1000"), Transaction.Type.DEPOSIT,
                "SALARY", LocalDateTime.now());

        acc1.addTransaction(payment);
        acc1.addTransaction(deposit);

        // when
        PriorityQueue<Transaction> result = analysisService.getTopNLargestTransactions(testUser, 2);

        // then
        assertEquals(1, result.size());
        assertEquals(new BigDecimal("500"), result.poll().getAmount());
    }

    @Test
    void testGetTopNLargestTransactions_NoPayments() {
        // given
        Transaction deposit = new Transaction("1", new BigDecimal("1000"), Transaction.Type.DEPOSIT,
                "SALARY", LocalDateTime.now());
        acc1.addTransaction(deposit);

        // when
        PriorityQueue<Transaction> result = analysisService.getTopNLargestTransactions(testUser, 3);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetTopNLargestTransactions_NullUser() {
        // when
        PriorityQueue<Transaction> result = analysisService.getTopNLargestTransactions(null, 5);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetTopNLargestTransactions_NegativeN() {
        // given
        Transaction t1 = new Transaction("1", new BigDecimal("500"), Transaction.Type.PAYMENT,
                "FOOD", LocalDateTime.now());
        acc1.addTransaction(t1);

        // when
        PriorityQueue<Transaction> result = analysisService.getTopNLargestTransactions(testUser, -1);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetTopNLargestTransactions_ZeroN() {
        // given
        Transaction t1 = new Transaction("1", new BigDecimal("500"), Transaction.Type.PAYMENT,
                "FOOD", LocalDateTime.now());
        acc1.addTransaction(t1);

        // when
        PriorityQueue<Transaction> result = analysisService.getTopNLargestTransactions(testUser, 0);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetTopNLargestTransactions_CorrectOrder() {
        // given
        Transaction t1 = new Transaction("1", new BigDecimal("100"), Transaction.Type.PAYMENT,
                "A", LocalDateTime.now());
        Transaction t2 = new Transaction("2", new BigDecimal("500"), Transaction.Type.PAYMENT,
                "B", LocalDateTime.now());
        Transaction t3 = new Transaction("3", new BigDecimal("300"), Transaction.Type.PAYMENT,
                "C", LocalDateTime.now());
        Transaction t4 = new Transaction("4", new BigDecimal("400"), Transaction.Type.PAYMENT,
                "D", LocalDateTime.now());

        acc1.addTransaction(t1);
        acc1.addTransaction(t2);
        acc1.addTransaction(t3);
        acc1.addTransaction(t4);

        // when
        PriorityQueue<Transaction> result = analysisService.getTopNLargestTransactions(testUser, 4);

        // then
        // Ожидаемый порядок: 500 → 400 → 300 → 100
        assertEquals(new BigDecimal("500"), result.poll().getAmount());
        assertEquals(new BigDecimal("400"), result.poll().getAmount());
        assertEquals(new BigDecimal("300"), result.poll().getAmount());
        assertEquals(new BigDecimal("100"), result.poll().getAmount());
    }

    @Test
    void testGetTopNLargestTransactions_MultipleAccounts() {
        // given
        Transaction t1 = new Transaction("1", new BigDecimal("1000"), Transaction.Type.PAYMENT,
                "FOOD", LocalDateTime.now());
        Transaction t2 = new Transaction("2", new BigDecimal("2000"), Transaction.Type.PAYMENT,
                "TRANSPORT", LocalDateTime.now());

        acc1.addTransaction(t1);
        acc2.addTransaction(t2);

        // when
        PriorityQueue<Transaction> result = analysisService.getTopNLargestTransactions(testUser, 1);

        // then
        assertEquals(new BigDecimal("2000"), result.poll().getAmount());
    }
}
