package fr.bellingard.accountmanager.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 *
 */
public class Repository {

    private Map<String, Institution> institutions;
    private Map<String, Payee> payees;
    private Map<String, Account> bankAccounts;
    private Map<String, Account> categories;

    public Repository() {
        institutions = new HashMap<>();
        payees = new HashMap<>();
        bankAccounts = new HashMap<>();
        categories = new HashMap<>();
    }

    public Collection<Institution> getInstitutions() {
        return institutions.values();
    }

    public Optional<Institution> findInstitution(String id) {
        return Optional.ofNullable(institutions.get(id));
    }

    public void addInstitution(Institution institution) {
        institutions.put(institution.getId(), institution);
    }

    public Collection<Payee> getPayees() {
        return payees.values();
    }

    public Optional<Payee> findPayee(String id) {
        return Optional.ofNullable(payees.get(id));
    }

    public void addPayee(Payee payee) {
        payees.put(payee.getId(), payee);
    }

    public Collection<Account> getBankAccounts() {
        return bankAccounts.values();
    }

    public Optional<Account> findBankAccount(String id) {
        return Optional.ofNullable(bankAccounts.get(id));
    }

    public void addBankAccount(Account bankAccount) {
        bankAccounts.put(bankAccount.getId(), bankAccount);
    }

    public Collection<Account> getCategories() {
        return categories.values();
    }

    public Optional<Account> findCategory(String id) {
        return Optional.ofNullable(categories.get(id));
    }

    public void addCategory(Account category) {
        categories.put(category.getId(), category);
    }
}
