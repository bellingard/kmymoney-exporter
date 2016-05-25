package fr.bellingard.accountmanager.kmymoney;

import fr.bellingard.accountmanager.model.Account;
import fr.bellingard.accountmanager.model.Repository;
import fr.bellingard.accountmanager.model.export.JSONExporter;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */
public class KMyMoneyReaderTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private static Repository repository;

    @BeforeClass
    public static void prepareFile() throws Exception {
        Path file = Paths.get(ClassLoader.getSystemResource("sample.kmy").toURI());
        repository = new Repository();
        KMyMoneyReader.on(file).populate(repository);

        dumpStructure(repository);
        dumpBankAccountBalance(repository);
    }

    @Test
    public void try_read_wrong_kmy_file() throws Exception {
        Path file = Paths.get(ClassLoader.getSystemResource("sample.xml").toURI());
        Repository repository = new Repository();

        exception.expect(ReaderException.class);
        exception.expectMessage("Could not read KMyMoneyFile");
        exception.expectMessage("sample.xml");

        KMyMoneyReader.on(file).populate(repository);
    }

    @Test
    public void try_read_non_valid_kmy_file() throws Exception {
        Path file = Paths.get(ClassLoader.getSystemResource("non-valid.kmy").toURI());
        Repository repository = new Repository();

        exception.expect(ReaderException.class);
        exception.expectMessage("Could not parse KMyMoneyFile");
        exception.expectMessage("non-valid.kmy");

        KMyMoneyReader.on(file).populate(repository);
    }

    @Test
    public void should_read_institutions() throws Exception {
        assertThat(repository.getInstitutions().size()).isEqualTo(1);
    }

    @Test
    public void should_read_payees() throws Exception {
        assertThat(repository.getPayees().size()).isEqualTo(4);
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

    @Test
    public void should_read_transactions() throws Exception {
        int numberOfBankTransactions = repository.getBankAccounts().stream()
                .mapToInt(a -> a.listTransactions().size())
                .sum(); // 7 - because one transaction is from one bank account to another
        int numberOfCategoryTransactions = repository.getCategories().stream()
                .mapToInt(a -> a.listTransactions().size())
                .sum(); // 5

        assertThat(numberOfBankTransactions + numberOfCategoryTransactions).isEqualTo(6 * 2);

        int numberOfUniqueTransactions = repository.getBankAccounts().stream()
                .flatMap(a -> a.listTransactions().stream())
                .distinct()
                .toArray().length;
        assertThat(numberOfUniqueTransactions).isEqualTo(6);
    }

    @Test
    public void should_compute_balance_for_bank_accounts() throws Exception {
        Account account1 = repository.findBankAccount("A000003").get();
        assertThat(account1.getBalance()).isEqualTo(988266);

        Account account2 = repository.findBankAccount("A000001").get();
        assertThat(account2.getBalance()).isEqualTo(640090);
    }

    private static void dumpStructure(Repository repository) {
        print(JSONExporter.export(repository));
    }

    private static void dumpBankAccountBalance(Repository repository) {
        repository.getBankAccounts().stream()
                .forEach(a -> print(a.getId() + " / " + a.getName() + " => " + (a.getBalance().floatValue() / 100)));
    }

    private static void print(Object a) {
        System.out.println(a);
    }
}
