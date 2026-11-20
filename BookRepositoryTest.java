public class BookRepositoryTest {

    public static void main(String[] args) {

        BookRepository repository = new BookRepository();

        System.out.println("TEST 1: Books by genre");
        System.out.println(
                repository.findByGenre("Programming"));

        System.out.println();

        System.out.println("TEST 2: Top 10 sellers");
        System.out.println(
                repository.findTop10Sellers());

        System.out.println();

        System.out.println("TEST 3: Rating 4.5 or higher");
        System.out.println(
                repository.findByMinimumRating(4.5));

        System.out.println();

        System.out.println("TEST 4: 20% TechBooks discount");
        int updated =
                repository.discountByPublisher(
                        "TechBooks", 20);

        System.out.println(
                "Books updated: " + updated);

        System.out.println();
        System.out.println("All tests completed.");
    }
}
