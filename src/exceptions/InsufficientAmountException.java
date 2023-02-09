package exceptions;

public class InsufficientAmountException extends RuntimeException {
    public InsufficientAmountException(double balance, double amount) {
        super(String.format("Can't withdraw %.2f €, Current balance : %.2f €",amount,balance));
    }
}
