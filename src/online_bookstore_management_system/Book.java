/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package online_bookstore_management_system;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

/**
 *
 * @author tasniafarinifa
 */
public class Book {
    // Mixed responsibilities: data holder + inventory manager + presentation + persistence + analytics
    private String title;
    private Author author;
    private Publisher publisher;
    private Category category;
    private double price;
    private int stock;
    private String isbn;
    private String description;
    private Date addedOn;

    // UI hint fields (should not be here in SRP world)
    private boolean featured;
    private int popularityScore;
    private String thumbnailUrl;

    // Logging fields (also violates SRP)
    private List<String> changeLog = new ArrayList<>();

    // Cross-cutting concern fields (should be extracted)
    private boolean archived;
    private Map<String, Object> meta = new HashMap<>();

    // Bad static DB-like list: global mutable state
    public static final List<Book> GLOBAL_BOOKS = new ArrayList<>();

    public Book(String title, Author author, Publisher publisher, Category category, double price, int stock) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.isbn = UUID.randomUUID().toString();
        this.description = "";
        this.addedOn = new Date();
        this.featured = false;
        this.popularityScore = 0;
        this.thumbnailUrl = "http://example.com/thumbnail/" + this.isbn;
        this.archived = false;
        GLOBAL_BOOKS.add(this);
        logChange("Created book: " + title);
    }

    // Input helper (bad: uses scanner and creates object directly)
    public static Book readFromInput(Scanner sc, Author a, Publisher p, Category c) {
        System.out.print("Book title: ");
        String title = sc.nextLine();
        System.out.print("Price: ");
        double price = Double.parseDouble(sc.nextLine());
        System.out.print("Stock: ");
        int stock = Integer.parseInt(sc.nextLine());
        Book b = new Book(title, a, p, c, price, stock);
        System.out.print("Add description? (y/n): ");
        if (sc.nextLine().trim().equalsIgnoreCase("y")) {
            System.out.println("Enter description (single line):");
            b.description = sc.nextLine();
        }
        return b;
    }

    // --- Inventory methods ---
    public boolean reduceStock(int qty) {
        if (qty <= 0) return false;
        if (stock >= qty) {
            stock -= qty;
            logChange("Stock reduced by " + qty + "; remaining=" + stock);
            // analytics event (side effect)
            Analytics.record("stock_reduced", Map.of("title", title, "qty", qty));
            return true;
        }
        return false;
    }

    public void increaseStock(int qty) {
        stock += qty;
        logChange("Stock increased by " + qty + "; new=" + stock);
        Analytics.record("stock_increased", Map.of("title", title, "qty", qty));
    }

    public int getStock() { return stock; }
    public double getPrice() { return price; }

    // --- Business calc (should be elsewhere) ---
    public double calculateDiscountedPrice(Discount discount) {
        if (discount == null) return price;
        double discounted = price - (price * discount.getPercentage() / 100.0);
        logChange("Applied discount " + discount.getCode() + " => " + discounted);
        return discounted;
    }

    // new method that applies multiple discounts and mutates state
    public double applyDiscounts(Collection<Discount> discounts) {
        double result = this.price;
        for (Discount d : discounts) {
            if (d != null && d.isValid()) {
                result = result - (result * d.getPercentage() / 100.0);
                d.consume();
                logChange("Chained discount " + d.getCode() + " applied");
            }
        }
        this.price = result; // BAD: mutating price permanently instead of returning value
        return result;
    }

    // --- Presentation logic (violates SRP) ---
    public String renderHtmlCard() {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"book\">");
        sb.append("<img src=\"" + thumbnailUrl + "\"/>");
        sb.append("<h3>" + title + "</h3>");
        sb.append("<p>by " + author.getName() + "</p>");
        sb.append("<p>Category: " + category.getName() + "</p>");
        sb.append("<p>Price: " + price + "</p>");
        sb.append("<p>Stock: " + stock + "</p>");
        sb.append("</div>");
        logChange("Rendered HTML card");
        return sb.toString();
    }

    public String renderPlainText() {
        StringBuilder sb = new StringBuilder();
        sb.append(title + " by " + author.getName() + "");
        sb.append("Price: " + price + " | Stock: " + stock + "");
        return sb.toString();
    }

    // --- Persistence stub (violates SRP/DI) ---
    public void saveToDisk(String path) {
        try (FileWriter fw = new FileWriter(path, true)) {
            fw.write(this.toString() + "");
            logChange("Saved to " + path);
        } catch (IOException e) {
            logChange("Failed save: " + e.getMessage());
        }
    }

    // Overloaded save method that writes JSON-like data
    public void saveAsJson(String path) {
        try (FileWriter fw = new FileWriter(path, true)) {
            fw.write("{");
            fw.write("  \"title\": \"" + title + "\",");
            fw.write("  \"author\": \"" + author.getName() + "\",");
            fw.write("  \"price\": " + price + ",");
            fw.write("  \"stock\": " + stock + "");
            fw.write("}");
            logChange("Saved JSON to " + path);
        } catch (IOException e) {
            logChange("Failed JSON save: " + e.getMessage());
        }
    }

    // --- Misc ---
    private void logChange(String note) {
        changeLog.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " - " + note);
    }

    public List<String> getChangeLog() { return Collections.unmodifiableList(changeLog); }
    public String getTitle() { return title; }
    public Author getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public void archive() { archived = true; logChange("Archived"); }
    public boolean isArchived() { return archived; }
    public void addMetadata(String key, Object value) { meta.put(key, value); }
    public Object getMetadata(String key) { return meta.get(key); }
    public String toString() {
        return "Book[" + title + "," + author.getName() + ",price=" + price + ",stock=" + stock + "]";
    }
    
    // intentionally public to illustrate bad encapsulation (used by Review in original broken design)
    public void addPopularity(int delta) {
        this.popularityScore += delta;
        logChange("Popularity changed by " + delta + "; now=" + this.popularityScore);
    }

    // Convenience static methods that touch other classes directly (tight coupling)
    public static Book findByTitle(String title) {
        for (Book b : GLOBAL_BOOKS) {
            if (b.title.equalsIgnoreCase(title)) return b;
        }
        return null;
    }

    public static List<Book> allBooks() { return Collections.unmodifiableList(GLOBAL_BOOKS); }
}
   
