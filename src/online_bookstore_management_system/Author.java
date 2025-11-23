/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package online_bookstore_management_system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author tasniafarinifa
 */
public class Author {
    private String name;
    private String email;
    private String biography;
    private List<Book> books = new ArrayList<>();

    // analytics counters (shouldn't be here)
    private int publishedCount;
    private Date lastNotified;

    public Author(String name, String email) {
        this.name = name;
        this.email = email;
        this.biography = "";
    }

    public static Author readFromInput(Scanner sc) {
        System.out.print("Author name: ");
        String name = sc.nextLine();
        System.out.print("Author email: ");
        String email = sc.nextLine();
        Author a = new Author(name, email);
        System.out.print("Add bio? (y/n): ");
        if (sc.nextLine().trim().equalsIgnoreCase("y")) {
            System.out.print("Bio: ");
            a.biography = sc.nextLine();
        }
        return a;
    }

    public void addBook(Book b) {
        books.add(b);
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getBiography() {
        return biography;
    }

    public List<Book> getBooks() {
        return Collections.unmodifiableList(books);
    }
}
