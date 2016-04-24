package fr.bellingard.accountmanager.model;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */
public class TransactionTest {

    private Transaction transaction;
    private Account bankAccount;
    private Account category;

    @Before
    public void init() {
        bankAccount = new Account("A001", "My Bank Account");
        category = new Account("A002", "Income");
        transaction = new Transaction("T1", bankAccount, category, "2015-03-15", 1200L);
        transaction.setDescription("Desc");
        transaction.setPayee(new Payee("P1", ""));
    }

    @Test
    public void should_read() throws Exception {
        assertThat(transaction.getId()).isEqualTo("T1");
        assertThat(transaction.getDate()).isEqualTo("2015-03-15");
        assertThat(transaction.getAmount()).isEqualTo(1200L);
        assertThat(transaction.getToAccount()).isEqualTo(bankAccount);
        assertThat(transaction.getFromAccount()).isEqualTo(category);
        assertThat(transaction.getPayee().get()).isEqualTo(new Payee("P1", ""));
        assertThat(transaction.getDescription().get()).isEqualTo("Desc");
    }

    @Test
    public void should_check_equals() throws Exception {
        assertThat(transaction).isEqualTo(new Transaction("T1", bankAccount, category, "2015-03-15", 1200L));
        assertThat(transaction).isNotEqualTo(new Transaction("T2", bankAccount, category, "2015-03-15", 1200L));
    }

    @Test
    public void should_update() throws Exception {
        transaction.setDate("XX");
        assertThat(transaction.getDate()).isEqualTo("XX");

        transaction.setAmount(1L);
        assertThat(transaction.getAmount()).isEqualTo(1L);

        Account a1 = new Account("A1", "");
        transaction.setToAccount(a1);
        assertThat(transaction.getToAccount()).isEqualTo(a1);
        transaction.setFromAccount(a1);
        assertThat(transaction.getFromAccount()).isEqualTo(a1);
    }

    @Test
    public void should_check_to_string() throws Exception {
        String message = transaction.toString();

        assertThat(message).contains("T1");
    }

}
