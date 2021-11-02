package src.retarded;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import src.Utilities;
import src.models.Injection;
import src.models.Student;
import src.models.Vaccine;

public class InjectionService {

    private List<Injection> injections = new ArrayList<>();
    private File injectionContainer = new File("injection.obj");
    private File injectionEncryptedContainer = new File("injection.dat");

    private StudentService students = new StudentService();
    private VaccineService vaccines = new VaccineService();

    public void add() {
        while (true) {
            Student student = null;
            Injection injection;
            Vaccine vaccine = null;
            // get student from input
            while (true) {

                student = students.getFromInput(true);

                injection = this.getByStudent(student.getId());
                if (injection == null) {
                    injection = new Injection();
                    break;
                }
                System.out.println("This student has already injected");

            }

            System.out.println("FIRST INJECTION INFORMATION");

            vaccine = vaccines.getFromInput(true);

            String id;
            while (true) {
                id = Utilities.inputFilledString("Injection ID");
                injection = this.get(id);
                if (injection == null) {
                    injection = new Injection();
                    break;
                }
                System.out.println("Injection ID has been already existed");
            }

            LocalDate firstInjectionDate = Utilities.inputDate("Injected date");
            String firstLocation = Utilities.inputString("Injected location");

            injection.setId(id);
            injection.setStudent(student);
            injection.setFirstDose(vaccine);
            injection.setFirstDate(firstInjectionDate);
            injection.setFirstLocation(firstLocation);

            this.injections.add(injection);

            if (!Utilities.inputSelection("Do you want to add more injection")) {
                break;
            }
        }
    }

    public Injection get(String id) {
        for (var injection : this.injections) {
            if (injection.getId().equals(id)) {
                return injection;
            }
        }
        return null;
    }

    public Injection getByStudent(String studentId) {
        for (Injection injection : injections) {
            if (injection.getStudent().getId().equals(studentId)) {
                return injection;
            }
        }
        return null;
    }

    public void update() {

        Injection injection;
        LocalDate secondInjectionDate;

        while (true) {
            String id = Utilities.inputFilledString("Injection ID");
            injection = this.get(id);

            if (injection == null) {
                System.out.println("Injection ID is not existed!! Please try again");
                return;
            }
            if (injection != null) {
                if (this.isInjectable(injection)) {
                    break;
                }
                System.out.println("This student is fully vaccinated");
                return;
            }
        }

        while (true) {
            secondInjectionDate = Utilities.inputDate("Injected date");
            if (isInjectable(injection.getFirstDate(), secondInjectionDate)) {
                break;
            }
            System.out.println("The second date must be given 4 to 12 weeks after the first injection!");
        }

        String secondLocation = Utilities.inputString("Injected location");

        injection.setSecondDose(injection.getFirstDose());
        injection.setSecondDate(secondInjectionDate);
        injection.setSecondLocation(secondLocation);
    }

    private boolean isInjectable(LocalDate firstInjectDate,
                                 LocalDate secondInjectDate) {
        return Utilities.isWeekLarger(secondInjectDate,
                                      firstInjectDate,
                                      4)
            && Utilities.isWeekSmaller(secondInjectDate,
                                       firstInjectDate,
                                       12);
    }

    private boolean isInjectable(Injection injection) {
        if (injection == null) return true;
        return (injection.getFirstDose() == null || injection.getSecondDose() == null);
    }

    public void delete() {
    }

    public void search() {
    }

    public void searchWithName() {
    }

    public void save() {
    }

    public void encryptData() {
    }

}
