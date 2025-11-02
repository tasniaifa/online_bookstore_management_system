import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import online_bookstore_management_system.Review;

// ------------------------------
// 1️⃣ SRP + OCP + DIP
// ------------------------------
interface ReviewStore {
    void save(Review review);
    List<Review> findAll();
}

// ------------------------------
// 2️⃣ In-memory Implementation
// ------------------------------
class InMemoryReviewStore implements ReviewStore {
    private final List<Review> all = new ArrayList<>();

    @Override
    public void save(Review review) {
        all.add(review);
    }

    @Override
    public List<Review> findAll() {
        return Collections.unmodifiableList(all);
    }
}

// ------------------------------
// 3️⃣ File-based Implementation
// ------------------------------
class FileReviewStore implements ReviewStore {
    private final String filePath;

    public FileReviewStore(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void save(Review review) {
        try (FileWriter fw = new FileWriter(filePath, true)) {
            fw.write(review.toString() + "\n");
        } catch (IOException e) {
            System.err.println("Error writing review: " + e.getMessage());
        }
    }

    @Override
    public List<Review> findAll() {
        throw new UnsupportedOperationException("File reading not implemented yet");
    }
}
