package online_bookstore_management_system;

import java.util.*;

public class Review {
    private final String id;
    private final String bookIsbn;
    private final String authorId;
    private final int rating;
    private final String comment;
    private final Date postedAt;
    private final boolean verifiedPurchase;

    public Review(String id, String bookIsbn, String authorId, int rating, String comment, boolean verifiedPurchase) {
        if (rating < 1 || rating > 5) throw new IllegalArgumentException("Rating must be 1..5");
        this.id = (id == null) ? UUID.randomUUID().toString() : id;
        this.bookIsbn = bookIsbn;
        this.authorId = authorId;
        this.rating = rating;
        this.comment = (comment == null) ? "" : comment;
        this.postedAt = new Date();
        this.verifiedPurchase = verifiedPurchase;
    }

    public String getId() { return id; }
    public String getBookIsbn() { return bookIsbn; }
    public String getAuthorId() { return authorId; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
    public Date getPostedAt() { return new Date(postedAt.getTime()); }
    public boolean isVerifiedPurchase() { return verifiedPurchase; }

    @Override
    public String toString() {
        return "Review[" + id + ", book=" + bookIsbn + ", author=" + authorId + ", rating=" + rating + "]";
    }
}

interface ReviewRepository {
    void save(Review review);
    List<Review> findByBook(String bookIsbn);
}

interface PopularityService {
    void addPopularity(String bookIsbn, int points);
    int getPopularity(String bookIsbn);
}

interface PurchaseService {
    boolean hasPurchased(Customer customer, Book book);
}
//____________________________________________
class InMemoryReviewRepository implements ReviewRepository {
    private final Map<String, List<Review>> store = new HashMap<>();

    @Override
    public void save(Review review) {
        store.computeIfAbsent(review.getBookIsbn(), k -> new ArrayList<>()).add(review);
    }

    @Override
    public List<Review> findByBook(String bookIsbn) {
        return store.getOrDefault(bookIsbn, Collections.emptyList());
    }
}

class InMemoryPopularityService implements PopularityService {
    private final Map<String, Integer> popularity = new HashMap<>();

    @Override
    public void addPopularity(String bookIsbn, int points) {
        popularity.put(bookIsbn, popularity.getOrDefault(bookIsbn, 0) + points);
    }

    @Override
    public int getPopularity(String bookIsbn) {
        return popularity.getOrDefault(bookIsbn, 0);
    }
}

class InMemoryPurchaseService implements PurchaseService {
    @Override
    public boolean hasPurchased(Customer customer, Book book) {
        for (Order order : customer.getOrders()) {
            for (OrderItem item : order.getItems()) {
                if (item.getBook().getIsbn().equals(book.getIsbn())) return true;
            }
        }
        return false;
    }
}

// ---------------------------
// Service to create reviews
// ---------------------------
class ReviewService {
    private final ReviewRepository repo; //interface reference 
    private final PopularityService popularity;
    private final PurchaseService purchase;

    public ReviewService(ReviewRepository repo, PopularityService popularity, PurchaseService purchase) {
        this.repo = repo;
        this.popularity = popularity;
        this.purchase = purchase;
    }

    public Review createReview(Customer author, Book book, int rating, String comment) {
        boolean verified = purchase.hasPurchased(author, book);

        Review review = new Review(null, book.getIsbn(), author.getId(), rating, comment, verified);

        repo.save(review);                       // save review
        popularity.addPopularity(book.getIsbn(), rating); // update book popularity

        return review;
    }
}
