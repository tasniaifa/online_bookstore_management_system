/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package online_bookstore_management_system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author tasniafarinifa
 */
public class Category {
    private String name;
    private String description;
    private List<Book> books = new ArrayList<>();

    // search cache baked into category (bad)
    private Map<String, Book> searchCache = new HashMap<>();

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static Category readFromInput(Scanner sc) {
        System.out.print("Category name: ");
        String name = sc.nextLine();
        System.out.print("Category description: ");
        String desc = sc.nextLine();
        return new Category(name, desc);
    }

    public void addBook(Book b) {
        books.add(b);
        searchCache.put(b.getTitle().toLowerCase(), b);
    }

    public Book findByTitleCached(String title) {
        return searchCache.get(title.toLowerCase());
    }

    public List<Book> getBooks() { return Collections.unmodifiableList(books); }
    public String getName() { return name; }
}
    

