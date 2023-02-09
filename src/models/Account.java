package models;

import exceptions.InsufficientAmountException;
import models.interfaces.IAccount;

public abstract class Account implements IAccount {
    protected final long id;
    protected final int ownerId;
    protected double balance = 0;

    protected Account(int ownerId, long id, double initialDeposit) {
        this.ownerId = ownerId;
        this.id = id;
        deposit(initialDeposit);
    }
    @Override
    public boolean deposit(double amount) {
        balance += Math.abs(amount);
        System.out.printf("Dépôt réussi sur le compte %d-%d\n",ownerId,id);
        return true;
    }

    @Override
    public boolean withdrawal(double amount) throws InsufficientAmountException {
        amount = Math.abs(amount);
        if (amount > balance) throw new InsufficientAmountException(balance,amount);
        balance -= amount;
        System.out.printf("Retrait réussi sur le compte %d-%d\n",ownerId,id);
        return true;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("Id : %d \t-- balance : %.2f €",id,balance);
    }
}
