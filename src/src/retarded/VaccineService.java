package src.retarded;

import java.util.ArrayList;
import java.util.List;

import src.Utilities;
import src.models.Vaccine;

public class VaccineService {

    private List<Vaccine> vaccines = new ArrayList<>();

    public VaccineService() {

        this.vaccines.addAll(List.of(this.getExampleData()));

    }
    public Vaccine[] getExampleData() {
        return new Vaccine[]{
            new Vaccine("Covid-V001", "AstraZeneca"),
            new Vaccine("Covid-V002", "Sputnik V"),
            new Vaccine("Covid-V003", "Vero Cell"),
            new Vaccine("Covid-V004", "Pfizer/BioNTech"),
            new Vaccine("Covid-V005", "Moderna")
        };
    }
    public void show() {
        System.out.println("DANH SACH VACCINES ");
        for (Vaccine s : this.vaccines) {
            System.out.println(s.getId() + " \t " + s.getName());
        }
    }
    public Vaccine get(String id) {
        for (var vaccine : vaccines) {
            if (vaccine.getId().equals(id)) {
                return vaccine;
            }
        }
        return null;
    }
    public Vaccine getFromInput(boolean repeat) {
        while (true) {
            String id = Utilities.inputFilledString("Vaccine ID");
            Vaccine v = this.get(id);

            if (v != null) {
                return v;
            }
            System.out.println("Vaccine ID is not existed");
            if (!repeat) {
                return null;
            }
        }
    }
}
