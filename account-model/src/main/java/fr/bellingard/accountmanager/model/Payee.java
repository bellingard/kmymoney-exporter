package fr.bellingard.accountmanager.model;

/**
 * Represents someone or an organisation (company, administration, ...) who is the recipient
 * or the origin of a transaction.
 */
public class Payee extends Element {

    /**
     * Creates a payee referenced by its immutable ID and with the given name.
     * @param id
     * @param name
     */
    public Payee(String id, String name) {
        super(id, name);
    }

}
