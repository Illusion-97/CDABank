package models.interfaces;

public interface ISparingAccount extends IAccount {
    double INTEREST = 1.015;
    void applyInterest();
}
