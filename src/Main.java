import exceptions.InsufficientAmountException;
import models.Account;
import models.AccountAction;
import models.Person;
import models.interfaces.IAccount;
import models.interfaces.IMainAccount;
import models.interfaces.ISparingAccount;

import java.util.*;

public class Main {
    private static final List<Person> clients = List.of(
            new Person("Yanis", 1234, 5000),
            new Person("Jérémy", 4321, 25000)
    );
    private static int actionCount = 0;
    private static Person selectedPerson = null;
    public static void main(String[] args) {
        // every 10 Account action completed -> applyInterest on each SparingAccount
        manageInterest();
    }

    // region Inputs
    private static int getInputInt(String message) {
        System.out.print(message + " : ");
        try {
            return new ExitTool<Integer>().checkExitCode(new Scanner(System.in).nextInt());
        } catch (InputMismatchException ime) {
            System.out.println("Saisie incorrecte, veuillez saisir un nombre.");
            return getInputInt(message);
        }
    }
    private static double getInputDouble(String message) {
        System.out.print(message + " : ");
        try {
            return new ExitTool<Double>().checkExitCode(new Scanner(System.in).nextDouble()); // Must use , instead of .
        } catch (InputMismatchException ime) {
            System.out.println("Saisie incorrecte, veuillez saisir un nombre.");
            return getInputDouble(message);
        }
    }
    private static boolean getYesNoChoice(String message) {
        try {
            System.out.print(message + " : ");
            return new ExitTool<String>().checkExitCode(new Scanner(System.in).next("[yYnN]"))
                    .equalsIgnoreCase("Y");
        } catch (NoSuchElementException nee) {
            System.out.println("Choix invalide, veuillez réessayer");
            return getYesNoChoice(message);
        }
    }
    // endregion

    // region Actions
    private static void displayPersons() {
        clients.forEach(person -> System.out.printf("%d\t->\t%s\n",person.getId(),person.getName()));
    }
    private static Person getPersonChoice() {
        System.out.println("Sélectionnez un client :");
        displayPersons();
        try {
            return clients.get(getInputInt("Client"));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Client introuvable, veuillez réessayer.");
            return getPersonChoice();
        }
    }
    private static boolean validatePerson(Person person) {
        return person.validatePinCode(getInputInt("Veuillez saisir votre mot de passe"));
    }
    private static void displayAccount(Person person, boolean showBalance) {
        if(showBalance) person.getAccounts().forEach(System.out::println);
        else person.getAccounts().forEach(account -> System.out.printf(
                "%d\t->\t%s\n",
                account.getId(),
                account.getClass().getSimpleName()));
    }
    private static Account getPersonAccountChoice(Person person, boolean showBalance) {
        System.out.println("Sélectionnez un compte :");
        displayAccount(person, showBalance);
        try {
            return person.getAccounts().get(getInputInt("Compte"));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Compte introuvable, veuillez réessayer.");
            return getPersonAccountChoice(person, showBalance);
        }
    }
    private static void displayAccountAction() {
        System.out.println("Que souhaitez vous faire ?");
        Arrays.stream(AccountAction.values()).forEach(action -> System.out.printf("%d\t->\t%s\n",action.ordinal(),action.name()));
    }
    private static AccountAction getAccountActionChoice() {
        displayAccountAction();
        try {
            return AccountAction.values()[getInputInt("Action")];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Action invalide veuillez réessayer");
            return getAccountActionChoice();
        }
    }
    // endregion
    private static boolean doAction(){
        System.out.println("A tout moment, pour quitter, saisissez : " + ExitTool.EXIT_CODE );
        if(selectedPerson == null) {
            Person logingPerson = getPersonChoice();
            int attempt = 0;
            while (!validatePerson(logingPerson)) {
                System.out.println("Code erroné");
                attempt++;
                if (attempt > 2) {
                    System.out.println("Rejet connexion");
                    return false;
                }
            }
            selectedPerson = logingPerson;
        }
        if(getYesNoChoice("Souhaitez vous créer un compte ? (Y/N)")) {
            return selectedPerson.createAccount(
                    getYesNoChoice("Souhaitez vous un compte courant ? (Y/N)"),
                    getInputDouble("Saisissez le montant de votre dépôt initial"));
        }
        IAccount selectedIAccount = getPersonAccountChoice(selectedPerson,true);
        switch (getAccountActionChoice()) {
            case DEPOSIT -> {
                return selectedIAccount.deposit(getInputDouble("Saisissez un montant à déposer"));
            }
            case WITHDRAWAL -> {
                try {
                    return selectedIAccount.withdrawal(getInputDouble("Saisissez un montant à retirer"));
                } catch (InsufficientAmountException e) {
                    System.out.println("Opération abandonnée : " + e.getMessage());
                    return false;
                }
            }
            case TRANSFER -> {
                try {
                    Person destPerson =  getPersonChoice();
                    Account destAccount = getPersonAccountChoice(destPerson, destPerson == selectedPerson);
                    if(destAccount != selectedIAccount) {
                        return ((IMainAccount) selectedIAccount)
                                .send(destAccount,
                                        getInputDouble("Saisissez un montant à transférer"));
                    }
                    System.out.println("Impossible de transférer vers le même compte.");
                    return false;
                } catch (ClassCastException castException) {
                    System.out.println("Les virements ne sont possible que depuis un compte courant.");
                    return false;
                }
            }
            default -> {
                System.out.println("Action inconnue.");
                return false;
            }
        }
    }
    private static void manageInterest() {
        if(doAction()) {
            actionCount++;
            if (actionCount % 3 == 0) {
                System.out.print("Application des intérêts ... ");
                clients.stream().map(Person::getAccounts)
                        .reduce(new ArrayList<>(), (resultList, currentList) -> {
                            resultList.addAll(currentList);
                            return resultList;
                        })
                        .stream()
                        .filter(account -> account instanceof ISparingAccount)
                        .map(account -> (ISparingAccount) account)
                        .forEach(ISparingAccount::applyInterest);
                System.out.println("Terminée.");
            }
        }
        if(!getYesNoChoice("Souhaitez vous continuer ? (Y/N)")) selectedPerson = null;
        manageInterest();
    }
}