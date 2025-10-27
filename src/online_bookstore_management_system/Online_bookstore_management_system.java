package online_bookstore_management_system;

import java.util.*;
import java.text.SimpleDateFormat;
import java.io.*;

public class Online_bookstore_management_system {
    // Global scanner used across the bad design — violates encapsulation and SRP
    public static final Scanner GLOBAL_SCANNER = new Scanner(System.in);
    
    // Very large main to demonstrate interconnections and prompt inputs
    public static void main(String[] args) {
        System.out.println("=== Online Bookstore Management System (Bad Design Edition) ===");
        System.out.println("Type 'demo' to run the demo, 'input' to create objects interactively, or 'exit' to quit.");
        while (true) {
            System.out.print("cmd> ");
            String cmd = GLOBAL_SCANNER.nextLine().trim();
            if (cmd.equalsIgnoreCase("exit")) break;
            if (cmd.equalsIgnoreCase("demo")) {
                runDemo();
            } else if (cmd.equalsIgnoreCase("input")) {
                interactiveRun();
            } else if (cmd.equalsIgnoreCase("help")) {
                System.out.println("Commands: demo, input, help, exit");
            } else {
                System.out.println("Unknown command. Type help.");
            }
        }
        System.out.println("Goodbye.");
    }

    private static void interactiveRun() {
        try {
            System.out.println("--- Create Author ---");
            Author a = Author.readFromInput(GLOBAL_SCANNER);

            System.out.println("--- Create Publisher ---");
            Publisher p = Publisher.readFromInput(GLOBAL_SCANNER);

            System.out.println("--- Create Category ---");
            Category c = Category.readFromInput(GLOBAL_SCANNER);

            System.out.println("--- Create Book ---");
            Book book = Book.readFromInput(GLOBAL_SCANNER, a, p, c);

            System.out.println("--- Create Customer ---");
            Customer cust = Customer.readFromInput(GLOBAL_SCANNER);

            System.out.println("Registering customer (auth)...");
            UserAuthentication auth = new UserAuthentication();
            System.out.print("Choose password: ");
            String pwd = GLOBAL_SCANNER.nextLine().trim();
            auth.register(cust, pwd);

            System.out.println("--- Shopping ---");
            ShoppingCart cart = new ShoppingCart(cust);
            cart.addItem(book, 1);

            System.out.print("Create a discount? (y/n): ");
            if (GLOBAL_SCANNER.nextLine().trim().equalsIgnoreCase("y")) {
                Discount d = Discount.readFromInput(GLOBAL_SCANNER);
                cart.applyDiscount(d);
            }

            Order order = cart.checkout();

            System.out.println("Choose payment method: card/cod");
            String method = GLOBAL_SCANNER.nextLine().trim();
            Payment payment = new Payment();
            if (method.equalsIgnoreCase("card")) {
                System.out.print("CardNumber: ");
                String card = GLOBAL_SCANNER.nextLine().trim();
                System.out.print("Expiry (mm/yy): ");
                String exp = GLOBAL_SCANNER.nextLine().trim();
                System.out.print("CVV: ");
                String cvv = GLOBAL_SCANNER.nextLine().trim();
                payment.processCardPayment(order, card, exp, cvv);
            } else {
                payment.processCashOnDelivery(order);
            }

            Invoice invoice = new Invoice(order, payment);
            invoice.generate();

            Admin admin = new Admin();
            admin.printOrderSummary(order);
            admin.backupData("backup_ifa.dat");

            System.out.println("Interactive run complete.");
        } catch (Exception e) {
            System.err.println("Interactive run failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void runDemo() {
        // Quick demo wiring: lots of direct coupling and mixed responsibilities
        Author a = new Author("S. King", "stephen.king@example.com");
        Publisher p = new Publisher("FictionHouse", "123 Fiction Ave");
        Category c = new Category("Horror", "Scary books");

        // create many books to inflate file use and create more interactions
        Book book1 = new Book("The Dark Tale", a, p, c, 499, 10);
        Book book2 = new Book("Another Story", a, p, c, 299, 5);
        Book book3 = new Book("Collected Nightmares", a, p, c, 799, 3);

        Customer cust = new Customer("Rafi", "rafi@example.com", "Dhaka");

        UserAuthentication auth = new UserAuthentication();
        auth.register(cust, "password123");

        ShoppingCart cart = new ShoppingCart(cust);
        cart.addItem(book1, 2);
        cart.addItem(book2, 1);
        cart.addItem(book3, 1);

        Discount disc = new Discount("WELCOME10", 10);
        cart.applyDiscount(disc);

        Order order = cart.checkout();

        Payment payment = new Payment();
        payment.processCardPayment(order, "4111111111111111", "12/25", "123");

        Invoice invoice = new Invoice(order, payment);
        invoice.generate();

        Admin admin = new Admin();
        admin.printOrderSummary(order);
        admin.backupData("/tmp/backup.dat");

        System.out.println("Demo finished — open the canvas to inspect the large classes.");

        // Additional actions to increase coupling and side effects
        Review r1 = new Review(book1, cust, 5, "Amazing!");
        Review r2 = new Review(book2, cust, 4, "Good read");
        book1.saveToDisk("books_dump.log");
        p.publishBook(book3);
        a.addBook(book3);
        // create more discounts and abuse admin class
        admin.createDiscountForCampaign("FLASH50", 50);
    }
}

