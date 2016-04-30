package fr.bellingard.accountmanager.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Represents a bank where an account is hold.
 */
public class Institution extends Element {

    private Collection<Account> accounts;

    /**
     * Creates an institution referenced by its immutable ID and with the given name.
     * @param id
     * @param name
     */
    public Institution(String id, String name) {
        super(id, name);
        accounts = new ArrayList<>();
    }

    /**
     * Adds an account in this institution.
     *
     * @param account
     */
    public void addAccount(Account account) {
        accounts.add(account);
    }

    /**
     * Removes an account from this institution.
     *
     * @param account
     */
    public void removeAccount(Account account) {
        accounts.remove(account);
    }

    /**
     * Lists all accounts of this institution.
     *
     * @return
     */
    public Collection<Account> listAccounts() {
        return accounts;
    }

    @Override
    public String toString() {
        return "Institution{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", accounts=" + Arrays.asList(accounts.stream().map(Account::getName).toArray()) +
                '}';
    }

}
