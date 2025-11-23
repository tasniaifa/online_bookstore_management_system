package online_bookstore_management_system;

import java.util.Date;

public class AuthorNotificationService {
    private Date lastNotified;

    public void notifyAuthorAddedBook(Author author, Book book) {
        // Concrete implementation for now (DIP/OCP fixed in second refactor)
        System.out.println("[Author] Notifying " + author.getEmail()
                + " about new book: " + book.getTitle());
        lastNotified = new Date();
    }

    public Date getLastNotified() {
        return lastNotified;
    }
}
