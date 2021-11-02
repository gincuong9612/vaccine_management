package src.controllers;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import src.models.Student;

public class StudentController implements Controller<Student> {
    
    private static StudentController instance;
    
    private List<Student> students;
    private File studentContainer;
    
    public static StudentController getInstance() {
        try {
            if (StudentController.instance == null) {
                StudentController.instance = new StudentController();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } 
        return StudentController.instance;
    }

    private StudentController() {
        this.students = new ArrayList<>();
        this.studentContainer = new File("student.dat");
        
        if (!studentContainer.exists()) {
            this.initData();
        }
        
        this.loadData();
        
    }
    
    public Student get(String id) {
        for (var student : students) {
            if (student.getId().equals(id)) {
                return student;
            }
        }
        return null;
    }
    
    public List<Student> getByName(String name) {
        List<Student> s = new ArrayList<>();
        
        for (var student : this.students) {
            if (student.getName().toUpperCase().contains(name.toUpperCase())) {
                s.add(student);
            }
        }
        
        // trả về mảng tạm các student trùng tên
        return s;
    }
    
    @Override
    public void loadData() {
       try (FileInputStream fis = new FileInputStream(studentContainer);
            ObjectInputStream ois = new ObjectInputStream(fis)) {
           
           int counter = 0;
           
           while (true) {
               try {
                  Student t = (Student) ois.readObject();
//                  System.out.println("LOAD : " + t.toString());
                  students.add(t);
                  counter += 1;
               } catch (EOFException eofEx) {
                   break;
               }
           }
           
           System.out.println("Data loaded successfully with " + counter + " object(s).");
           
       } catch (IOException | ClassNotFoundException ex) {
           ex.printStackTrace();
       }
    }

    @Override
    public void initData() {
        try {
            studentContainer.createNewFile();
            FileOutputStream fos = new FileOutputStream(studentContainer);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            
            Student[] t = getExampleData();
            
            for (var student : t) {
                System.out.println("WRITE: " + student.toString());
                oos.writeObject(student);
            }
            
            oos.close();
            fos.close();
            
            System.out.println("Data initialization completed with " 
                + t.length + " written objects");
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Student[] getExampleData() {
        return new Student[]{
            new Student("ST002", "An"),
            new Student("ST003", "Anh"),
            new Student("ST001", "Binh"),
            new Student("ST004", "Bao"),
            new Student("ST005", "Cuong"),
            new Student("ST007", "Dung"),
            new Student("ST006", "Giang")
        };
    }

    @Override
    public int size() {
        return students.size();
    }
    
    public void show() {
        System.out.println("DANH SACH SINH VIEN ");
        for (Student s : this.students) {
            System.out.println(s.getId() + " \t " + s.getName());
        }
    } 
}
