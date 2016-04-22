package fr.bellingard.accountmanager.model;

import java.util.Optional;

/**
 *
 */
public class Transaction {

    // Mandatory
    private String id;
    private Account bankAccount;
    private String date;
    private Float amount;

    // Optional
    private Payee payee;
    private Account category;
    private String description;

    public Transaction(String id, Account bankAccount, String date, Float amount) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        setBankAccount(bankAccount);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Account getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(Account bankAccount) {
        if (this.bankAccount != null) {
            this.bankAccount.removeTransaction(this);
        }
        this.bankAccount = bankAccount;
        this.bankAccount.addTransaction(this);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Optional<Payee> getPayee() {
        return Optional.ofNullable(payee);
    }

    public void setPayee(Payee payee) {
        this.payee = payee;
    }

    public Optional<Account> getCategory() {
        return Optional.ofNullable(category);
    }

    public void setCategory(Account category) {
        if (this.category != null) {
            this.category.removeTransaction(this);
        }
        this.category = category;
        this.category.addTransaction(this);
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", bankAccount=" + bankAccount.getName() +
                ", date='" + date + '\'' +
                ", amount=" + amount +
                ", payee=" + (payee == null ? "null" : payee.getName()) +
                ", category=" + (category == null ? "null" : category.getName()) +
                ", description='" + description + '\'' +
                '}';
    }
}
