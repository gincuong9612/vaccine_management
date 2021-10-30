
package src.models;

import java.io.Serializable;

public class Vaccine implements Serializable {
    
    private String id;
    private String name;

    public Vaccine() {
    }

    public Vaccine(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("Vaccine: {Id: \"%s\", Name: \"%s\"}", id, name);
    }

    public boolean equals(Vaccine vaccine) {
        return (id.equals(vaccine.getId()) && name.equals(vaccine.getName()));
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    
}
