package models;

import java.util.ArrayList;
import java.util.List;

public class Person {
    private static int count = 0;
    private final int id;
    private final String name;
    private final int pinCode;
    private final List<Account> accounts;


    public Person(String name, int pinCode, double initialDeposit) {
        this.id = count;
        this.name = name;
        this.pinCode = pinCode;
        accounts = new ArrayList<>();
        createAccount(true,initialDeposit);
        count++;
    }

    public boolean createAccount(boolean main, double initialDeposit) {
        return accounts.add(
                main ? new MainAccount(id, accounts.size(), initialDeposit)
                        : new SparringAccount(id, accounts.size(), initialDeposit)
        );
    }

    public boolean validatePinCode(int pinCode) {
        return this.pinCode == pinCode;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Account> getAccounts() {
        return accounts;
    }
}
