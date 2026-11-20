import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class BookBrowsingAPI {

    private static final BookRepository repository = new BookRepository();

    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(
                new InetSocketAddress(8080), 0);

        server.createContext("/books/genre",
                BookBrowsingAPI::getBooksByGenre);

        server.createContext("/books/top-sellers",
                BookBrowsingAPI::getTopSellers);

        server.createContext("/books/rating",
                BookBrowsingAPI::getBooksByRating);

        server.createContext("/books/discount",
                BookBrowsingAPI::discountBooks);

        System.out.println("Geek Text Book Browsing API");
        System.out.println("Running at http://localhost:8080");
        System.out.println();
        System.out.println("Available endpoints:");
        System.out.println("GET  /books/genre/{genre}");
        System.out.println("GET  /books/top-sellers");
        System.out.println("GET  /books/rating/{rating}");
        System.out.println("PUT  /books/discount?publisher={publisher}&discount={percent}");

        server.start();
    }

    private static void getBooksByGenre(HttpExchange exchange)
            throws IOException {

        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            send(exchange, 405, "Method Not Allowed");
            return;
        }

        String[] parts = exchange.getRequestURI()
                .getPath().split("/");

        if (parts.length < 4) {
            send(exchange, 400, "Please provide a genre.");
            return;
        }

        String genre = URLDecoder.decode(
                parts[3], StandardCharsets.UTF_8);

        List<Book> books = repository.findByGenre(genre);

        sendJson(exchange, books);
    }

    private static void getTopSellers(HttpExchange exchange)
            throws IOException {

        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            send(exchange, 405, "Method Not Allowed");
            return;
        }

        List<Book> books = repository.findTop10Sellers();

        sendJson(exchange, books);
    }

    private static void getBooksByRating(HttpExchange exchange)
            throws IOException {

        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            send(exchange, 405, "Method Not Allowed");
            return;
        }

        String[] parts = exchange.getRequestURI()
                .getPath().split("/");

        if (parts.length < 4) {
            send(exchange, 400, "Please provide a rating.");
            return;
        }

        try {
            double rating = Double.parseDouble(parts[3]);

            if (rating < 0 || rating > 5) {
                send(exchange, 400,
                        "Rating must be between 0 and 5.");
                return;
            }

            List<Book> books =
                    repository.findByMinimumRating(rating);

            sendJson(exchange, books);

        } catch (NumberFormatException e) {
            send(exchange, 400,
                    "Rating must be a number.");
        }
    }

    private static void discountBooks(HttpExchange exchange)
            throws IOException {

        if (!exchange.getRequestMethod().equalsIgnoreCase("PUT")) {
            send(exchange, 405, "Use PUT for this operation.");
            return;
        }

        String query = exchange.getRequestURI().getQuery();

        if (query == null) {
            send(exchange, 400,
                    "Publisher and discount are required.");
            return;
        }

        String publisher = null;
        double discount = -1;

        String[] parameters = query.split("&");

        for (String parameter : parameters) {

            String[] pair = parameter.split("=", 2);

            if (pair.length != 2) {
                continue;
            }

            String key = pair[0];

            String value = URLDecoder.decode(
                    pair[1], StandardCharsets.UTF_8);

            if (key.equalsIgnoreCase("publisher")) {
                publisher = value;
            }

            if (key.equalsIgnoreCase("discount")) {
                try {
                    discount = Double.parseDouble(value);
                } catch (NumberFormatException e) {
                    send(exchange, 400,
                            "Discount must be a number.");
                    return;
                }
            }
        }

        if (publisher == null || publisher.isEmpty()) {
            send(exchange, 400, "Publisher is required.");
            return;
        }

        if (discount < 0 || discount > 100) {
            send(exchange, 400,
                    "Discount must be between 0 and 100.");
            return;
        }

        int updated =
                repository.discountByPublisher(
                        publisher, discount);

        send(exchange, 200,
                updated + " book(s) discounted successfully.");
    }

    private static void sendJson(
            HttpExchange exchange,
            List<Book> books) throws IOException {

        StringBuilder json = new StringBuilder("[");

        for (int i = 0; i < books.size(); i++) {

            json.append("  ")
                    .append(books.get(i).toJson());

            if (i < books.size() - 1) {
                json.append(",");
            }

            json.append("");
        }

        json.append("]");

        exchange.getResponseHeaders()
                .set("Content-Type", "application/json");

        send(exchange, 200, json.toString());
    }

    private static void send(
            HttpExchange exchange,
            int status,
            String response) throws IOException {

        byte[] data =
                response.getBytes(StandardCharsets.UTF_8);

        exchange.sendResponseHeaders(
                status, data.length);

        try (OutputStream output =
                     exchange.getResponseBody()) {

            output.write(data);
        }
    }
}
