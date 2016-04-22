package fr.bellingard.accountmanager.model;

import java.util.Optional;

/**
 *
 */
public class Transaction {

    // Mandatory
    private String id;
    private Account fromAccount;
    private Account toAccount;
    private String date;
    private Long amount;

    // Optional
    private Payee payee;
    private String description;

    public Transaction(String id, Account fromAccount, Account toAccount, String date, Long amount) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        setFromAccount(fromAccount);
        setToAccount(toAccount);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(Account fromAccount) {
        if (this.fromAccount != null) {
            this.fromAccount.removeTransaction(this);
        }
        this.fromAccount = fromAccount;
        this.fromAccount.addTransaction(this);
    }

    public Account getToAccount() {
        return toAccount;
    }

    public void setToAccount(Account toAccount) {
        if (this.toAccount != null) {
            this.toAccount.removeTransaction(this);
        }
        this.toAccount = toAccount;
        this.toAccount.addTransaction(this);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Optional<Payee> getPayee() {
        return Optional.ofNullable(payee);
    }

    public void setPayee(Payee payee) {
        this.payee = payee;
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
                ", date='" + date + '\'' +
                ", fromAccount=" + fromAccount.getName() +
                ", toAccount=" + (toAccount == null ? "null" : toAccount.getName()) +
                ", amount=" + amount +
                ", payee=" + (payee == null ? "null" : payee.getName()) +
                ", description='" + description + '\'' +
                '}';
    }
}
