package edu.fau.cen4010.feature1;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Small input rules kept separate so their boundary cases are easy to test and explain. */
final class FeatureRules {
    private FeatureRules() { }

    static BigDecimal rating(String value) {
        BigDecimal rating = decimal(value, "rating");
        if (rating.compareTo(BigDecimal.ONE) < 0 || rating.compareTo(BigDecimal.valueOf(5)) > 0)
            throw new IllegalArgumentException("rating must be between 1 and 5");
        return rating;
    }

    static BigDecimal discount(String value) {
        BigDecimal discount = decimal(value, "discount");
        if (discount.compareTo(BigDecimal.ZERO) < 0 || discount.compareTo(BigDecimal.valueOf(100)) > 0)
            throw new IllegalArgumentException("discount must be between 0 and 100");
        return discount;
    }

    static BigDecimal discountedPrice(BigDecimal price, BigDecimal percent) {
        return price.multiply(BigDecimal.ONE.subtract(percent.movePointLeft(2)))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private static BigDecimal decimal(String value, String field) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(field + " is required");
        try { return new BigDecimal(value); }
        catch (NumberFormatException e) { throw new IllegalArgumentException(field + " must be a number"); }
    }
}
