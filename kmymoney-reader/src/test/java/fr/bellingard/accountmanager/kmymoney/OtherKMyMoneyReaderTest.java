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
public class OtherKMyMoneyReaderTest {

    private static Repository repository;

    @BeforeClass
    public static void prepareFile() throws Exception {
        Path file = Paths.get("/Users/bellingard/AccountPersonalApp/some-tests/Comptes.kmy");
        repository = new Repository();
        KMyMoneyReader.on(file).populate(repository);

        //dumpStructure(repository);
    }

    @Test
    public void should_read_institutions() throws Exception {
        assertThat(repository.getInstitutions().size()).isEqualTo(4);
    }

    @Test
    public void should_read_payees() throws Exception {
        assertThat(repository.getPayees().size()).isEqualTo(500);
    }

    @Test
    public void should_read_accounts() throws Exception {
        assertThat(repository.getBankAccounts().size() + repository.getCategories().size()).isEqualTo(228);
    }

    @Test
    public void should_read_transactions() throws Exception {
        int numberOfBankTransactions = repository.getBankAccounts().stream()
                .mapToInt(a -> a.listTransactions().size())
                .sum();
        int numberOfCategoryTransactions = repository.getCategories().stream()
                .mapToInt(a -> a.listTransactions().size())
                .sum();

        assertThat(numberOfBankTransactions + numberOfCategoryTransactions).isEqualTo(4542 * 2);
    }

    private static void dumpStructure(Repository repository) {
        print("####  INSTITUTIONS ####");
        repository.getInstitutions().stream().forEach(OtherKMyMoneyReaderTest::print);
        print("\n\n####  BANK ACCOUNTS ####");
        repository.getBankAccounts().stream().forEach(OtherKMyMoneyReaderTest::print);
        print("\n\n####  PAYEES ####");
        repository.getPayees().stream().forEach(OtherKMyMoneyReaderTest::print);
        print("\n\n####  CATEGORIES ####");
        repository.getCategories().stream().forEach(OtherKMyMoneyReaderTest::print);
    }

    private static void print(Object a) {
        System.out.println(a);
    }
}
