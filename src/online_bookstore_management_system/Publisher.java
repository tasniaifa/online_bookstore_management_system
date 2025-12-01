package online_bookstore_management_system;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Publisher {

    private String name;
    private String address;
    private List<Book> published = new ArrayList<>();

    // revenue removed (SRP)
    // analytics removed (SRP)
    // inventory updates removed (SRP)

    public Publisher(String name, String address) {
        this.name = name;
        this.address = address;
    }

    // Keep input inside class
    public static Publisher readFromInput(Scanner sc) {
        System.out.print("Publisher name: ");
        String name = sc.nextLine();
        System.out.print("Publisher address: ");
        String address = sc.nextLine();
        return new Publisher(name, address);
    }

    // SRP version of publishing a book
    public void publishBook(Book b) {
        // Only log that the publisher is associated with the book
        published.add(b);

        // No inventory changing
        // No revenue calculation
        // No direct Analytics.record()
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    // Expose list safely
    public List<Book> getPublishedBooks() {
        return List.copyOf(published);
    }
}
