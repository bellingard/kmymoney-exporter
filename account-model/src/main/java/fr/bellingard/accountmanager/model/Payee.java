package fr.bellingard.accountmanager.model;

/**
 *
 */
public class Payee {

    private String id;
    private String name;

    public Payee(String id, String name) {
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

        Payee payee = (Payee) o;

        return id.equals(payee.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Payee{" +
                "id='" + id + '\'' +
                "name='" + name + '\'' +
                '}';
    }

}
