package online_bookstore_management_system;

import java.util.Scanner;

public class Online_bookstore_management_system {

    // Global scanner
    public static final Scanner GLOBAL_SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Online Bookstore Management System (Facade Applied) ===");
        System.out.println("Type 'demo' to run the demo, 'input' to create objects interactively, or 'exit' to quit.");

        while (true) {
            System.out.print("cmd> ");
            String cmd = GLOBAL_SCANNER.nextLine().trim();

            if (cmd.equalsIgnoreCase("exit")) break;

            switch (cmd.toLowerCase()) {
                case "demo":
                    BookstoreFacade.runDemoFacade();
                    break;

                case "input":
                    runInteractiveFacade();
                    break;

                case "help":
                    System.out.println("Commands: demo, input, help, exit");
                    break;

                default:
                    System.out.println("Unknown command. Type help.");
            }
        }

        System.out.println("Goodbye.");
    }

    private static void runInteractiveFacade() {
        try {
            Scanner sc = GLOBAL_SCANNER;

            Author author = BookstoreFacade.createAuthor(sc);
            Publisher publisher = BookstoreFacade.createPublisher(sc);
            Category category = BookstoreFacade.createCategory(sc);
            Book book = BookstoreFacade.createBook(sc, author, publisher, category);

            Customer customer = BookstoreFacade.createCustomer(sc);
            BookstoreFacade.registerCustomer(customer, sc);

            Order order = BookstoreFacade.checkoutBook(customer, book, sc);
            Payment payment = BookstoreFacade.processPayment(order, sc);

            BookstoreFacade.generateInvoiceAndAdmin(order, payment);

            System.out.println("Interactive run complete (Facade Applied).");

        } catch (Exception e) {
            System.err.println("Interactive run failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
