package online_bookstore_management_system;

import java.util.Scanner;

public class BookstoreFacade {
  
    public static Author createAuthor(Scanner sc) {
        System.out.println("--- Create Author ---");
        return Author.readFromInput(sc);
    }

    public static Publisher createPublisher(Scanner sc) {
        System.out.println("--- Create Publisher ---");
        return Publisher.readFromInput(sc);
    }

    public static Category createCategory(Scanner sc) {
        System.out.println("--- Create Category ---");
        return Category.readFromInput(sc);
    }

    public static Book createBook(Scanner sc, Author a, Publisher p, Category c) {
        System.out.println("--- Create Book ---");
        return Book.readFromInput(sc, a, p, c);
    }

    public static Customer createCustomer(Scanner sc) {
        System.out.println("--- Create Customer ---");
        return Customer.readFromInput(sc);
    }

    public static void registerCustomer(Customer cust, Scanner sc) {
        System.out.println("Registering customer (auth)...");
        UserAuthentication auth = new UserAuthentication();
        System.out.print("Choose password: ");
        String pwd = sc.nextLine().trim();
        auth.register(cust, pwd);
    }

    public static Order checkoutBook(Customer cust, Book book, Scanner sc) {
        System.out.println("--- Shopping ---");
        ShoppingCart cart = new ShoppingCart(cust);
        cart.addItem(book, 1);

        System.out.print("Create a discount? (y/n): ");
        if (sc.nextLine().trim().equalsIgnoreCase("y")) {
            Discount d = Discount.readFromInput(sc);
            cart.applyDiscount(d);
        }

        return cart.checkout();
    }

    public static Payment processPayment(Order order, Scanner sc) {
        System.out.println("Choose payment method: card/cod");
        String method = sc.nextLine().trim();

        if (method.equalsIgnoreCase("card")) {
            System.out.print("CardNumber: ");
            String card = sc.nextLine().trim();
            System.out.print("Expiry (mm/yy): ");
            String exp = sc.nextLine().trim();
            System.out.print("CVV: ");
            String cvv = sc.nextLine().trim();

            Payment payment = new Payment();
            payment.processCardPayment(order, card, exp, cvv);
            return payment;
        } else {
            Payment payment = new Payment();
            payment.processCashOnDelivery(order);
            return payment;
        }
    }

    public static void generateInvoiceAndAdmin(Order order, Payment payment) {
        Invoice invoice = new Invoice(order, payment);
        invoice.generate();

        Admin admin = new Admin();
        admin.printOrderSummary(order);
        admin.backupData("backup_ifa.dat");
    }

    public static void runDemoFacade() {
        Author a = new Author("S. King", "stephen.king@example.com");
        Publisher p = new Publisher("FictionHouse", "123 Fiction Ave");
        Category c = new Category("Horror", "Scary books");

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

        System.out.println("Demo finished — everything handled via Facade now.");
    }
}
