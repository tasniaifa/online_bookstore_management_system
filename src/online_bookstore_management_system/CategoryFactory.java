package online_bookstore_management_system;

import java.util.Scanner;

public class CategoryFactory {

    public static Category createCategoryFromInput(Scanner sc) {
        System.out.print("Category name: ");
        String name = sc.nextLine();
        System.out.print("Category description: ");
        String description = sc.nextLine();
        return new Category(name, description);
    }
}
