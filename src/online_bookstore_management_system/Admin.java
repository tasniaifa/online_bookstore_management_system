package online_bookstore_management_system;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Admin {
    // Monster class: reporting, maintenance, user mgmt, order processing, payment refunds, backups, UI printing
    public void printOrderSummary(Order order) {
        System.out.println("=== ORDER SUMMARY ===");
        System.out.println("Order: " + order.getId());
        System.out.println("Customer: " + order.getCustomer().getEmail());
        System.out.println("Amount: " + order.getTotalAmount());
        System.out.println("Items:");
        for (OrderItem it : order.getItems()) {
            System.out.println(" - " + it.getBook().getTitle() + " x " + it.getQuantity() + " = " + it.getSubtotal());
        }
    }

    public void refundPayment(Payment payment) {
        if (payment.isSuccess()) {
            System.out.println("Refunding " + payment.getTransactionRef());
        }
    }

    public void backupData(String path) {
        try (FileWriter fw = new FileWriter(path)) {
            fw.write("backup at " + new Date());
        } catch (IOException e) {
            System.err.println("Backup failed: " + e.getMessage());
        }
    }

    public void createDiscountForCampaign(String code, int pct) {
        Discount d = new Discount(code, pct);
        System.out.println("Created discount " + d.getCode() + " for campaign");
    }

    public void deactivateBookPermanently(Book b) {
        b.archive();
        System.out.println("Book archived: " + b.getTitle());
    }

    public void runFullSiteReport() {
        double totalRevenue = 0;
        int totalOrders = 0;
        for (Book b : Book.allBooks()) {
            totalRevenue += b.getPrice() * b.getStock();
        }
        System.out.println("Total revenue estimate: " + totalRevenue);
        System.out.println("Total books: " + Book.allBooks().size());
    }
}
