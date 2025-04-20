import java.util.HashMap;
import java.util.Map;

public class OnlineLibrarySystem {

    private Map<String, Integer> books;         // bookId -> status (1 = available, 2 = borrowed)
    private Map<String, String> borrowedBy;     // bookId -> userId

    // Constructor to initialize the books and borrowed tracking
    public OnlineLibrarySystem() {
        books = new HashMap<>();
        borrowedBy = new HashMap<>();

        books.put("book1", 1); // available
        books.put("book2", 2); // borrowed
        books.put("book3", 1); // available

        borrowedBy.put("book2", "userX"); // Simulate already borrowed
    }

    // Pre-condition: Book must exist and be available
    // Post-condition: Book is marked as borrowed and user is recorded
    public void borrowBook(String bookId, User user) {
        assert bookIdExists(bookId) : "Error: Book '" + bookId + "' does not exist in the system.";
        assert isAvailable(bookId) : "Error: Book '" + bookId + "' is already borrowed.";

        System.out.println("User " + user.getUserId() + " is borrowing book: " + bookId);
        books.put(bookId, 2); // Mark as borrowed
        borrowedBy.put(bookId, user.getUserId());

        assert isBorrowed(bookId) : "Error: Failed to borrow the book.";
    }

    // Pre-condition: Book must be borrowed by this user
    // Post-condition: Book is marked as available
    public void returnBook(String bookId, User user) {
        assert bookIdExists(bookId) : "Error: Book '" + bookId + "' does not exist in the system.";
        assert isBorrowed(bookId) : "Error: Book '" + bookId + "' wasn't borrowed.";
        assert borrowedBy.get(bookId).equals(user.getUserId()) : "Error: User '" + user.getUserId() + "' didn't borrow book '" + bookId + "'.";

        System.out.println("User " + user.getUserId() + " is returning book: " + bookId);
        books.put(bookId, 1); // Mark as available
        borrowedBy.remove(bookId);

        assert isAvailable(bookId) : "Error: Failed to return the book.";
    }

    // Pre-condition: Book must exist
    // Post-condition: Print availability info
    public void checkAvailability(String bookId) {
        assert bookIdExists(bookId) : "Error: Book '" + bookId + "' does not exist in the system.";

        int status = books.get(bookId);
        if (status == 1) {
            System.out.println("Book " + bookId + " is available.");
        } else {
            String userId = borrowedBy.get(bookId);
            System.out.println("Book " + bookId + " is currently borrowed by user " + userId + ".");
        }
    }

    // Invariant: All book statuses must be valid (1 or 2)
    public void invariant() {
        for (int status : books.values()) {
            assert status == 1 || status == 2 : "Invariant violation: Invalid book status.";
        }
        System.out.println("Invariant is satisfied: All books have valid status.");
    }

    // Helper method: Check if book exists
    private boolean bookIdExists(String bookId) {
        return books.containsKey(bookId);
    }

    // Helper method: Check if book is available
    private boolean isAvailable(String bookId) {
        return books.get(bookId) == 1;
    }

    // Helper method: Check if book is borrowed
    private boolean isBorrowed(String bookId) {
        return books.get(bookId) == 2;
    }

    // Main method for testing
    public static void main(String[] args) {
        OnlineLibrarySystem library = new OnlineLibrarySystem();
        User user1 = new User("user123");

        library.checkAvailability("book1");
        library.borrowBook("book1", user1);
        library.checkAvailability("book1");
        library.returnBook("book1", user1);
        library.checkAvailability("book1");

        library.invariant();

        // Test failure case: book does not exist
        library.borrowBook("book4", user1);
    }
}
