package online_bookstore_management_system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * REFACTORED PUBLISHER CLASS
 * --------------------------
 * ✔ SRP: Publisher now ONLY represents publisher information + published books
 * ✔ OCP: Publishing now uses an external service instead of modifying logic
 * here
 * ✔ DIP: Depends on interfaces (AnalyticsService, InventoryService,
 * RevenueService)
 */
public class Publisher {

    private String name;
    private String address;
    private List<Book> published = new ArrayList<>();

    public Publisher(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public static Publisher readFromInput(Scanner sc) {
        System.out.print("Publisher name: ");
        String name = sc.nextLine();
        System.out.print("Publisher address: ");
        String address = sc.nextLine();
        return new Publisher(name, address);
    }

    /**
     * Publisher now only records the book as published.
     * All other responsibilities are moved to services (SRP).
     */
    public void publishBook(
            Book book,
            InventoryService inventoryService,
            RevenueService revenueService,
            AnalyticsService analyticsService) {
        published.add(book); // SRP — only maintain list

        inventoryService.addInitialStock(book, 20); // moved responsibility
        revenueService.registerRevenue(book.getPrice() * 20); // moved responsibility
        analyticsService.recordEvent("book_published", // moved responsibility
                Map.of("publisher", name, "title", book.getTitle()));
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public List<Book> getPublishedBooks() {
        return Collections.unmodifiableList(published);
    }
}

/*
 * ============================================================
 * INVENTORY MANAGEMENT (SRP + DIP)
 * The Publisher depends on abstraction, not a concrete class.
 * ============================================================
 */
interface InventoryService {
    void addInitialStock(Book book, int qty);
}

class DefaultInventoryService implements InventoryService {
    @Override
    public void addInitialStock(Book book, int qty) {
        book.increaseStock(qty);
        System.out.println("[Inventory] Added " + qty + " copies of " + book.getTitle());
    }
}

/*
 * ============================================================
 * REVENUE TRACKING (SRP)
 * Publisher no longer manages money.
 * ============================================================
 */
interface RevenueService {
    void registerRevenue(double amount);
}

class SimpleRevenueService implements RevenueService {
    private double totalRevenue = 0;

    @Override
    public void registerRevenue(double amount) {
        totalRevenue += amount;
        System.out.println("[Revenue] Added " + amount + "; Total = " + totalRevenue);
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }
}

/*
 * ============================================================
 * ANALYTICS (SRP + DIP)
 * Publisher no longer directly calls Analytics.record()
 * ============================================================
 */
interface AnalyticsService {
    void recordEvent(String event, Map<String, Object> data);
}

class DefaultAnalyticsService implements AnalyticsService {
    @Override
    public void recordEvent(String event, Map<String, Object> data) {
        Analytics.record(event, data);
    }
}
