package fr.bellingard.accountmanager.model;

/**
 * Abstract class for many concepts
 */
public abstract class Element {

    private String id;
    private String name;

    /**
     * Creates an element referenced by its immutable ID and with the given name.
     *
     * @param id
     * @param name
     */
    public Element(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Element payee = (Element) o;

        return id.equals(payee.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id='" + id + '\'' +
                "name='" + name + '\'' +
                '}';
    }

}
