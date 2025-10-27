/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package online_bookstore_management_system;

import java.util.Date;

/**
 *
 * @author tasniafarinifa
 */
public class Review {
    private Book book;
    private Customer author;
    private int rating; // 1..5
    private String comment;
    private Date postedAt;
    private boolean verifiedPurchase;

    public Review(Book book, Customer author, int rating, String comment) {
        this.book = book;
        this.author = author;
        this.rating = rating;
        this.comment = comment;
        this.postedAt = new Date();
        this.verifiedPurchase = checkVerified(author, book);
        // record to book's popularity (side effect)
        book.addPopularity(rating);
        // store review in global bad store
        BadReviewStore.store(this);
    }

    private boolean checkVerified(Customer c, Book b) {
        for (Order o : c.getOrders()) {
            for (OrderItem it : o.getItems()) {
                if (it.getBook().getTitle().equalsIgnoreCase(b.getTitle())) return true;
            }
        }
        return false;
    }
    
}
