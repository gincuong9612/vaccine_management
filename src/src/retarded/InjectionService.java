package src.retarded;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
            
            vaccine = vaccines.getFromInput(true);

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
                System.out.println("Injection ID is not existed!!");
                return;
            }
            if (this.isInjectable(injection)) {
                break;
            }
            System.out.println("This student is fully vaccinated");
            return;
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
        Injection i = this.getFromInput(false);
        if (i == null) {
                return;
        }
        if (Utilities.inputSelection("Are you sure want to delete")) {
            this.injections.remove(i);
        }
        System.out.println("Delete injection '" + i.getId() + "' successfully");
    }

    public void searchWihtID() {
        Student student = students.getFromInput(false);
        if (student == null) {
            return;
        }
        Injection t = this.getByStudent(student.getId());
        if (t == null) {
            System.out.println("There is no injection.");
            return;
        }
        System.out.println("| Injection ID |  Student ID  |    Student Name    |  Vaccine ID  | 1st date |        1st place        | 2nd date |             2nd place            |");
        System.out.println("|--------------|--------------|--------------------|--------------|----------|-------------------------|----------|----------------------------------|");
        System.out.println(t);                             
    }

    public void searchWithName() {
        List<Student> list = students.getByNameFromInput(false);
        if (list == null) {
            return;
        }

        List<Injection> t = new ArrayList<>();

        for (var student : list) {
            if (student != null) {
                System.out.println(student.getId() + " " + student.getName());
            }
            t.add(this.getByStudent(student.getId()));
        }

        if (t.isEmpty()) {
            System.out.println("There is no injection");
            return;
        }

        if (injections.stream().allMatch(i -> i == null)) {
            System.out.println("There is no injection");
            return;
        }

        System.out.println("| Injection ID |  Student ID  |    Student Name    |  Vaccine ID  | 1st date |        1st place        | 2nd date |             2nd place            |");
        System.out.println("|--------------|--------------|--------------------|--------------|----------|-------------------------|----------|----------------------------------|");

        for (Injection i : injections) {
            if (i != null)
                System.out.println(i);
        }
    }

    public void save() {
        try {
            injectionContainer.createNewFile();
            FileOutputStream fos = new FileOutputStream(injectionContainer);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            for (var injection : injections) {
                oos.writeObject(injection);
            }

            oos.close();
            fos.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void encryptData() {
        try {
            injectionEncryptedContainer.createNewFile();
            FileWriter fw = new FileWriter(injectionEncryptedContainer);
            BufferedWriter bw = new BufferedWriter(fw);

            int i = 0;
            for (var injection : injections) {
                bw.write(Utilities.md5Encode(injection.toString()));
                bw.write("\n");
                System.out.println("ENCODING  (" + ++i + "/" + injections.size() + ") OK");
            }

            bw.close();
            fw.close();
            System.out.println("Encoding and saving completed");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public Injection getFromInput(boolean repeat) {
        while (true) {
            String id = Utilities.inputFilledString("Injection ID");
            Injection i = this.get(id);
            if (i != null) {
                return i;
            }
            System.out.println("Injection is not existed");
            if (!repeat) {
                return null;
            }
        }
    }

    public void show() {

        if (injections.isEmpty()) {
            System.out.println("Nothing to print");
            return;
        }
        System.out.println("| Injection ID |  Student ID  |    Student Name    |  Vaccine ID  | 1st date |        1st place        | 2nd date |             2nd place            |");
        System.out.println("|--------------|--------------|--------------------|--------------|----------|-------------------------|----------|----------------------------------|");

        for (Injection i : injections) {
            if (i != null)
                System.out.println(i);
        }
    }

}
