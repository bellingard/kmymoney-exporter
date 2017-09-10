package fr.bellingard.accountmanager.model.export;

import fr.bellingard.accountmanager.model.Account;
import fr.bellingard.accountmanager.model.Element;
import fr.bellingard.accountmanager.model.Repository;
import fr.bellingard.accountmanager.model.Transaction;

/**
 * Exports a Repository into a JSON representation
 */
public class JSONExporter {

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

    private static void appendInstitutionOrPayee(StringBuilder json, Element element) {
        appendName(json, element.getId()).append(":{");
        appendName(json, "id").append(":");
        appendName(json, element.getId()).append(",");
        appendName(json, "name").append(":");
        appendName(json, element.getName()).append("},");
    }

    private static void appendAccount(StringBuilder json, Account account, boolean isBankAccount, boolean withTransactionListing) {
        appendName(json, account.getId()).append(":{");
        appendName(json, "id").append(":");
        appendName(json, account.getId()).append(",");
        appendName(json, "name").append(":");
        appendName(json, account.getName()).append(",");
        if (isBankAccount) {
            appendName(json, "closed").append(":").append(Boolean.toString(account.isClosed())).append(",");
        }
        account.getAccountNumber().ifPresent(n -> {
            appendName(json, "accountNumber").append(":");
            appendName(json, n).append(",");
        });
        account.getInstitution().ifPresent(i -> {
            appendName(json, "institutionId").append(":");
            appendName(json, i.getId()).append(",");
        });
        account.getParent().ifPresent(p -> {
            appendName(json, "parentId").append(":");
            appendName(json, p.getId()).append(",");
        });
        if (withTransactionListing) {
            account.listTransactions().stream()
                    .map(t -> "\"" + t.getId() + "\"")
                    .reduce((a, b) -> a + "," + b)
                    .ifPresent(list -> {
                        appendName(json, "transactionIds").append(":[");
                        json.append(list).append("],");
                    });
        }
        account.listSubAccount().stream()
                .map(a -> "\"" + a.getId() + "\"")
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
        appendName(json, transaction.getId()).append(":{");
        appendName(json, "id").append(":");
        appendName(json, transaction.getId()).append(",");
        appendName(json, "date").append(":");
        appendName(json, transaction.getDate()).append(",");
        appendName(json, "amount").append(":").append(transaction.getAmount()).append(",");
        appendName(json, "toId").append(":");
        appendName(json, transaction.getToAccount().getId()).append(",");
        appendName(json, "fromId").append(":");
        appendName(json, transaction.getFromAccount().getId()).append(",");
        transaction.getPayee().ifPresent(p -> {
            appendName(json, "payeeId").append(":");
            appendName(json, p.getId()).append(",");
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

}
