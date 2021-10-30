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
import src.models.Vaccine;

public class VaccineController implements Controller<Vaccine> {

    private static VaccineController instance;
    
    private List<Vaccine> vaccines;
    private File vaccineContainer;
    
    public static VaccineController getInstance() {
        try {
            if (VaccineController.instance == null) {
                VaccineController.instance = new VaccineController();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } 
        return VaccineController.instance;
    }

    private VaccineController() {
        this.vaccines = new ArrayList<>();
        this.vaccineContainer = new File("vaccine.dat");
        
        if (!vaccineContainer.exists()) {
            this.initData();
        }
        
        this.loadData();
        
    }
    
    public Vaccine get(String id) {
        for (var vaccine : vaccines) {
            if (vaccine.getId().equals(id)) {
                return vaccine;
            }
        }
        return null;
    }
    
    @Override
    public void loadData() {
       try (FileInputStream fis = new FileInputStream(vaccineContainer);
            ObjectInputStream ois = new ObjectInputStream(fis)) {
           
           int counter = 0;
           
           while (true) {
               try {
                  Vaccine t = (Vaccine) ois.readObject();
//                  System.out.println("LOAD : " + t.toString());
                  vaccines.add(t);
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
            vaccineContainer.createNewFile();
            FileOutputStream fos = new FileOutputStream(vaccineContainer);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            
            Vaccine[] t = getExampleData();
            
            for (var vaccine : t) {
                System.out.println("WRITE: " + vaccine.toString());
                oos.writeObject(vaccine);
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
    public Vaccine[] getExampleData() {
        return new Vaccine[]{
            new Vaccine("Covid-V001", "AstraZeneca"),
            new Vaccine("Covid-V002", "Sputnik V"),
            new Vaccine("Covid-V003", "Vero Cell"),
            new Vaccine("Covid-V004", "Pfizer/BioNTech"),
            new Vaccine("Covid-V005", "Moderna")
        };
    }

    @Override
    public int size() {
        return vaccines.size();
    }

    public void show() {
        System.out.println("DANH SACH VACCINES ");
        for (Vaccine s : this.vaccines) {
            System.out.println(s.getId() + " \t " + s.getName());
        }
    }
    
}