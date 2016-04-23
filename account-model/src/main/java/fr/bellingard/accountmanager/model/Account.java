package fr.bellingard.accountmanager.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

/**
 *
 */
public class Account extends Element {

    // For every account
    private Account parent;
    private Collection<Account> subAccounts;
    private Collection<Transaction> transactions;

    // For bank accounts only
    private Institution institution;
    private String accountNumber;

    public Account(String id, String name) {
        super(id, name);
        subAccounts = new ArrayList<>();
        transactions = new ArrayList<>();
    }

    public Long getBalance() {
        return transactions.stream()
                .mapToLong(t -> t.getToAccount().equals(this) ? t.getAmount() : -t.getAmount())
                .sum();
    }

    public Optional<Account> getParent() {
        return Optional.ofNullable(parent);
    }

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

    public Collection<Account> listSubAccount() {
        return subAccounts;
    }

    public Optional<Institution> getInstitution() {
        return Optional.ofNullable(institution);
    }

    public void setInstitution(Institution institution) {
        if (this.institution != null) {
            institution.removeAccount(this);
        }
        this.institution = institution;
        institution.addAccount(this);
    }

    public Optional<String> getAccountNumber() {
        return Optional.ofNullable(accountNumber);
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public void removeTransaction(Transaction transaction) {
        transactions.remove(transaction);
    }

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
