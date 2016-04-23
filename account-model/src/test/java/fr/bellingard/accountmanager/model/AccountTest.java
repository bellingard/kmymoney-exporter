package fr.bellingard.accountmanager.model;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */
public class AccountTest {

    private Account account;
    private Account parentAccount;
    private Account subAccount1;
    private Account subAccount2;

    @Before
    public void init() {
        account = new Account("A001", "An Account");
        account.setInstitution(new Institution("I001", ""));
        account.setAccountNumber("123456");

        parentAccount = new Account("A00P", "Parent");
        account.setParent(parentAccount);

        subAccount1 = new Account("A00S1", "Sub1");
        subAccount1.setParent(account);
        subAccount2 = new Account("A00S2", "Sub2");
        subAccount2.setParent(account);
    }

    @Test
    public void should_read() throws Exception {
        assertThat(account.getId()).isEqualTo("A001");
        assertThat(account.getName()).isEqualTo("An Account");
        assertThat(account.getAccountNumber().get()).isEqualTo("123456");
        assertThat(account.getInstitution().get()).isEqualTo(new Institution("I001", ""));
    }

    @Test
    public void should_check_equals() throws Exception {
        assertThat(account).isEqualTo(new Account("A001", ""));
        assertThat(account).isNotEqualTo(new Account("", ""));
    }

    @Test
    public void should_update() throws Exception {
        account.setName("Foo");
        assertThat(account.getName()).isEqualTo("Foo");
    }

    @Test
    public void should_check_to_string() throws Exception {
        String message = account.toString();

        assertThat(message).contains("A001");
        assertThat(message).contains("An Account");
    }

    @Test
    public void should_have_parent_or_not() throws Exception {
        assertThat(parentAccount.getParent().isPresent()).isFalse();
        assertThat(account.getParent().get()).isEqualTo(parentAccount);
        assertThat(subAccount1.getParent().get()).isEqualTo(account);
    }

    @Test
    public void should_have_sub_accounts_or_not() throws Exception {
        assertThat(parentAccount.listSubAccount()).containsExactly(account);
        assertThat(account.listSubAccount()).containsExactly(subAccount1, subAccount2);
        assertThat(subAccount1.listSubAccount().size()).isEqualTo(0);
    }

    @Test
    public void should_move_account_to_another_parent() throws Exception {
        subAccount2.setParent(parentAccount);

        assertThat(parentAccount.listSubAccount()).containsExactly(account, subAccount2);
        assertThat(account.listSubAccount()).containsExactly(subAccount1);
    }

}
