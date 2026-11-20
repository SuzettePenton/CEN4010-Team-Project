public class Book {
    private String isbn;
    private String title;
    private String author;
    private String genre;
    private String publisher;
    private double price;
    private int copiesSold;
    private double rating;

    public Book(String isbn, String title, String author, String genre,
                String publisher, double price, int copiesSold, double rating) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publisher = publisher;
        this.price = price;
        this.copiesSold = copiesSold;
        this.rating = rating;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public String getPublisher() {
        return publisher;
    }

    public double getPrice() {
        return price;
    }

    public int getCopiesSold() {
        return copiesSold;
    }

    public double getRating() {
        return rating;
    }

    public void applyDiscount(double discountPercent) {
        price = price * (1 - discountPercent / 100);
    }

    public String toJson() {
        return "{"
                + "\"isbn\":\"" + escape(isbn) + "\","
                + "\"title\":\"" + escape(title) + "\","
                + "\"author\":\"" + escape(author) + "\","
                + "\"genre\":\"" + escape(genre) + "\","
                + "\"publisher\":\"" + escape(publisher) + "\","
                + "\"price\":" + String.format("%.2f", price) + ","
                + "\"copiesSold\":" + copiesSold + ","
                + "\"rating\":" + rating
                + "}";
    }

    private String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    @Override
    public String toString() {
        return title + " | " + author + " | " + genre
                + " | $" + String.format("%.2f", price)
                + " | Sold: " + copiesSold
                + " | Rating: " + rating;
    }
}
