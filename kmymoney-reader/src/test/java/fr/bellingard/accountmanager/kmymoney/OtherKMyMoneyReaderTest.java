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

        dumpStructure(repository);
    }

    @Test
    public void read_file() {

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
