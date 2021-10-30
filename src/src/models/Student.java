
package src.models;

import java.io.Serializable;


public class Student implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String name;

    public Student() { }

    public Student(String id, String name) {
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
        return String.format("Student: {Id: \"%s\", Name: \"%s\"}", id, name);
    }

    public boolean equals(Student student) {
        return (id.equals(student.getId()) && name.equals(student.getName()));
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
