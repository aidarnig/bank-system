package org.example;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                SpringConfig.class
        );

        // создаем пользователя
        User user = context.getBean("user",User.class);

        // создаем сервис
        BankService bankService = context.getBean("bankService", BankService.class);

        // Создаем счета
        bankService.createAccount(user, "ACC123");
        bankService.createAccount(user, "ACC456");

        // Получаем счета пользователя
        List<BankAccount> accounts = user.getAccounts();
        BankAccount acc1 = accounts.get(0);
        BankAccount acc2 = accounts.get(1);

        // Пополняем первый счет
//        acc1.deposit(new BigDecimal("1000"));
        bankService.deposit(BigDecimal.valueOf(1000), acc1);

        // Переводим средства между счетами
        bankService.transfer(acc1, acc2, new BigDecimal("400"));

        // снимаем средства со второго счета
 //       acc2.withdraw(new BigDecimal("200"));
        bankService.withdraw(BigDecimal.valueOf(200), acc2);

        // Выводим балансы
        System.out.println("Balance of ACC123: " + acc1.getBalance() + "\n");
        System.out.println("Balance of ACC456: " + acc2.getBalance() + "\n");

        // Выводим историю транзакций
        System.out.println("Transaction history for ACC123:");
        bankService.getTransactionHistory(acc1);

        // Выводим историю транзакций
        System.out.println("Transaction history for ACC456:");
        bankService.getTransactionHistory(acc2);

        // Выводим общий баланс на счете user
        System.out.println("\n" + "Total balance: " + bankService.getTotalBalance(user));


        context.close();
    }

}
