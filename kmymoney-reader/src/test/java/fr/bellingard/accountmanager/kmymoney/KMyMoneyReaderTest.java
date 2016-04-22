package fr.bellingard.accountmanager.kmymoney;

import fr.bellingard.accountmanager.model.Repository;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */
public class KMyMoneyReaderTest {

    private static Repository repository;

    @BeforeClass
    public static void prepareFile() throws Exception {
        Path file = Paths.get(ClassLoader.getSystemResource("sample.kmy").toURI());
        repository = new Repository();
        KMyMoneyReader.on(file).populate(repository);

        dumpStructure(repository);
    }

    @Test
    public void should_read_institutions() throws Exception {
        assertThat(repository.getInstitutions().size()).isEqualTo(1);
    }

    @Test
    public void should_read_payees() throws Exception {
        assertThat(repository.getPayees().size()).isEqualTo(3);
    }

    @Test
    public void should_read_accounts() throws Exception {
        assertThat(repository.getBankAccounts().size() + repository.getCategories().size()).isEqualTo(11);
    }

    @Test
    public void should_read_bank_accounts() throws Exception {
        assertThat(repository.getBankAccounts().size()).isEqualTo(2);
    }

    @Test
    public void should_read_categories() throws Exception {
        assertThat(repository.getCategories().size()).isEqualTo(9);
    }

    private static void dumpStructure(Repository repository) {
        print("####  INSTITUTIONS ####");
        repository.getInstitutions().stream().forEach(KMyMoneyReaderTest::print);
        print("\n\n####  PAYEES ####");
        repository.getPayees().stream().forEach(KMyMoneyReaderTest::print);
        print("\n\n####  BANK ACCOUNTS ####");
        repository.getBankAccounts().stream().forEach(KMyMoneyReaderTest::print);
        print("\n\n####  CATEGORIES ####");
        repository.getCategories().stream().forEach(KMyMoneyReaderTest::print);
    }

    private static void print(Object a) {
        System.out.println(a);
    }
}
