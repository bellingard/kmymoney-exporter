package fr.bellingard.accountmanager.model;

import java.util.Optional;

/**
 *
 */
public class Transaction {

    // Mandatory
    private String id;
    private Account toAccount;
    private Account fromAccount;
    private String date;
    private Long amount;

    // Optional
    private Payee payee;
    private String description;

    public Transaction(String id, Account toAccount, Account fromAccount, String date, Long amount) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        setToAccount(toAccount);
        setFromAccount(fromAccount);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Transaction transaction = (Transaction) o;

        return id.equals(transaction.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", toAccount=" + toAccount.getName() +
                ", fromAccount=" + (fromAccount == null ? "null" : fromAccount.getName()) +
                ", amount=" + amount +
                ", payee=" + (payee == null ? "null" : payee.getName()) +
                ", description='" + description + '\'' +
                '}';
    }
}
