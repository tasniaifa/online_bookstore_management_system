package online_bookstore_management_system;

import java.util.Map;

public class AuthorAnalyticsService implements AuthorEventHandler {

    @Override
    public void onBookAdded(Author author, Book book) {
        Analytics.record(
                "author_book_added",
                Map.of("author", author.getName(), "book", book.getTitle()));
    }
}
