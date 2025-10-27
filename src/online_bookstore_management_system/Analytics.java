/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package online_bookstore_management_system;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 *
 * @author tasniafarinifa
 */
public class Analytics {

    public static void record(String event, Map<String, Object> payload) {
        // naive print, but keeps a history file too (bad mixing)
        System.out.println("[ANALYTICS] " + event + " => " + payload);
        try (FileWriter fw = new FileWriter("analytics.log", true)) {
            fw.write(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " " + event + " " + payload + "");
        } catch (IOException e) { /* ignore */ }
    }
}

