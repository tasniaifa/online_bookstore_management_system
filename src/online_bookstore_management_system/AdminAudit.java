package online_bookstore_management_system;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
/**
 *
 * @author tasniafarinifa
 */
public class AdminAudit {
    public static void log(String message) {
        try (FileWriter fw = new FileWriter("admin_audit.log", true)) { //record/log a message
            fw.write(new Date() + " - " + message + ""); // handle file writing
        } catch (IOException e) { /* ignore */ }      
    }
}
