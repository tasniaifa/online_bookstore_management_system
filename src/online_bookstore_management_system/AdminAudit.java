
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
        try (FileWriter fw = new FileWriter("admin_audit.log", true)) {
            fw.write(new Date() + " - " + message + "");
        } catch (IOException e) { /* ignore */ }
    }
    
}
