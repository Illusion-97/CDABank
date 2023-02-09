package models.interfaces;

import exceptions.InsufficientAmountException;

public interface IAccount {
    boolean deposit(double amount);
    boolean withdrawal(double amount) throws InsufficientAmountException;
}
