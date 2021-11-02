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
            new Student("ST002", "An"),
            new Student("ST003", "Anh"),
            new Student("ST001", "Binh"),
            new Student("ST004", "Bao"),
            new Student("ST005", "Cuong"),
            new Student("ST007", "Dung"),
            new Student("ST006", "Giang")
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

        // trả về mảng tạm các student trùng tên
        return s;
    }

    public Student getFromInput(boolean repeat) {
        while (true) {
            String id = Utilities.inputFilledString("Student ID");
            Student student = this.get(id);

            if (student != null) {
                return student;
            }
            System.out.println("Student is not existed");
            if (!repeat) {
                return null;
            }
        }
    }

}
