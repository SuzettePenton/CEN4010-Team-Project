package edu.fau.cen4010.feature1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpsServer;
import com.sun.net.httpserver.HttpsConfigurator;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Map;

/** Small JDK HTTP adapter for the Feature 1 routes. Replace this adapter if the team adopts a shared REST framework. */
public final class Feature1Server {
    private static final ObjectMapper JSON = new ObjectMapper();
    private final BookRepository books;

    Feature1Server(BookRepository books) { this.books = books; }

    public static void main(String[] args) throws IOException {
        String url = required("DB_URL");
        String user = required("DB_USER");
        String password = System.getenv().getOrDefault("DB_PASSWORD", "");
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8443"));
        HttpsServer server = HttpsServer.create(new InetSocketAddress(port), 0);
        server.setHttpsConfigurator(new HttpsConfigurator(sslContext()));
        Feature1Server app = new Feature1Server(new BookRepository(url, user, password));
        server.createContext("/books", app::handleBooks);
        server.createContext("/books/discount", app::handleDiscount);
        server.start();
        System.out.println("Feature 1 HTTPS API listening on port " + port);
    }

    private static SSLContext sslContext() throws IOException {
        try {
            String path = required("TLS_KEYSTORE_PATH");
            char[] password = required("TLS_KEYSTORE_PASSWORD").toCharArray();
            KeyStore store = KeyStore.getInstance("PKCS12");
            try (FileInputStream input = new FileInputStream(path)) { store.load(input, password); }
            KeyManagerFactory managers = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            managers.init(store, password);
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(managers.getKeyManagers(), null, null);
            return context;
        } catch (Exception e) {
            throw new IOException("Could not initialize HTTPS. Check TLS_KEYSTORE_PATH and TLS_KEYSTORE_PASSWORD.", e);
        }
    }

    private static String required(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) throw new IllegalStateException("Set environment variable " + name);
        return value;
    }

    private void handleBooks(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            if (!exchange.getRequestMethod().equals("GET")) { send(exchange, 405, Map.of("error", "Method not allowed")); return; }
            if (path.equals("/books/top-sellers")) {
                send(exchange, 200, books.findTopSellers()); return;
            }
            Map<String, String> query = queryParams(exchange.getRequestURI().getRawQuery());
            if (path.equals("/books/genre")) {
                String genre = query.get("genre");
                if (genre == null || genre.isBlank()) { send(exchange, 400, Map.of("error", "genre is required")); return; }
                send(exchange, 200, books.findByGenre(genre)); return;
            }
            if (path.equals("/books/rating")) {
                BigDecimal rating = FeatureRules.rating(query.get("rating"));
                send(exchange, 200, books.findAtOrAboveRating(rating)); return;
            }
            send(exchange, 404, Map.of("error", "Route not found"));
        } catch (IllegalArgumentException e) {
            send(exchange, 400, Map.of("error", e.getMessage()));
        } catch (SQLException e) {
            send(exchange, 500, Map.of("error", "Database operation failed"));
        }
    }

    private void handleDiscount(HttpExchange exchange) throws IOException {
        try {
            if (!exchange.getRequestURI().getPath().equals("/books/discount")) {
                send(exchange, 404, Map.of("error", "Route not found")); return;
            }
            if (!exchange.getRequestMethod().equals("PATCH")) {
                send(exchange, 405, Map.of("error", "Use PATCH")); return;
            }
            Map<String, String> query = queryParams(exchange.getRequestURI().getRawQuery());
            String publisher = query.get("publisher");
            if (publisher == null || publisher.isBlank()) throw new IllegalArgumentException("publisher is required");
            BigDecimal percent = FeatureRules.discount(query.get("discount"));
            books.discountPublisher(publisher, percent);
            exchange.sendResponseHeaders(204, -1);
        } catch (IllegalArgumentException e) {
            send(exchange, 400, Map.of("error", e.getMessage()));
        } catch (SQLException e) {
            send(exchange, 500, Map.of("error", "Database operation failed"));
        }
    }

    private static Map<String, String> queryParams(String raw) {
        java.util.HashMap<String, String> values = new java.util.HashMap<>();
        if (raw != null) for (String pair : raw.split("&")) {
            String[] kv = pair.split("=", 2);
            values.put(decode(kv[0]), kv.length > 1 ? decode(kv[1]) : "");
        }
        return values;
    }

    private static String decode(String text) { return URLDecoder.decode(text, StandardCharsets.UTF_8); }

    private static void send(HttpExchange exchange, int status, Object body) throws IOException {
        byte[] bytes = JSON.writeValueAsBytes(body);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (var out = exchange.getResponseBody()) { out.write(bytes); }
    }
}
