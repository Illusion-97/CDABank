package models;

import models.interfaces.ISparingAccount;

public class SparringAccount extends Account implements ISparingAccount {
    protected SparringAccount(int ownerId, long id, double initialDeposit) {
        super(ownerId, id, initialDeposit);
    }

    @Override
    public void applyInterest() {
        balance *= INTEREST;
    }
}
