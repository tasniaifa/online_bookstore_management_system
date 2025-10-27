/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package online_bookstore_management_system;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 *
 * @author tasniafarinifa
 */
public class Invoice {

    private String id;
    private Order order;
    private Payment payment;
    private Date issuedAt;

    public Invoice(Order order, Payment payment) {
        this.id = "INV-" + UUID.randomUUID().toString();
        this.order = order;
        this.payment = payment;
        this.issuedAt = new Date();
    }

    // mixes formatting, persistence, delivery (violates SRP)
    public void generate() {
        StringBuilder sb = new StringBuilder();
        sb.append("INVOICE");
        sb.append("ID: " + id + "");
        sb.append("Order: " + order.getId() + "");
        sb.append("Amount: " + order.getTotalAmount() + "");
        sb.append("Paid: " + (payment != null && payment.isSuccess()) + "");
        sb.append("Items:");
        for (OrderItem it : order.getItems()) {
            sb.append(" - " + it.getBook().getTitle() + " x " + it.getQuantity() + "");
        }
        sb.append("Issued: " + issuedAt + "");
        sb.append("Transaction: " + (payment != null ? payment.getTransactionRef() : "NONE") + "");

        // write to disk
        try (FileWriter fw = new FileWriter("invoices/" + id + ".txt")) {
            fw.write(sb.toString());
        } catch (IOException e) {
            // try to create dir then retry
            new File("invoices").mkdirs();
            try (FileWriter fw2 = new FileWriter("invoices/" + id + ".txt")) {
                fw2.write(sb.toString());
            } catch (IOException ex) {
                System.err.println("Failed to persist invoice: " + ex.getMessage());
            }
        }

        // send email (side effect)
        sendInvoiceToCustomer(order.getCustomer().getEmail(), sb.toString());
        // also print to console
        System.out.println("[INVOICE SENT] " + id + " amount=" + order.getTotalAmount());
    }

    private void sendInvoiceToCustomer(String email, String content) {
        // naive print instead of real email system
        System.out.println("Sending invoice to " + email + " with length " + content.length());
    }
}
    

