package fr.bellingard.accountmanager.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

/**
 * Represents a bank account or a category.
 */
public class Account extends Element {

    // For every account
    private Account parent;
    private Collection<Account> subAccounts;
    private Collection<Transaction> transactions;

    // For bank accounts only
    private Institution institution;
    private String accountNumber;

    /**
     * Creates an account.
     *
     * @param id
     * @param name
     */
    public Account(String id, String name) {
        super(id, name);
        subAccounts = new ArrayList<>();
        transactions = new ArrayList<>();
    }

    /**
     * Computes and returns the balance.
     *
     * @return
     */
    public Long getBalance() {
        return transactions.stream()
                .mapToLong(t -> t.getToAccount().equals(this) ? t.getAmount() : -t.getAmount())
                .sum();
    }

    /**
     * Returns the parent of this account, if any.
     *
     * @return
     */
    public Optional<Account> getParent() {
        return Optional.ofNullable(parent);
    }

    /**
     * Sets the parent of this account.
     *
     * @param parent
     */
    public void setParent(Account parent) {
        if (this.parent != null) {
            this.parent.removeSubAccount(this);
        }
        this.parent = parent;
        parent.addSubAccount(this);
    }

    private void addSubAccount(Account account) {
        subAccounts.add(account);
    }

    private void removeSubAccount(Account account) {
        subAccounts.remove(account);
    }

    /**
     * Lists sub accounts.
     *
     * @return
     */
    public Collection<Account> listSubAccount() {
        return subAccounts;
    }

    /**
     * Give the institution of this account. Only available if this is a bank account.
     *
     * @return
     */
    public Optional<Institution> getInstitution() {
        return Optional.ofNullable(institution);
    }

    /**
     * Sets the institution for this (bank) account.
     *
     * @param institution
     */
    public void setInstitution(Institution institution) {
        if (this.institution != null) {
            this.institution.removeAccount(this);
        }
        this.institution = institution;
        institution.addAccount(this);
    }

    /**
     * Give the bank account number. Only available if this is a bank account.
     *
     * @return
     */
    public Optional<String> getAccountNumber() {
        return Optional.ofNullable(accountNumber);
    }

    /**
     * Sets the bank account number.
     *
     * @param accountNumber
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * Adds the given transaction.
     *
     * @param transaction
     */
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    /**
     * Removes the given transaction.
     *
     * @param transaction
     */
    public void removeTransaction(Transaction transaction) {
        transactions.remove(transaction);
    }

    /**
     * Lists all the transactions.
     *
     * @return
     */
    public Collection<Transaction> listTransactions() {
        return transactions;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", parent=" + (parent == null ? "null" : parent.getName()) +
                ", subAccounts=" + Arrays.asList(subAccounts.stream().map(Account::getName).toArray()) +
                ", institution=" + (institution == null ? "" : institution.getName()) +
                ", accountNumber='" + accountNumber + '\'' +
                ", transactions=" + transactions.size() +
                '}';
    }
}
