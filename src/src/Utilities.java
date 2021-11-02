package src;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Utilities {

    public static String getDateFormatString() {
        return "dd'/'M'/'yyyy";
    }

    public static LocalDate toLocalDate(String date) {
        return LocalDate.parse(date, Utilities.getDateFormatter());
    }

    public static DateTimeFormatter getDateFormatter() {
        return DateTimeFormatter.ofPattern(Utilities.getDateFormatString());
    }

    public static String inputString(String require) {
        if (require != null) {
            System.out.print(require + " : ");
        }
        String r = new Scanner(System.in).nextLine();
        if (r.equals("")) {
            return "";
        } else return r;
    }

    public static String toStringDate(LocalDate date) {
        return date.format(Utilities.getDateFormatter());
    }

    public static String inputFilledString(String require) {
        while (true) {
            try {
                String t = Utilities.inputString(require).trim();
                if (t.equals("")) {
                    throw new NullPointerException("Input string must not be blank");
                }
                return t;
            } catch (NullPointerException e) {
                System.out.println(e.getMessage() + "! Please try again");
            }

        }
    }

    public static boolean inputSelection(String question) {
        while (true) {
            try {
                String selection = Utilities.inputFilledString(question + "? [y/n]");
                if (selection.equalsIgnoreCase("y")) {
                    return true;
                } else if (selection.equalsIgnoreCase("n")) {
                    return false;
                } else {
                    throw new Exception("Selection must be 'Y' or 'N'! Please try again");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
    }

    public static LocalDate inputDate(String require) {
        while (true) {
            String date = Utilities.inputFilledString(require + " (dd/mm/yyyy)");
            try {
                String[] parts = date.split("/");

                return LocalDate.of(Integer.valueOf(parts[2]),
                                    Integer.valueOf(parts[1]),
                                    Integer.valueOf(parts[0]));
            } catch (Exception ex) {
                System.out.println("Date must be true format! Please try again");
            }
        }
    }

    public static boolean isWeekLarger(LocalDate d1, LocalDate d2, int week) {
        return d1.isAfter(d2.plusWeeks(week));
    }

    public static boolean isWeekSmaller(LocalDate d1, LocalDate d2, int week) {
        return d1.isBefore(d2.plusWeeks(week));
    }

    public static String md5Encode(String txt) {
        try {
            return String.format("%-256x",
                new BigInteger(
                    MessageDigest.getInstance("MD5")
                                 .digest(txt.getBytes())
                )
            ).toUpperCase().trim();
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
