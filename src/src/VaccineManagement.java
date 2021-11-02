package src;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class VaccineManagement {
    
    
    public static boolean TEST_FLAG = false;
    
    public static void main(String[] args) {
        if (TEST_FLAG) {
            System.out.println("TEST MODE \n");
            VaccineManagement.tester();
        } else {
            new Menu().selectionQuery();
        }
    }
    
    public static void tester() {
        System.out.println(new StringBuilder("Hello").hashCode());
        System.out.println("System".hashCode());
        System.out.println(new StringBuilder("Hello").hashCode());
    }
    
    public static void invoker() {
        try {
            String.format("%-64x", 
                new BigInteger(
                    MessageDigest.getInstance("MD5")
                                 .digest("daubuoirerach".getBytes())
                )
            );
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        
    }
}
