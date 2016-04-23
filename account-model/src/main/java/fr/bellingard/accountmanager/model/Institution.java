package fr.bellingard.accountmanager.model;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 */
public class Institution {

    private String id;
    private String name;
    private Collection<Account> accounts;

    public Institution(String id, String name) {
        this.id = id;
        this.name = name;
        accounts = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public void removeAccount(Account account) {
        accounts.remove(account);
    }

    public Collection<Account> listAccounts() {
        return accounts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Institution institution = (Institution) o;

        return id.equals(institution.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Institution{" +
                "name='" + name + '\'' +
                '}';
    }

}
