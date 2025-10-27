/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package online_bookstore_management_system;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author tasniafarinifa
 */
public class Customer {
   
    private String name;
    private String email;
    private String address;
    private String phone;
    private List<Order> orders = new ArrayList<>();
    private double outstandingBalance;

    // mixing persistence and domain
    private String notes;
    private boolean blacklisted;
    private String preferredPaymentMethod;

    public Customer(String name, String email, String address) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.outstandingBalance = 0.0;
        this.blacklisted = false;
    }

    public static Customer readFromInput(Scanner sc) {
        System.out.print("Customer name: ");
        String name = sc.nextLine();
        System.out.print("Customer email: ");
        String email = sc.nextLine();
        System.out.print("Customer address: ");
        String address = sc.nextLine();
        Customer c = new Customer(name, email, address);
        System.out.print("Phone (optional): ");
        String phone = sc.nextLine();
        if (!phone.isBlank()) c.phone = phone;
        return c;
    }

    public void addOrder(Order o) {
        orders.add(o);
    }

    public List<Order> getOrders() { return Collections.unmodifiableList(orders); }
    public String getEmail() { return email; }
    public String getName() { return name; }

    public void saveProfile(String file) {
        // persistence in domain class
        try (FileWriter fw = new FileWriter(file, true)) {
            fw.write(name + "," + email + "," + address + "");
        } catch (IOException e) { /* ignore */ }
    }

    // admin convenience method tightly coupled to Admin (bad)
    public void flagForReview(String reason) {
        this.blacklisted = true;
        this.notes = reason;
        AdminAudit.log("Customer flagged: " + name + " reason=" + reason);
    }
}
    

