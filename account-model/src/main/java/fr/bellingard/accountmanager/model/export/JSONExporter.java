package fr.bellingard.accountmanager.model.export;

import fr.bellingard.accountmanager.model.Account;
import fr.bellingard.accountmanager.model.Element;
import fr.bellingard.accountmanager.model.Repository;
import fr.bellingard.accountmanager.model.Transaction;

import java.util.HashSet;
import java.util.Set;

/**
 * Exports a Repository into a JSON representation
 */
public class JSONExporter {

    private static Set<Account> foundCategories = new HashSet<>();

    private JSONExporter() {
        // private constructor for utility class
    }

    /**
     * Returns a JSON string representing the repository
     *
     * @param repository the repository to serialize
     * @return the JSON representation
     */
    public static String fullExport(Repository repository) {

        return export(repository, true);
    }

    /**
     * Returns a JSON string representing the repository, but does not lists the
     * transactions under each bank account
     *
     * @param repository the repository to serialize
     * @return the JSON representation
     */
    public static String lightExport(Repository repository) {

        return export(repository, false);
    }

    /**
     * Returns a JSON string representing the repository
     *
     * @param repository the repository to serialize
     * @return the JSON representation
     */
    private static String export(Repository repository, boolean withTransactionListing) {
        extractKnownCategories(repository);

        StringBuilder json = new StringBuilder();

        json.append("{");

        // Institutions
        appendName(json, "institutions").append(":{");
        repository.getInstitutions().stream().forEach(i -> appendInstitutionOrPayee(json, i));
        removeLastComma(json);
        json.append("},");

        // Payees
        appendName(json, "payees").append(":{");
        repository.getPayees().stream().forEach(p -> appendInstitutionOrPayee(json, p));
        removeLastComma(json);
        json.append("},");

        // Bank Accounts
        appendName(json, "bankAccounts").append(":{");
        repository.getBankAccounts().stream().forEach(a -> appendAccount(json, a, true, withTransactionListing));
        removeLastComma(json);
        json.append("},");

        // Categories
        appendName(json, "categories").append(":{");
        repository.getCategories().stream().forEach(a -> appendAccount(json, a, false, withTransactionListing));
        removeLastComma(json);
        json.append("},");

        // Remains transactions
        appendName(json, "transactions").append(":{");
        repository.getBankAccounts().stream()
                .flatMap(a -> a.listTransactions().stream())
                .distinct()
                .forEach(t -> appendTransaction(json, t));
        removeLastComma(json);
        json.append("}");

        // end
        json.append("}");

        return json.toString();
    }

    private static void extractKnownCategories(Repository repository) {
        repository.getBankAccounts().stream()
                .flatMap(a -> a.listTransactions().stream())
                .distinct()
                .forEach(t -> referenceCategories(t.getFromAccount(), t.getToAccount()));
    }

    private static void referenceCategories(Account fromAccount, Account toAccount) {
        foundCategories.add(fromAccount);
        foundCategories.add(toAccount);
    }

    private static void appendInstitutionOrPayee(StringBuilder json, Element element) {
        appendId(json, element.getId()).append(":{");
        appendName(json, "id").append(":");
        appendId(json, element.getId()).append(",");
        appendName(json, "name").append(":");
        appendName(json, element.getName()).append("},");
    }

    private static void appendAccount(StringBuilder json, Account account, boolean isBankAccount, boolean withTransactionListing) {
        if (account.listSubAccount().isEmpty() && !foundCategories.contains(account)) {
            // Following account not found in any transaction - ignoring it in the export
            return;
        }

        appendId(json, account.getId()).append(":{");
        appendName(json, "id").append(":");
        appendId(json, account.getId()).append(",");
        appendName(json, "name").append(":");
        appendName(json, account.getName()).append(",");
        if (isBankAccount) {
            appendName(json, "closed").append(":").append(Boolean.toString(account.isClosed())).append(",");
            appendName(json, "favorite").append(":").append(Boolean.toString(account.isFavorite())).append(",");
        }
        account.getAccountNumber().ifPresent(n -> {
            appendName(json, "accountNumber").append(":");
            appendName(json, n).append(",");
        });
        account.getInstitution().ifPresent(i -> {
            appendName(json, "institutionId").append(":");
            appendId(json, i.getId()).append(",");
        });
        account.getParent().ifPresent(p -> {
            appendName(json, "parentId").append(":");
            appendId(json, p.getId()).append(",");
        });
        if (withTransactionListing) {
            account.listTransactions().stream()
                    .map(t -> removeZeros(t.getId()))
                    .reduce((a, b) -> a + "," + b)
                    .ifPresent(list -> {
                        appendName(json, "transactionIds").append(":[");
                        json.append(list).append("],");
                    });
        }
        account.listSubAccount().stream()
                .map(a -> removeZeros(a.getId()))
                .reduce((a, b) -> a + "," + b)
                .ifPresent(list -> {
                    appendName(json, "subAccountIds").append(":[");
                    json.append(list).append("]");
                });

        // the end, let's remove the last comma if it is there
        removeLastComma(json);
        json.append("},");
    }

    private static void appendTransaction(StringBuilder json, Transaction transaction) {
        appendId(json, transaction.getId()).append(":{");
        appendName(json, "id").append(":");
        appendId(json, transaction.getId()).append(",");
        appendName(json, "date").append(":");
        appendName(json, transaction.getDate()).append(",");
        appendName(json, "amount").append(":").append(transaction.getAmount()).append(",");
        appendName(json, "toId").append(":");
        appendId(json, transaction.getToAccount().getId()).append(",");
        appendName(json, "fromId").append(":");
        appendId(json, transaction.getFromAccount().getId()).append(",");
        transaction.getPayee().ifPresent(p -> {
            appendName(json, "payeeId").append(":");
            appendId(json, p.getId()).append(",");
        });
        appendName(json, "desc").append(":");
        appendName(json, transaction.getDescription().orElse("").replace("\n", " - "));
        json.append("},");
    }

    private static void removeLastComma(StringBuilder json) {
        if (json.charAt(json.length() - 1) == ',') {
            json.deleteCharAt(json.length() - 1);
        }
    }

    private static StringBuilder appendName(StringBuilder builder, String s) {
        return builder.append("\"").append(s).append("\"");
    }

    private static StringBuilder appendId(StringBuilder builder, String s) {
        builder.append("\"");
        // let's remove all the zeros between the first character and the real count
        builder.append(s.charAt(0));
        char[] chars = s.toCharArray();
        boolean nonZeroFound = false;
        for (int i = 1; i < chars.length; i++) {
            char c = chars[i];
            if (c != '0') {
                nonZeroFound = true;
            }
            if (nonZeroFound) {
                builder.append(c);
            }
        }
        return builder.append("\"");
    }

    private static String removeZeros(String s) {
        StringBuilder builder = new StringBuilder();
        return appendId(builder, s).toString();
    }

}
