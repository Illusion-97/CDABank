package models;

import exceptions.InsufficientAmountException;
import models.interfaces.IMainAccount;

public class MainAccount extends Account implements IMainAccount {
    protected MainAccount(int ownerId, long id, double initialDeposit) {
        super(ownerId, id, initialDeposit);
    }

    @Override
    public boolean send(Account destAccount, double amount) {
        try {
            withdrawal(amount);
            return destAccount.deposit(amount);
        } catch (InsufficientAmountException iae) {
            System.out.printf("Can't send to %d-%d : %s\n",destAccount.ownerId,destAccount.id, iae.getMessage());
            return false;
        }
    }
}
