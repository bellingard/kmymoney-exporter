package fr.bellingard.accountmanager.model;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */
public class InstitutionTest {

    private Institution institution;

    @Before
    public void init() {
        institution = new Institution("I001", "An institution");
    }

    @Test
    public void should_read() throws Exception {
        assertThat(institution.getId()).isEqualTo("I001");
        assertThat(institution.getName()).isEqualTo("An institution");
    }

    @Test
    public void should_check_equals() throws Exception {
        assertThat(institution).isEqualTo(new Institution("I001", ""));
        assertThat(institution).isNotEqualTo(new Institution("", ""));
    }

    @Test
    public void should_update() throws Exception {
        institution.setName("Foo");
        assertThat(institution.getName()).isEqualTo("Foo");
    }

    @Test
    public void should_check_to_string() throws Exception {
        String message = institution.toString();

        assertThat(message).contains("I001");
        assertThat(message).contains("An institution");
    }

    @Test
    public void should_manage_accounts() throws Exception {
        institution.addAccount(new Account("A001", "Account 1"));
        institution.addAccount(new Account("A002", "Account 2"));
        assertThat(institution.listAccounts().size()).isEqualTo(2);

        institution.removeAccount(new Account("A001", "Account 1"));
        assertThat(institution.listAccounts().size()).isEqualTo(1);
    }

}
