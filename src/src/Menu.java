package src;

import src.controllers.VaccineController;
import src.retarded.InjectionService;
import src.retarded.StudentService;
import src.retarded.VaccineService;
import src.controllers.InjectionController;
import src.controllers.StudentController;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;


public class Menu {

    private String[] options;
    private String title;

    private InjectionService injections = new InjectionService();
    private StudentService students = new StudentService();
    private VaccineService vaccines = new VaccineService();

    public Menu() {
        this.title = "Vaccine Management";

        this.options = new String[] {
            "Show injections",
            "Show student and vaccine list",
            "Add new injection",
            "Update injection information",
            "Delete Injection",
            "Search injection by Id of Student",
            "Search injection by Student name",
            "Store data to file",
            "Store encrypted data with MD5 method to file"
        };
    }

    private void showMenu() {
        System.out.println();
        System.out.println(title + " - @ 2021 by <SE150357 - Le Quoc Cuong>");
        System.out.println();

        for (int i = 0; i < options.length; i++) {
            System.out.printf("[%d] : %s\n", i+1, options[i]);
        }
        System.out.println("[0] : Quit");
    }

    private int getOption() {
        System.out.println();
        System.out.printf("Your selection [%d-%d]: ", 0, options.length);
        Scanner sc = new Scanner(System.in);
        int r = -1;
        String r1;
        try {
            r1 = sc.nextLine();
            if (r1.equals("")) {
                System.out.println("Selection must not be blank!!!Please try again!!!");
            } else {
                r = Integer.parseInt(r1);
            }
        } catch (Exception e) {
            r = -1;
        }
        return r;
    }

    private boolean isInvalidInput(int selection) {
        return selection > this.options.length || selection < 0;
    }

    public void selectionQuery() {
        int selection = -1;

        this.preTask();

        boolean isExceptionRised = false;
        while (selection != 0) {
            try {
                if (!isExceptionRised) {
                    this.showMenu();
                }
                selection = this.getOption();

            if (isInvalidInput(selection)) {
                System.out.println("Input 1 digit ( 0 - 5 ) only please!!!Please retry again!!!");
                isExceptionRised = true;
                continue;
            }

            if (selection != 0) {
                isExceptionRised = false;
                this.directSelections(selection);
            }
            } catch (Exception e) {
                    System.out.println("Selection must not be blank!!!Please try again!!!");
            }

            this.afterFunction();

        }

        this.postTask();

        System.out.println("Good bye.");
    }

    private void directSelections(int selection) {
        try {
            Method method = Menu.class.getMethod(("s" + selection),
                                                      (Class<?>) int.class);
            method.invoke(this, (Object) 0);
        } catch (IllegalAccessException |
                 IllegalArgumentException |
                 NoSuchMethodException |
                 SecurityException |
                 InvocationTargetException ex) {
            //if (!(ex instanceof NoSuchMethodException)) {
                ex.printStackTrace();
            //}
        }
    }

    // ========================================================================

    public void preTask() {

    }

    public void postTask() {

    }

    public void afterFunction() {
        InjectionController.getInstance().initData();
    }

    // ========================================================================

    public void s1(int pseudo) {
        this.injections.show();
    }
    public void s2(int pseudo) {
        this.vaccines.show();
        System.out.println();
        this.student.show();
    }
    public void s3(int pseudo) {
        this.injections.add();
    }
    public void s4(int pseudo) {
        this.injections.update();
    }
    public void s5(int pseudo) {
        this.injections.delete();
    }
    public void s6(int pseudo) {
        this.injections.search();
    }
    public void s7(int pseudo) {
        this.injections.searchWithName();
    }
    public void s8(int pseudo) {
        this.injections.save();
    }
    public void s9(int pseudo) {
        this.injections.encryptData();
        System.out.println("Done");
    }
}
