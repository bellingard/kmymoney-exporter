package fr.bellingard.accountmanager.kmymoney;

import fr.bellingard.accountmanager.model.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.zip.GZIPInputStream;

/**
 * Allows to read a ".kmy" file and load information into a Repository.
 */
public class KMyMoneyReader {

    private Path kmyFile;

    private KMyMoneyReader() {
    }

    /**
     * Creates a reader from the given ".kmy" file.
     *
     * @param kmyFile the ".kmy" file
     * @return the reader
     */
    public static KMyMoneyReader on(Path kmyFile) {
        KMyMoneyReader reader = new KMyMoneyReader();
        reader.kmyFile = kmyFile;
        return reader;
    }

    /**
     * Loads information found in the file into the given Repository.
     *
     * @param repository the repository
     * @throws ReaderException
     */
    public void populate(Repository repository) throws ReaderException {
        try (GZIPInputStream input = new GZIPInputStream(new FileInputStream(kmyFile.toFile()))) {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setNamespaceAware(true);
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            Document xmlDoc = builder.parse(input);

            loadInstitutions(repository, xmlDoc);
            loadPayees(repository, xmlDoc);
            loadAccounts(repository, xmlDoc);
            loadTransactions(repository, xmlDoc);
        } catch (IOException e) {
            throw new ReaderException("Could not read KMyMoneyFile: " + kmyFile, e);
        } catch (ParserConfigurationException | SAXException | XPathExpressionException e) {
            throw new ReaderException("Could not parse KMyMoneyFile: " + kmyFile, e);
        }
    }

    private void loadInstitutions(Repository repository, Document xmlDoc) throws XPathExpressionException {
        NodeList nodes = getXPathResults(xmlDoc, "//INSTITUTIONS/INSTITUTION");
        Element institutionElement;
        for (int i = 0; i < nodes.getLength(); i++) {
            institutionElement = (Element) nodes.item(i);

            String id = institutionElement.getAttribute("id");
            String name = institutionElement.getAttribute("name").trim();
            repository.addInstitution(new Institution(id, name));
        }
    }

    private void loadPayees(Repository repository, Document xmlDoc) throws XPathExpressionException {
        NodeList nodes = getXPathResults(xmlDoc, "//PAYEES/PAYEE");
        Element payeeElement;
        for (int i = 0; i < nodes.getLength(); i++) {
            payeeElement = (Element) nodes.item(i);

            String id = payeeElement.getAttribute("id");
            String name = payeeElement.getAttribute("name").trim();
            repository.addPayee(new Payee(id, name));
        }
    }

    private void loadAccounts(Repository repository, Document xmlDoc) throws XPathExpressionException {
        Map<String, Account> accounts = new HashMap<>();
        NodeList nodes = getXPathResults(xmlDoc, "//ACCOUNTS/ACCOUNT");
        Element accountElement;
        // First create all accounts
        for (int i = 0; i < nodes.getLength(); i++) {
            accountElement = (Element) nodes.item(i);

            String id = accountElement.getAttribute("id");
            String name = accountElement.getAttribute("name").trim();
            Account account = new Account(id, name);
            accounts.put(id, account);

            String institutionId = accountElement.getAttribute("institution");
            Optional<Institution> institution = repository.findInstitution(institutionId);
            if (institution.isPresent()) {
                // this is a bank account
                repository.addBankAccount(account);
                account.setInstitution(institution.get());
                account.setAccountNumber(accountElement.getAttribute("number").trim());
            } else {
                // this is a category
                repository.addCategory(account);
            }
        }
        // Then create hierarchy of existing accounts
        for (int i = 0; i < nodes.getLength(); i++) {
            accountElement = (Element) nodes.item(i);
            Account account = accounts.get(accountElement.getAttribute("id"));
            Account parent = accounts.get(accountElement.getAttribute("parentaccount"));
            if (parent != null) {
                account.setParent(parent);
            }
        }
    }

    private void loadTransactions(Repository repository, Document xmlDoc) throws XPathExpressionException {
        NodeList nodes = getXPathResults(xmlDoc, "//TRANSACTIONS/TRANSACTION");
        Element transactionElement;
        // First create all accounts
        for (int i = 0; i < nodes.getLength(); i++) {
            transactionElement = (Element) nodes.item(i);

            String id = transactionElement.getAttribute("id");
            String date = transactionElement.getAttribute("postdate");
            Account toAccount;
            Account fromAccount;
            Long amount;
            Payee payee;
            String description;

            Element[] splits = extractSplits(transactionElement);

            Element firstElement = splits[0];
            payee = repository.findPayee(firstElement.getAttribute("payee")).orElse(null);
            toAccount = repository.findBankAccount(firstElement.getAttribute("account")).orElse(null);
            amount = convert(firstElement.getAttribute("shares"));
            description = firstElement.getAttribute("memo").trim();

            Element secondElement = splits[1];
            fromAccount = repository.findCategory(secondElement.getAttribute("account")).orElse(null);
            if (fromAccount == null) {
                // this is a transfer between 2 back accounts
                fromAccount = repository.findBankAccount(secondElement.getAttribute("account")).orElse(null);
            }

            Transaction transaction = new Transaction(id, toAccount, fromAccount, date, amount);
            transaction.setPayee(payee);
            transaction.setDescription(description);
        }
    }

    private Element[] extractSplits(Element transactionElement) {
        // There are only 2 splits
        Element[] splits = new Element[2];
        int index = 0;
        NodeList splitsChildren = transactionElement.getChildNodes().item(1).getChildNodes();
        for (int j = 0; j < splitsChildren.getLength(); j++) {
            Node current = splitsChildren.item(j);
            if ("SPLIT".equals(current.getNodeName())) {
                splits[index++] = (Element) current;
            }
        }
        return splits;
    }

    private Long convert(String value) {
        String[] data = value.split("/");
        long numerator = Long.parseLong(data[0]);
        long denominator = Long.parseLong(data[1]);
        return numerator * 100 / denominator;
    }

    private NodeList getXPathResults(final Node node, final String query)
            throws XPathExpressionException {
        final XPathFactory factory = XPathFactory.newInstance();
        final XPath xpath = factory.newXPath();
        final XPathExpression expr = xpath.compile(query);
        return (NodeList) expr.evaluate(node, XPathConstants.NODESET);
    }

}
