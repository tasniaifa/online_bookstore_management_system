package online_bookstore_management_system;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

// -----------------------------
// 1) Domain entity (Book) — SRP
// -----------------------------
public class Book {
    private final String isbn;
    private String title;
    private Author author;
    private Publisher publisher;
    private Category category;
    private String description;
    private final Date addedOn;
    private boolean archived;

    public Book(String title, Author author, Publisher publisher, Category category) {
        this.isbn = UUID.randomUUID().toString();
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.category = category;
        this.description = "";
        this.addedOn = new Date();
        this.archived = false;
    }

    // Domain behavior only (minimal)
    public void archive() { this.archived = true; }

    // Defensive copy for date
    public Date getAddedOn() { return new Date(addedOn.getTime()); }

    // Getters / setters for domain fields (no heavy logic)
    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public Author getAuthor() { return author; }
    public Publisher getPublisher() { return publisher; }
    public Category getCategory() { return category; }
    public String getDescription() { return description; }
    public boolean isArchived() { return archived; }
    public void setDescription(String d) { this.description = d == null ? "" : d; }

    @Override
    public String toString() {
        return "Book[" + title + ", isbn=" + isbn + ", price/stock handled elsewhere]";
    }
}


// -----------------------------
// 2) Inventory responsibility
// -----------------------------
interface InventoryService {
    boolean reduceStock(String bookIsbn, int qty);
    void increaseStock(String bookIsbn, int qty);
    int getStock(String bookIsbn);
}

class InMemoryInventoryService implements InventoryService {
    private final Map<String, Integer> stock = new HashMap<>();

    @Override
    public synchronized boolean reduceStock(String bookIsbn, int qty) {
        if (qty <= 0) return false;
        int current = stock.getOrDefault(bookIsbn, 0);
        if (current >= qty) {
            stock.put(bookIsbn, current - qty);
            return true;
        }
        return false;
    }

    @Override
    public synchronized void increaseStock(String bookIsbn, int qty) {
        if (qty <= 0) return;
        stock.put(bookIsbn, stock.getOrDefault(bookIsbn, 0) + qty);
    }

    @Override
    public int getStock(String bookIsbn) {
        return stock.getOrDefault(bookIsbn, 0);
    }
}


// -----------------------------
// 3) Pricing responsibility
// -----------------------------
interface PricingService {
    double calculateDiscountedPrice(double basePrice, Discount discount);
    double applyDiscounts(double basePrice, Collection<Discount> discounts);
}

class SimplePricingService implements PricingService {
    @Override
    public double calculateDiscountedPrice(double basePrice, Discount discount) {
        if (discount == null) return basePrice;
        return basePrice - (basePrice * discount.getPercentage() / 100.0);
    }

    @Override
    public double applyDiscounts(double basePrice, Collection<Discount> discounts) {
        double result = basePrice;
        if (discounts == null) return result;
        for (Discount d : discounts) {
            if (d != null && d.isValid()) {
                result = result - (result * d.getPercentage() / 100.0);
            }
        }
        return result; // DOES NOT mutate original price
    }
}


// -----------------------------
// 4) Rendering responsibility
// -----------------------------
interface BookRenderer {
    String renderHtml(Book book, int stock, double price);
    String renderText(Book book, int stock, double price);
}

class SimpleBookRenderer implements BookRenderer {
    @Override
    public String renderHtml(Book book, int stock, double price) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class='book'>");
        sb.append("<h3>").append(book.getTitle()).append("</h3>");
        sb.append("<p>by ").append(book.getAuthor().getName()).append("</p>");
        sb.append("<p>Price: ").append(price).append("</p>");
        sb.append("<p>Stock: ").append(stock).append("</p>");
        sb.append("</div>");
        return sb.toString();
    }

    @Override
    public String renderText(Book book, int stock, double price) {
        return book.getTitle() + " by " + book.getAuthor().getName()
            + " | Price: " + price + " | Stock: " + stock;
    }
}


// -----------------------------
// 5) Persistence responsibility
// -----------------------------
interface BookRepository {
    void save(Book book);
    Optional<Book> findByIsbn(String isbn);
    List<Book> findAll();
}

class InMemoryBookRepository implements BookRepository {
    private final Map<String, Book> map = new HashMap<>();

    @Override
    public void save(Book book) { map.put(book.getIsbn(), book); }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return Optional.ofNullable(map.get(isbn));
    }

    @Override
    public List<Book> findAll() { return Collections.unmodifiableList(new ArrayList<>(map.values())); }
}

class FileBookRepository implements BookRepository {
    private final String path;
    public FileBookRepository(String path) { this.path = path; }

    @Override
    public void save(Book book) {
        try (FileWriter fw = new FileWriter(path, true)) {
            fw.write(book.toString() + "\n");
        } catch (IOException e) {
            System.err.println("Save failed: " + e.getMessage());
        }
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) { throw new UnsupportedOperationException("Not implemented"); }

    @Override
    public List<Book> findAll() { throw new UnsupportedOperationException("Not implemented"); }
}


// -----------------------------
// 6) Logging / Audit responsibility
// -----------------------------
interface BookLogger {
    void log(String note);
}

class InMemoryBookLogger implements BookLogger {
    private final List<String> lines = new ArrayList<>();
    @Override
    public void log(String note) {
        String ts = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        lines.add(ts + " - " + note);
    }
    public List<String> getLog() { return Collections.unmodifiableList(lines); }
}


// -----------------------------
// 7) Analytics (cross-cutting)
// -----------------------------
interface AnalyticsService {
    void record(String event, Map<String,Object> meta);
}

class SimpleAnalyticsService implements AnalyticsService {
    @Override
    public void record(String event, Map<String,Object> meta) {
        // simple print for demo
        System.out.println("ANALYTICS: " + event + " => " + meta);
    }
}


// -----------------------------
// 8) Factory / Input responsibility
// -----------------------------
class BookFactory {
    public static Book fromInput(Scanner sc, Author a, Publisher p, Category c) {
        System.out.print("Book title: ");
        String title = sc.nextLine();
        System.out.print("Price (ignored by Book entity): ");
        sc.nextLine(); // read but pricing handled elsewhere
        System.out.print("Stock (ignored by Book entity): ");
        sc.nextLine();
        Book b = new Book(title, a, p, c);
        System.out.print("Add description? (y/n): ");
        if (sc.nextLine().trim().equalsIgnoreCase("y")) {
            System.out.println("Enter description (single line):");
            b.setDescription(sc.nextLine());
        }
        return b;
    }
}


// -----------------------------
// 9) Orchestration / High-level API
// -----------------------------
class BookService {
    private final BookRepository repo;
    private final InventoryService inventory;
    private final PricingService pricing;
    private final BookLogger logger;
    private final AnalyticsService analytics;

    public BookService(BookRepository repo,
                       InventoryService inventory,
                       PricingService pricing,
                       BookLogger logger,
                       AnalyticsService analytics) {
        this.repo = repo;
        this.inventory = inventory;
        this.pricing = pricing;
        this.logger = logger;
        this.analytics = analytics;
    }

    // create and persist book (service handles multiple responsibilities by delegating)
    public Book createBook(String title, Author author, Publisher publisher, Category category,
                           int initialStock, double initialPrice) {
        Book b = new Book(title, author, publisher, category);
        repo.save(b);
        inventory.increaseStock(b.getIsbn(), initialStock);
        // we don't store price in Book entity: external systems track price
        logger.log("Created book: " + b.getTitle());
        analytics.record("book_created", Map.of("isbn", b.getIsbn(), "title", b.getTitle()));
        return b;
    }

    public boolean sellBook(String isbn, int qty) {
        boolean ok = inventory.reduceStock(isbn, qty);
        if (ok) {
            logger.log("Sold " + qty + " of " + isbn);
            analytics.record("book_sold", Map.of("isbn", isbn, "qty", qty));
        }
        return ok;
    }

    public String renderBookCard(String isbn, double price) {
        Optional<Book> ob = repo.findByIsbn(isbn);
        if (ob.isEmpty()) return "Not found";
        Book b = ob.get();
        int stock = inventory.getStock(isbn);
        BookRenderer r = new SimpleBookRenderer();
        return r.renderHtml(b, stock, price);
    }
}
