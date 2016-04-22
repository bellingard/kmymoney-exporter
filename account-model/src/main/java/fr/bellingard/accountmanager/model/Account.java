package fr.bellingard.accountmanager.model;

import java.util.*;

/**
 *
 */
public class Account {

    // For every account
    private String id;
    private String name;
    private Account parent;
    private Collection<Account> subAccounts;
    private Collection<Transaction> transactions;

    // For bank accounts only
    private Institution institution;
    private String accountNumber;

    public Account(String id, String name) {
        this.id = id;
        this.name = name;
        subAccounts = new ArrayList<>();
        transactions = new ArrayList<>();
    }

    public Long getBalance() {
        return transactions.stream()
                .mapToLong(t -> (t.getFromAccount() == this ? t.getAmount() : - t.getAmount()))
                .sum();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Optional<Account> getParent() {
        return Optional.ofNullable(parent);
    }

    public void setParent(Account parent) {
        this.parent = parent;
        parent.addSubAccount(this);
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

    public void addSubAccount(Account account) {
        subAccounts.add(account);
    }

    public Collection<Account> listSubAccount() {
        return subAccounts;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", parent=" + (parent == null ? "null" : parent.getName()) +
                ", subAccounts=" + Arrays.asList(subAccounts.stream().map(a -> a.getName()).toArray()) +
                ", institution=" + institution +
                ", accountNumber='" + accountNumber + '\'' +
                ", transactions=" + transactions.size() +
                '}';
    }
}
