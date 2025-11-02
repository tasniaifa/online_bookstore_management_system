package online_bookstore_management_system;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

// 1️⃣ SRP + DIP — Interface for logging
interface AuditLogger {
    void log(String message);
}

// 2️⃣ Low-level implementation (File)
class FileAuditLogger implements AuditLogger {
    private final String filePath;

    public FileAuditLogger(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void log(String message) {
        try (FileWriter fw = new FileWriter(filePath, true)) {
            fw.write(new Date() + " - " + message + "\n");
        } catch (IOException e) {
            System.err.println("Log failed: " + e.getMessage());
        }
    }
}

// 3️⃣ High-level class depends on interface, not FileWriter
public class AdminAudit {
    private final AuditLogger logger;

    public AdminAudit(AuditLogger logger) {
        this.logger = logger;
    }

    public void record(String message) {
        logger.log(message);
    }
}
