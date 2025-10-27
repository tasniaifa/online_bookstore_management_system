/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package online_bookstore_management_system;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author tasniafarinifa
 */
public class Publisher {
    private String name;
    private String address;
    private List<Book> published = new ArrayList<>();

    // publisher-level financial tracking inside publisher class (bad idea)
    private double revenue;

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

    public void publishBook(Book b) {
        published.add(b);
        // do inventory update for each published book (weird coupling)
        b.increaseStock(20);
        revenue += b.getPrice() * 20; // naive revenue calc
        // directly call analytics
        Analytics.record("book_published", Map.of("publisher", name, "title", b.getTitle()));
    }

    public String getName() { return name; }
    public double getRevenue() { return revenue; }
}
    

