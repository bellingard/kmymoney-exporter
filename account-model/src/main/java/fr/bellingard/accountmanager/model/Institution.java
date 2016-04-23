package fr.bellingard.accountmanager.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 *
 */
public class Institution extends Element {

    private Collection<Account> accounts;

    public Institution(String id, String name) {
        super(id, name);
        accounts = new ArrayList<>();
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

    @Override
    public String toString() {
        return "Institution{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", accounts=" + Arrays.asList(accounts.stream().map(Account::getName).toArray()) +
                '}';
    }

}
