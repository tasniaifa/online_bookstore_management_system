package online_bookstore_management_system;

import java.util.Map;

public class AuthorAnalyticsService {
    private int publishedCount = 0;

    public void incrementPublishedCount() {
        publishedCount++;
    }

    public int getPublishedCount() {
        return publishedCount;
    }

    public void recordAuthorNotification(Author author, Book book) {
        Analytics.record(
                "author_notified",
                Map.of("author", author.getName(), "book", book.getTitle()));
    }
}
