package bloodbank.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class DateUtil {
    private static final DateTimeFormatter FORMAT=DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private DateUtil() {}
    public static String format(LocalDate date){return date==null?"-":date.format(FORMAT);}
    public static LocalDate parse(String value){return LocalDate.parse(value,FORMAT);}
}
