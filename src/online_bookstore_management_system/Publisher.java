package online_bookstore_management_system;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Publisher {

    private String name;
    private String address;
    private List<Book> published = new ArrayList<>();

    // Injected interfaces (DIP)
    private final PublisherFinance finance;
    private final PublisherAnalytics analytics;
    private final InventoryManager inventory;

    // Constructor with dependencies injected (DIP)
    public Publisher(String name, String address,
            PublisherFinance finance,
            PublisherAnalytics analytics,
            InventoryManager inventory) {

        this.name = name;
        this.address = address;
        this.finance = finance;
        this.analytics = analytics;
        this.inventory = inventory;
    }

    // Keep input inside class (your requirement)
    public static Publisher readFromInput(
            Scanner sc,
            PublisherFinance finance,
            PublisherAnalytics analytics,
            InventoryManager inventory) {

        System.out.print("Publisher name: ");
        String name = sc.nextLine();
        System.out.print("Publisher address: ");
        String address = sc.nextLine();

        return new Publisher(name, address, finance, analytics, inventory);
    }

    public void publishBook(Book b) {
        published.add(b);

        // Apply services through interfaces (DIP)
        inventory.addInitialStock(b, 20);
        finance.recordInitialStockRevenue(b, 20);
        analytics.recordBookPublished(this, b);
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public List<Book> getPublishedBooks() {
        return List.copyOf(published);
    }
}
