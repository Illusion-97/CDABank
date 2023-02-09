package models.interfaces;

public interface IMainAccount extends IAccount {
    boolean send(models.Account destAccount, double amount);
}
