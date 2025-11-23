package online_bookstore_management_system;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.List;

//change in book, author and publisher

interface Logger { // event or message record kora
    void log(String message);
}

class ConsoleLogger implements Logger {
    @Override
    public void log(String message) {
        System.out.println(message);
    }
}

class FileLogger implements Logger {
    private final String path;

    public FileLogger(String path) {
        this.path = path;
    }

    @Override
    public void log(String message) {
        try (FileWriter fw = new FileWriter(path, true)) {
            fw.write(message + "\n");
        } catch (IOException e) {
            System.err.println("File write failed: " + e.getMessage());
        }
    }
}

public class Analytics {
    private static List<Logger> loggers;

    public Analytics(List<Logger> loggers) {
        this.loggers = loggers;
    }

    public static void record(String event, Map<String, Object> payload) {
        String message = "[ANALYTICS] " + event + " => " + payload;
        String timestamped = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                + " " + event + " " + payload;
        // 2025-11-02 23:40:12 book_sold {isbn=123, qty=2}

        for (Logger logger : loggers) {
            if (logger instanceof FileLogger) {
                logger.log(timestamped); // 2025-11-02 23:40:12 book_sold {isbn=123, qty=2}
            } else {
                logger.log(message); // [ANALYTICS] book_sold => {isbn=123, qty=2}
            }
        }
    }
}
