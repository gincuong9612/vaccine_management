package src.controllers;

import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import src.models.Injection;
import src.models.Student;
import src.Utilities;
import src.models.Vaccine;

public class InjectionController implements Controller<Injection> {
    
    private static InjectionController instance;
    
    private List<Injection> injections;
    private File injectionContainer;
    private File injectionEncryptedContainer;
    
    public static InjectionController getInstance() {
        try {
            if (InjectionController.instance == null) {
                InjectionController.instance = new InjectionController();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } 
        return InjectionController.instance;
    }

    private InjectionController() {
        this.injections = new ArrayList<>();
        this.injectionContainer = new File("injection.obj");
        this.injectionEncryptedContainer = new File("injection.dat");
        
        this.loadData();
    }
    
    public void addNewInjection() {
        
        while (true) {
            Student student = inputStudentAdd();
            Injection injection = new Injection();
        
            System.out.println("\t\t\tFIRST INJECTION INFORMATION");
            
            Vaccine firstVaccine = inputVaccine(false);
            String firstInjectionId = inputInjectionForAdd();
            LocalDate firstInjectionDate = Utilities.inputDate("Injected date");
            String firstLocation = Utilities.inputString("Injected location");
        
            injection.setId(firstInjectionId);
            injection.setStudent(student);
            injection.setFirstDose(firstVaccine);
            injection.setFirstDate(firstInjectionDate);
            injection.setFirstLocation(firstLocation);
            
            this.injections.add(injection);
            
            if (!Utilities.inputSelection("Do you want to add more injection")) {
                break;
            }
            
        }       
    }
    
    public void updateInjection() {

            String injectionId = inputInjectionForUpdate();
            if (injectionId.equals("")) {
                return ;
            }
            Injection injection = this.get(injectionId);
            Vaccine secondVaccine = injection.getFirstDose();
            LocalDate secondInjectionDate = this.inputInjectDate(injection.getFirstDate());
            String secondLocation = Utilities.inputString("Injected location");

            injection.setSecondDose(secondVaccine);
            injection.setSecondDate(secondInjectionDate);
            injection.setSecondLocation(secondLocation);

    }
    
    public void deleteInjection() {
        String injectionId = inputInjectionForDelete();
        if (injectionId.equals("")) {
                return ;
            }
        if (Utilities.inputSelection("Are you sure want to delete")) {
            this.injections.remove(this.get(injectionId));
        }
        System.out.println("Delete injection '" + injectionId + "' successfully");
    }
    
    public void printInjections(List<Injection> injections) {
        if (injections.isEmpty()) {
            System.out.println("There is no injection");
            return;
        }
        
        if (injections.stream().allMatch(
                i -> { 
                    return (i == null); 
                }
        )) {
            System.out.println("There is no injection");
            return;
        }
        
        System.out.println("| Injection ID |  Student ID  | Student Name |  Vaccine ID  | 1st date |        1st place        | 2nd date |           2nd place          |");
        System.out.println("|--------------|--------------|--------------|--------------|----------|-------------------------|----------|------------------------------|");
        
        //injections.forEach(System.out::println);
        
        for (Injection i : injections) {
            if (i != null)
                System.out.println(i);
        }
        
    }
    
    public void searchInjectionWithId() {

        Student student = inputStudentById(false);
        if (student == null) {
            System.out.println("Student ID is not existed");
            return;
        }
        Injection t = this.getByStudent(student.getId());
        if (t == null) {
            System.out.println("There is no injection.");
            return;
        }
        
        this.printInjections(List.of(t));
        
    }
    
    public void searchInjectionWithName() {
        List<Student> students = inputStudentByName(false);
        if (students == null) {
            System.out.println("Student with given name is not existed");
        }
        
        List<Injection> t = new ArrayList<>();
        
        
        for (var student : students) {
            t.add(this.getByStudent(student.getId()));
        }
        
        this.printInjections(t);
    }
    
    public void show() {
        this.printInjections(injections);
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
    
    private boolean isInjectable(Student student) {
        Injection t = this.getByStudent(student.getId());
        return isInjectable(t);
    }
    
    private boolean isInjectable(Injection injection) {
        if (injection == null) return true;
        return (injection.getFirstDose() == null || injection.getSecondDose() == null);
    }
    
    private Student inputStudentAdd() {
        while (true) {
            try {
                String studentId = Utilities.inputFilledString("Student ID");
                
                Student student = StudentController.getInstance().get(studentId);
                if (student != null) {
                    Injection injection = this.getByStudent(studentId);
                    if (injection == null) {
                        return student;
                    } else if (injection != null) {
                        if (injection.getFirstDose() != null) {
                            throw new Exception("This student has already injected the first dose");
                        }
                        if (injection.getSecondDose() != null) {
                            throw new Exception("This student is fully vaccinated");
                        }
                    }
                    
                }
                throw new NullPointerException("Student ID is not existed");
            } catch (Exception ex) {
                System.out.println(ex.getMessage() + "! Please try again");
            }
        }
    }
    
    private Student inputStudentUpdate() {
        while (true) {
            String studentId;
            try {
                studentId = Utilities.inputFilledString("Student ID");
                Student student = StudentController.getInstance()
                                                   .get(studentId);
                if (student != null) {
                    Injection injection = this.getByStudent(studentId);
                    if (injection != null) {
                        throw new Exception("This student is still not injected the first dose");
                    } else if (injection != null) {
                        if (injection.getFirstDose() != null) {
                            return student;
                        }
                        if (injection.getSecondDose() != null) {
                            throw new Exception("This student is fully vaccinated");
                        }
                    }
                    
                } 
                throw new NullPointerException("Student ID is not existed");
            } catch (Exception ex) {
                System.out.println(ex.getMessage() + "! Please try again");
            }
        }
    }

    private String inputInjectionForAdd() {
        while (true) {
            String injectionId;
            try {
                injectionId = Utilities.inputFilledString("Injection ID");
                Injection injection = this.get(injectionId);
                
                if (injection == null) {
                    return injectionId;
                }
                throw new Exception("Injection ID has been already existed");
            } catch (Exception e) {
                System.out.println(e.getMessage() + "! Please try again");
            } 
        }
    }
    
    private String inputInjectionForUpdate() {
        while (true) {
            String injectionId;
            try {
                injectionId = Utilities.inputFilledString("Injection ID");
                Injection injection = this.get(injectionId);
                
                if (injection == null) {
                    System.out.println("Injection ID is not existed!! Please try again");
                    return "";
                } 
                if (injection != null) {
                    if (isInjectable(injection)) {
                        return injectionId;
                    }
                    System.out.println("This student is fully vaccinated");
                    return ""; 
                }
                
            } catch (Exception e) {
                System.out.println(e.getMessage() + "! Please try again");
            } 
        }
    }
    
    private String inputInjectionForDelete() {
        while (true) {
            try {
                String injectionId = Utilities.inputFilledString("Injection ID");
                Injection injection = this.get(injectionId);
                
                if (injection == null) {
                    System.out.println("Injection ID is not existed!! Please try again");
                    return "";
                }
                
                return injectionId;
                
            } catch (Exception e) {
                System.out.println(e.getMessage() + "! Please try again");
            } 
        }
    }
    
    
    private Vaccine inputVaccine(boolean isSecondDose) {
        while (true) {
            String vaccineId;
            try {
                vaccineId = Utilities.inputFilledString("Vaccine ID");
                
                if (isSecondDose & vaccineId.equals("")) {
                    return null;
                }
                
                Vaccine vaccine = VaccineController.getInstance()
                                                   .get(vaccineId);
                if (vaccine != null) {
                    return vaccine;
                } else {
                   throw new NullPointerException("Vaccine ID is not existed");
                }
            } catch (NullPointerException e) {
                System.out.println(e.getMessage() + "! Please try again");
            }  
        }
    }
    
    private LocalDate inputInjectDate(LocalDate firstInjectDate) {
        while (true) {
            try {
                LocalDate secondInjectDate = Utilities.inputDate("Injected date");
                if (isInjectable(firstInjectDate, secondInjectDate)) {
                    return secondInjectDate;
                } else {
                    throw new Exception("The second date must be given 4 to 12 weeks after the first injection!");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage() + "! Please try again");
            }  
        }
    }

    @Override
    public Injection[] getExampleData() {
        return new Injection[] {
            new Injection("INJECTION-001", 
                          StudentController.getInstance().get("ST002"), 
                          VaccineController.getInstance().get("Covid-V001"), 
                          "Benh vien Cho Ray", 
                          Utilities.toLocalDate("01/7/2021"), 
                          VaccineController.getInstance().get("Covid-V001"), 
                          "Trung tam Y te quan Binh Thanh", 
                          Utilities.toLocalDate("20/8/2021")
            ),
            new Injection("INJECTION-002", 
                          StudentController.getInstance().get("ST001"), 
                          VaccineController.getInstance().get("Covid-V002"), 
                          "Benh vien Thu Duc", 
                          Utilities.toLocalDate("03/7/2021"), 
                          VaccineController.getInstance().get("Covid-V002"), 
                          "Benh Vien Thu Duc", 
                          Utilities.toLocalDate("15/8/2021")
            ),
            new Injection("INJECTION-003", 
                          StudentController.getInstance().get("ST003"), 
                          VaccineController.getInstance().get("Covid-V002"), 
                          "Benh vien Thu Duc", 
                          Utilities.toLocalDate("03/7/2021"), 
                          null, 
                          null, 
                          null
            )
        };
    }

    @Override
    public void loadData() {
        if (!injectionContainer.exists()) {
            try {
                injectionContainer.createNewFile();
                System.out.println("A new storage file created");
                this.injections.addAll(Arrays.asList(this.getExampleData()));
                return;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        try (FileInputStream fis = new FileInputStream(injectionContainer);
            ObjectInputStream ois = new ObjectInputStream(fis)) {
           
           int counter = 0;
           
           while (true) {
               try {
                  Injection t = (Injection) ois.readObject();
//                  System.out.println("LOAD : " + t.toString());
                  injections.add(t);
                  counter += 1;
               } catch (EOFException eofEx) {
                   break;
               }
           }
           
           System.out.println("Data loaded successfully with " + counter + " object(s).");
           
       } catch (EOFException ex) {
            System.out.println("Empty storage file. Nothing to read.");
       } catch (IOException | ClassNotFoundException ex) {
           ex.printStackTrace();
       } 
    }

    @Override
    public void initData() {
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
    
    @Override
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

    @Override
    public int size() {
        return this.injections.size();
    }

    private Student inputStudentById(boolean loopToDeath) {
        while (true) {
            try {
                String studentId = Utilities.inputFilledString("Student ID");
                
                Student student = StudentController.getInstance().get(studentId);
                if (student != null) {
                    return student;
                }
                if (loopToDeath) {
                    throw new NullPointerException("Student ID is not existed");
                } else {
                    return null;
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage() + "! Please try again");
            }
        }
    }
    
    private List<Student> inputStudentByName(boolean loopToDeath) {
        while (true) {
            try {
                String studentName = Utilities.inputFilledString("Student Name");
                
                List<Student> students = StudentController.getInstance()
                                                          .getByName(studentName);
                
                if (!students.isEmpty()) {
                    return students;
                }
                if (loopToDeath) {
                    throw new NullPointerException("Student with input name is not existed");
                } else {
                    return null;
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage() + "! Please try again");
            }
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
    
}
