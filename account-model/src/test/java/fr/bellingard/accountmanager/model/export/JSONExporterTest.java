package fr.bellingard.accountmanager.model.export;

import fr.bellingard.accountmanager.model.*;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */
public class JSONExporterTest {

    @Test
    public void testExport() throws Exception {
        Repository repository = new Repository();

        Institution i1 = new Institution("I1", "Institution 1");
        repository.addInstitution(i1);
        Institution i2 = new Institution("I2", "Institution 2");
        repository.addInstitution(i2);

        Payee p1 = new Payee("P1", "Payee 1");
        repository.addPayee(p1);
        Payee p2 = new Payee("P2", "Payee 2");
        repository.addPayee(p2);

        Account allBankAccounts = new Account("All-BA", "All Bank Accounts");
        repository.addBankAccount(allBankAccounts);

        Account bankAccount1 = new Account("BA1", "CA");
        bankAccount1.setInstitution(i1);
        bankAccount1.setAccountNumber("12345");
        bankAccount1.setParent(allBankAccounts);
        bankAccount1.setClosed(true);
        repository.addBankAccount(bankAccount1);

        Account bankAccount2 = new Account("BA2", "LCL");
        bankAccount2.setInstitution(i2);
        bankAccount2.setAccountNumber("67890");
        bankAccount2.setParent(allBankAccounts);
        repository.addBankAccount(bankAccount2);

        Account cat1 = new Account("CAT1", "Category 1");
        repository.addCategory(cat1);

        Account cat2 = new Account("CAT2", "Category 2");
        repository.addCategory(cat2);

        new Transaction("T1", bankAccount1, cat1, "2015-01_01", 1234L).setPayee(p1);
        new Transaction("T2", bankAccount2, cat1, "2015-01_01", 5678L).setDescription("Yoohooo");
        new Transaction("T3", bankAccount2, bankAccount1, "2015-01_01", 10L).setDescription("2\n lines");

        assertThat(new File(ClassLoader.getSystemResource("export.json").toURI())).hasContent(JSONExporter.export(repository));

    }

}
