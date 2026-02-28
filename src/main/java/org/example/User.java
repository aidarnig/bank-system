/*
### **2.2. Класс** `User`

- Поля:
    - `id` (String): уникальный идентификатор пользователя.
    - `name` (String): имя пользователя.
    - `accounts` (List<BankAccount>): список счетов пользователя.
- Методы:
    - `addAccount(BankAccount account)`: добавление нового счета пользователю.
    - `getAccounts()`: возвращает список счетов пользователя.
 */

package org.example;

import java.util.ArrayList;
import java.util.List;

public class User {

    // уникальный идентификатор пользователя
    String id;

    // имя пользователя
    String name;

    // список счетов пользователя
    List<BankAccount> accounts = new ArrayList<BankAccount>();

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // добавление нового счета пользователю
    public void addAccount(BankAccount account){
        accounts.add(account);
    }

    // возвращает список счетов пользователя.
    public List<BankAccount> getAccounts() {
        return accounts;
    }

}
