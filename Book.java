package edu.fau.cen4010.feature1;

import java.math.BigDecimal;

/** Fields required by the book record described in TeamProject.pdf. */
public record Book(String isbn, String bookName, String description, BigDecimal price,
                   String author, String genre, String publisher, int yearPublished,
                   long copiesSold, BigDecimal averageRating) { }
