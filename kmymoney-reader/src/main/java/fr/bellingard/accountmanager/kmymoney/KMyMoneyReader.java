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
import java.util.*;
import java.util.zip.GZIPInputStream;

/**
 *
 */
public class KMyMoneyReader {

    private Path kmyFile;

    private KMyMoneyReader() {
    }

    public static KMyMoneyReader on(Path kmyFile) {
        KMyMoneyReader reader = new KMyMoneyReader();
        reader.kmyFile = kmyFile;
        return reader;
    }

    public void populate(Repository repository) {
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
            // TODO pbm with file -  do something
            e.printStackTrace();
        } catch (ParserConfigurationException | SAXException | XPathExpressionException e) {
            // TODO pbm with content of the file -  do something
            e.printStackTrace();
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
            Payee payee = null;
            Account bankAccount = null;
            Account category = null;
            Float amount = null;
            String description = null;

            Collection<Element> splits = extractSplits(transactionElement);
            for (Element splitElement : splits) {
                payee = repository.findPayee(splitElement.getAttribute("payee")).orElse(null);
                bankAccount = repository.findBankAccount(splitElement.getAttribute("account")).orElse(bankAccount);
                if (amount == null && bankAccount != null) {
                    amount = convert(splitElement.getAttribute("shares"));
                }
                category = repository.findCategory(splitElement.getAttribute("account")).orElse(category);
                description = splitElement.getAttribute("memo").trim();
            }

            Transaction transaction = new Transaction(id, bankAccount, date, amount);
            transaction.setCategory(category);
            transaction.setPayee(payee);
            transaction.setDescription(description);
            System.out.println(transaction);
        }

    }

    private Collection<Element> extractSplits(Element transactionElement) {
        Collection<Element> splits = new ArrayList<>();
        NodeList splitsChildren = transactionElement.getChildNodes().item(1).getChildNodes();
        for (int j = 0; j < splitsChildren.getLength(); j++) {
            Node current = splitsChildren.item(j);
            if (current.getNodeName().equals("SPLIT")) {
                splits.add((Element) current);
            }
        }
        return splits;
    }

    private Float convert(String value) {
        String[] data = value.split("/");
        float numerator = Float.parseFloat(data[0]);
        float denominator = Float.parseFloat(data[1]);
        return new Float(numerator / denominator);
    }

    private NodeList getXPathResults(final Node node, final String query)
            throws XPathExpressionException {
        final XPathFactory factory = XPathFactory.newInstance();
        final XPath xpath = factory.newXPath();
        final XPathExpression expr = xpath.compile(query);
        return (NodeList) expr.evaluate(node, XPathConstants.NODESET);
    }

}
