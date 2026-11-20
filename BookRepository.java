import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BookRepository {

    private final List<Book> books = new ArrayList<>();

    public BookRepository() {
        loadBooks();
    }

    private void loadBooks() {
        books.add(new Book("978001", "Java Basics", "John Smith",
                "Programming", "TechBooks", 49.99, 5000, 4.2));

        books.add(new Book("978002", "Advanced Java", "Jane Doe",
                "Programming", "TechBooks", 59.99, 9000, 4.7));

        books.add(new Book("978003", "Python for Beginners", "Alex Brown",
                "Programming", "CodePress", 39.99, 12000, 4.8));

        books.add(new Book("978004", "Artificial Intelligence", "Sam Lee",
                "Artificial Intelligence", "TechBooks", 69.99, 15000, 4.9));

        books.add(new Book("978005", "Database Systems", "Chris Green",
                "Databases", "CodePress", 54.99, 7000, 4.1));

        books.add(new Book("978006", "Machine Learning", "Taylor White",
                "Artificial Intelligence", "TechBooks", 64.99, 11000, 4.6));

        books.add(new Book("978007", "Data Science", "Mike Jones",
                "Data Science", "TechBooks", 61.99, 8500, 4.5));

        books.add(new Book("978008", "Web Development", "Lisa Brown",
                "Programming", "CodePress", 44.99, 6500, 4.0));

        books.add(new Book("978009", "Algorithms", "Robert King",
                "Computer Science", "TechBooks", 55.99, 10000, 4.7));

        books.add(new Book("978010", "Cybersecurity", "Emily White",
                "Cybersecurity", "CodePress", 57.99, 9500, 4.4));

        books.add(new Book("978011", "Cloud Computing", "David Clark",
                "Computer Science", "TechBooks", 62.99, 8000, 4.3));
    }

    public List<Book> findByGenre(String genre) {
        return books.stream()
                .filter(book -> book.getGenre().equalsIgnoreCase(genre))
                .collect(Collectors.toList());
    }

    public List<Book> findTop10Sellers() {
        return books.stream()
                .sorted(Comparator.comparingInt(Book::getCopiesSold).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<Book> findByMinimumRating(double minimumRating) {
        return books.stream()
                .filter(book -> book.getRating() >= minimumRating)
                .sorted(Comparator.comparingDouble(Book::getRating).reversed())
                .collect(Collectors.toList());
    }

    public int discountByPublisher(String publisher, double discountPercent) {
        int count = 0;

        for (Book book : books) {
            if (book.getPublisher().equalsIgnoreCase(publisher)) {
                book.applyDiscount(discountPercent);
                count++;
            }
        }

        return count;
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }
}
