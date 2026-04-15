/**
### **2.2. Класс** `User`

- Поля:
    - `id` (String): уникальный идентификатор пользователя.
    - `name` (String): имя пользователя.
    - `accounts` (List<BankAccount>): список счетов пользователя.
- Методы:
    - `addAccount(BankAccount account)`: добавление нового счета пользователю.
    - `getAccounts()`: возвращает список счетов пользователя.
 */

package org.example.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class User {

    // уникальный идентификатор пользователя
    @NonNull
    private final String id;

    // имя пользователя
    @NonNull
    private String name;

    // возвращает список счетов пользователя.
    // список счетов пользователя
    @Getter
    private final List<BankAccount> accounts = new ArrayList<BankAccount>();

    // добавление нового счета пользователю
    public void addAccount(BankAccount account){
        accounts.add(account);
    }

}
