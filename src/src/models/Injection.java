
package src.models;

import java.io.Serializable;
import java.time.LocalDate;
import src.Utilities;

public class Injection implements Serializable {
    
    private String id;
    private Student student;
    private Vaccine firstDose;
    private String firstLocation;
    private LocalDate firstDate;
    private Vaccine secondDose;
    private String secondLocation;
    private LocalDate secondDate;


    public Injection() {
    }

    public Injection(String id, Student student, Vaccine firstDose, String firstLocation, LocalDate firstDate, Vaccine secondDose, String secondLocation, LocalDate secondDate) {
        this.id = id;
        this.student = student;
        this.firstDose = firstDose;
        this.firstLocation = firstLocation;
        this.firstDate = firstDate;
        this.secondDose = secondDose;
        this.secondLocation = secondLocation;
        this.secondDate = secondDate;
    }

    @Override
    public String toString() {
        return String.format(
                "|%-14s|%-14s|%-14s|%-14s|%-10s|%-25s|%-10s|%-34s|", 
                this.id, 
                this.getStudent().getId(), 
                this.getStudent().getName(),
                this.getFirstDose().getId(), 
                this.getFormattedDate(this.firstDate), 
                this.getFirstLocation(),
                this.getFormattedDate(this.secondDate), 
                this.getSecondLocation()
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Vaccine getFirstDose() {
        return firstDose;
    }

    public void setFirstDose(Vaccine firstDose) {
        this.firstDose = firstDose;
    }

    public String getFirstLocation() {
        return firstLocation;
    }

    public void setFirstLocation(String firstLocation) {
        this.firstLocation = firstLocation;
    }

    public LocalDate getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(LocalDate firstDate) {
        this.firstDate = firstDate;
    }

    public Vaccine getSecondDose() {
        return secondDose;
    }

    public void setSecondDose(Vaccine secondDose) {
        this.secondDose = secondDose;
    }

    public String getSecondLocation() {
        return secondLocation;
    }

    public void setSecondLocation(String secondLocation) {
        this.secondLocation = secondLocation;
    }

    public LocalDate getSecondDate() {
        return secondDate;
    }

    public void setSecondDate(LocalDate secondDate) {
        this.secondDate = secondDate;
    }

    public String getFormattedDate(LocalDate date) {
        if (date == null) {
            return "null";
        }
        return Utilities.toStringDate(date);
    }
    
}
