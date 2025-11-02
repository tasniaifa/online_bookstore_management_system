package online_bookstore_management_system;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
//Class name Admin

// ---------------------------
// Order Reporting (SRP)
// ---------------------------
public class Admin {
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

    public void runFullSiteReport(List<Book> allBooks) {
        double totalRevenue = 0;
        for (Book b : allBooks) {
            totalRevenue += b.getPrice() * b.getStock();
        }
        System.out.println("Total revenue estimate: " + totalRevenue);
        System.out.println("Total books: " + allBooks.size());
    }
}

// ---------------------------
// Payment Service (SRP, OCP)
// ---------------------------
interface PaymentService {
    void refund(Payment payment);
}

class BkashPaymentService implements PaymentService {
    @Override
    public void refund(Payment payment) {
        if (payment.isSuccess()) {
            System.out.println("Refunding " + payment.getTransactionRef());
        }
    }
}

// ---------------------------
// Backup Service (SRP)
// ---------------------------
class BackupService {
    public void backup(String path) {
        try (FileWriter fw = new FileWriter(path)) {
            fw.write("Backup at " + new Date());
        } catch (IOException e) {
            System.err.println("Backup failed: " + e.getMessage());
        }
    }
}

// ---------------------------
// Discount Management (SRP)
// ---------------------------
class DiscountService {
    public void createDiscount(String code, int pct) {
        Discount d = new Discount(code, pct);
        System.out.println("Created discount " + d.getCode() + " for campaign");
    }
}

// ---------------------------
// Book Management (SRP)
// ---------------------------
class BookService {
    public void deactivate(Book book) {
        book.archive();
        System.out.println("Book archived: " + book.getTitle());
    }
}
