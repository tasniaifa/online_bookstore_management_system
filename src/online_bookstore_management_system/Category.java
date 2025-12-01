package online_bookstore_management_system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * REFACTORED CATEGORY CLASS
 * -------------------------
 * ✔ SRP: Category now only stores books + metadata.
 * ✔ OCP: Search logic moved to external service so it can be extended.
 * ✔ DIP: Category does NOT depend on concrete cache implementations.
 */
public class Category {

    private String name;
    private String description;
    private List<Book> books = new ArrayList<>();

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    Scanner sc = new Scanner(System.in);
    Category newCategory = CategoryFactory.createCategoryFromInput(sc);

    /**
     * Category no longer handles caching itself (SRP fixed)
     */
    public void addBook(Book b) {
        books.add(b);
    }

    public List<Book> getBooks() {
        return Collections.unmodifiableList(books);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}

/*
 * ============================================================
 * SEARCH CACHE (SRP + OCP + DIP)
 * Category depends on abstraction, not implementation.
 * Previously was: Map<String, Book> inside Category (bad SRP)
 * ============================================================
 */

interface CategorySearchService {
    Book findByTitle(String title, Category category);

    void updateCache(Category category, Book book);
}

class CachedCategorySearchService implements CategorySearchService {

    // Cache stored OUTSIDE Category to follow SRP
    private final Map<String, Book> cache;

    public CachedCategorySearchService(Map<String, Book> cache) {
        this.cache = cache;
    }

    @Override
    public Book findByTitle(String title, Category category) {
        return cache.get(title.toLowerCase());
    }

    @Override
    public void updateCache(Category category, Book book) {
        cache.put(book.getTitle().toLowerCase(), book);
    }
}

/*
 * ============================================================
 * NO-CACHE SEARCH (example of OCP)
 * You can add new search types without modifying Category.
 * ============================================================
 */
class LinearCategorySearchService implements CategorySearchService {

    @Override
    public Book findByTitle(String title, Category category) {
        for (Book b : category.getBooks()) {
            if (b.getTitle().equalsIgnoreCase(title))
                return b;
        }
        return null;
    }

    @Override
    public void updateCache(Category category, Book book) {
        // no cache used here — intentionally empty
    }
}
