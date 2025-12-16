/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package online_bookstore_management_system;

import java.io.FileWriter;
import java.io.IOException;

public interface InvoiceService {
    void generateInvoice(Order order);
}

public class InvoiceServiceDetails implements InvoiceService {
    @Override
    public void generateInvoice(Order order) {
        String content = "Invoice for order " + order.getId() + " amount " + order.getTotalAmount();
        try (FileWriter fw = new FileWriter("invoices/" + order.getId() + ".txt")) {
            fw.write(content);
        } catch (IOException e) {
            System.err.println("Failed to persist invoice: " + e.getMessage());
        }
    }
}


