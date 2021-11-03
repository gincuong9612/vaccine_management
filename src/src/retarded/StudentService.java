package src.retarded;

import java.util.ArrayList;
import java.util.List;

import src.Utilities;
import src.models.Student;

public class StudentService {
    private List<Student> students = new ArrayList<>();

    public StudentService() {
        this.students.addAll(List.of(this.getExampleData()));
    }

    public Student[] getExampleData() {
        return new Student[]{
            new Student("ST001", "Nguyen Hoang Phuc"),
            new Student("ST002", "Pham Quang Tuong"),
            new Student("ST003", "Pham Quoc Hung"),
            new Student("ST004", "Le Thi Ngoc Tran"),
            new Student("ST005", "Le Quoc Cuong"),
            new Student("ST006", "Tran Thuy Dung"),
            new Student("ST007", "Nguyen Tien Giang")
        };
    }

    public void show() {
        System.out.println("DANH SACH SINH VIEN ");
        for (Student s : this.students) {
            System.out.println(s.getId() + " \t " + s.getName());
        }
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

        return s;
    }

    public Student getFromInput(boolean repeat) {
        while (true) {
            String id = Utilities.inputFilledString("Student ID");
            Student student = this.get(id);

            if (student != null) {
                return student;
            }
            System.out.println("Student ID is not existed");
            if (!repeat) {
                return null;
            }
        }
    }
    
    public List<Student> getByNameFromInput(boolean repeat) {
        while (true) {
            String name = Utilities.inputFilledString("Student Name");
            List<Student> students = new ArrayList<>();

            for (var student : this.students) {
                if (student.getName().toUpperCase().contains(name.toUpperCase())) {
                    students.add(student);
                }
            }

            if (!students.isEmpty()) {
                return students;
            }
            System.out.println("Student is not existed");
            if (!repeat) {
                return null;
            }
        }
    }

}
