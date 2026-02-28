package org.example;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BankAccountTest {
    @Test
    public void testDeposit() {
        // 1. Подготовка
        User user = new User("1", "John");

        BankService bankService = new BankService();
        bankService.createAccount(user, "ACC123");
        List<BankAccount> accounts = user.getAccounts();
        BankAccount acc1 = accounts.get(0);

        // 2. Действие — пополняем счёт на 500;
        bankService.deposit(BigDecimal.valueOf(500), acc1);

        // 3. Проверка — баланс должен стать 500
        assertEquals(new BigDecimal("500"), acc1.getBalance());
    }

    @Test
    public void testWithdraw() {
        // Подготовка
        User user = new User("1", "John");
        BankService bankService = new BankService();
        bankService.createAccount(user, "ACC123");
        List<BankAccount> accounts = user.getAccounts();
        BankAccount acc1 = accounts.get(0);

        // на счету 1000
        bankService.deposit(BigDecimal.valueOf(1000), acc1);

        // Действие — снимаем 300
        bankService.withdraw(BigDecimal.valueOf(300), acc1);

        // Проверка — должно остаться 700
        assertEquals(new BigDecimal("700"), acc1.getBalance());
    }


}
