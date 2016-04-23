package fr.bellingard.accountmanager.model;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */
public class PayeeTest {

    private Payee payee;

    @Before
    public void init() {
        payee = new Payee("P001", "A Payee");
    }

    @Test
    public void should_read() throws Exception {
        assertThat(payee.getId()).isEqualTo("P001");
        assertThat(payee.getName()).isEqualTo("A Payee");
    }

    @Test
    public void should_check_equals() throws Exception {
        assertThat(payee).isEqualTo(new Payee("P001", ""));
        assertThat(payee).isNotEqualTo(new Payee("", ""));
    }

    @Test
    public void should_update() throws Exception {
        payee.setName("Foo");
        assertThat(payee.getName()).isEqualTo("Foo");
    }

    @Test
    public void should_check_to_string() throws Exception {
        String message = payee.toString();

        assertThat(message).contains("P001");
        assertThat(message).contains("A Payee");
    }

}
