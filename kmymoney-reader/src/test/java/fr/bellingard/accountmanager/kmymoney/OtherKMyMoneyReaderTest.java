package fr.bellingard.accountmanager.kmymoney;

import fr.bellingard.accountmanager.model.Repository;
import fr.bellingard.accountmanager.model.export.JSONExporter;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.file.Files;
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
        Path file = Paths.get("/Users/bellingard/Repos/_PERSO_/_resources_/some-tests/Comptes.kmy");
        repository = new Repository();
        KMyMoneyReader.on(file).populate(repository);

        dumpStructure(repository);
        dumpBankAccountBalance(repository);
    }

    @Test
    public void should_read_institutions() throws Exception {
        assertThat(repository.getInstitutions().size()).isEqualTo(4);
    }

    @Test
    public void should_read_payees() throws Exception {
        assertThat(repository.getPayees().size()).isEqualTo(560);
    }

    @Test
    public void should_read_accounts() throws Exception {
        assertThat(repository.getBankAccounts().size() + repository.getCategories().size()).isEqualTo(231);
    }

    @Test
    public void should_read_transactions() throws Exception {
        int numberOfBankTransactions = repository.getBankAccounts().stream()
                .mapToInt(a -> a.listTransactions().size())
                .sum();
        int numberOfCategoryTransactions = repository.getCategories().stream()
                .mapToInt(a -> a.listTransactions().size())
                .sum();

        assertThat(numberOfBankTransactions + numberOfCategoryTransactions).isEqualTo(5829 * 2);
    }

    private static void dumpStructure(Repository repository) throws Exception {
        Path file = Paths.get("/Users/bellingard/Repos/_PERSO_/_resources_/some-tests/Comptes.json");
        if (file.toFile().exists()) {
            throw new Exception("File already exists, won't dump JSON content into it.");
        }

        String jsonContent = JSONExporter.export(repository);
        Files.write(file, jsonContent.getBytes());

        //System.out.println("");
        //System.out.println("");
        //print(jsonContent);
    }

    private static void dumpBankAccountBalance(Repository repository) {
        System.out.println("");
        System.out.println("");
        repository.getBankAccounts().stream()
                .forEach(a -> print(a.getId() + " / " + a.getName() + " => " + (a.getBalance().floatValue() / 100)));
    }

    private static void print(Object a) {
        System.out.println(a);
    }
}