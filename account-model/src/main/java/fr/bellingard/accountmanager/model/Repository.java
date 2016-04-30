package fr.bellingard.accountmanager.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Repository that holds all the information of the personal account manager.
 */
public class Repository {

    private Map<String, Institution> institutions;
    private Map<String, Payee> payees;
    private Map<String, Account> bankAccounts;
    private Map<String, Account> categories;

    /**
     * Creates the repository.
     */
    public Repository() {
        institutions = new HashMap<>();
        payees = new HashMap<>();
        bankAccounts = new HashMap<>();
        categories = new HashMap<>();
    }

    public Collection<Institution> getInstitutions() {
        return institutions.values();
    }

    /**
     * Finds an institution based on its ID.
     *
     * @param id
     * @return
     */
    public Optional<Institution> findInstitution(String id) {
        return Optional.ofNullable(institutions.get(id));
    }

    /**
     * Adds a new institution.
     *
     * @param institution
     */
    public void addInstitution(Institution institution) {
        institutions.put(institution.getId(), institution);
    }

    public Collection<Payee> getPayees() {
        return payees.values();
    }

    /**
     * Finds a payee based on its ID.
     *
     * @param id
     * @return
     */
    public Optional<Payee> findPayee(String id) {
        return Optional.ofNullable(payees.get(id));
    }

    /**
     * Adds a new payee.
     *
     * @param payee
     */
    public void addPayee(Payee payee) {
        payees.put(payee.getId(), payee);
    }

    public Collection<Account> getBankAccounts() {
        return bankAccounts.values();
    }

    /**
     * Finds a bank account based on its ID.
     *
     * @param id
     * @return
     */
    public Optional<Account> findBankAccount(String id) {
        return Optional.ofNullable(bankAccounts.get(id));
    }

    /**
     * Adds a new bank account.
     *
     * @param bankAccount
     */
    public void addBankAccount(Account bankAccount) {
        bankAccounts.put(bankAccount.getId(), bankAccount);
    }

    public Collection<Account> getCategories() {
        return categories.values();
    }

    /**
     * Finds a category based on its ID.
     *
     * @param id
     * @return
     */
    public Optional<Account> findCategory(String id) {
        return Optional.ofNullable(categories.get(id));
    }

    /**
     * Adds a new category.
     *
     * @param category
     */
    public void addCategory(Account category) {
        categories.put(category.getId(), category);
    }
}
