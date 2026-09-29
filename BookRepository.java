package edu.fau.cen4010.feature1;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class BookRepository {
    private final String jdbcUrl;
    private final String user;
    private final String password;

    public BookRepository(String jdbcUrl, String user, String password) {
        this.jdbcUrl = jdbcUrl;
        this.user = user;
        this.password = password;
    }

    private Connection open() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, user, password);
    }

    public List<Book> findByGenre(String genre) throws SQLException {
        return query("SELECT b.*, (SELECT AVG(r.rating) FROM book_ratings r WHERE r.book_isbn=b.isbn) AS average_rating " +
                "FROM books b WHERE LOWER(b.genre)=LOWER(?) ORDER BY b.book_name, b.isbn", genre);
    }

    public List<Book> findTopSellers() throws SQLException {
        return query("SELECT b.*, (SELECT AVG(r.rating) FROM book_ratings r WHERE r.book_isbn=b.isbn) AS average_rating " +
                "FROM books b ORDER BY b.copies_sold DESC, b.isbn ASC LIMIT 10");
    }

    public List<Book> findAtOrAboveRating(BigDecimal minimumRating) throws SQLException {
        return query("SELECT b.*, (SELECT AVG(r.rating) FROM book_ratings r WHERE r.book_isbn=b.isbn) AS average_rating " +
                "FROM books b WHERE (SELECT AVG(r.rating) FROM book_ratings r WHERE r.book_isbn=b.isbn) >= ? " +
                "ORDER BY average_rating DESC, b.book_name, b.isbn", minimumRating);
    }

    public int discountPublisher(String publisher, BigDecimal percent) throws SQLException {
        String sql = "UPDATE books SET price=ROUND(price * (1 - ? / 100), 2) WHERE LOWER(publisher)=LOWER(?)";
        try (Connection c = open(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setBigDecimal(1, percent);
            ps.setString(2, publisher);
            return ps.executeUpdate();
        }
    }

    private List<Book> query(String sql, Object... parameters) throws SQLException {
        try (Connection c = open(); PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 0; i < parameters.length; i++) ps.setObject(i + 1, parameters[i]);
            try (ResultSet rs = ps.executeQuery()) {
                List<Book> books = new ArrayList<>();
                while (rs.next()) books.add(new Book(rs.getString("isbn"), rs.getString("book_name"),
                        rs.getString("description"), rs.getBigDecimal("price"), rs.getString("author"),
                        rs.getString("genre"), rs.getString("publisher"), rs.getInt("year_published"),
                        rs.getLong("copies_sold"), rs.getBigDecimal("average_rating")));
                return books;
            }
        }
    }
}
