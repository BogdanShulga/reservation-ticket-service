package handlers;

import enums.*;
import mainObjects.Client;
import mainObjects.Movie;
import mainObjects.MovieShowDuration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

class ListGenerator {
    private static final double AFTER_PREMIERE_DISCOUNT = 15.0;
    private static final double STUDENT_DISCOUNT = 10.0;
    private static final double ON_DAY_DISCOUNT = 7.0;
    private static final double NORMAL_PRICE = 100;
    private static final double PREMIUM_PRICE = 140;
    private static final double VIP_PRICE = 180;

    static List<Movie> getMultiplexMovieList(LocalDate date, Client client) {
        List<Movie> movieList = new ArrayList<>();
        movieList.add(new Movie("LIZ THE ELIZABETH TAYLOR STORY", PlaceType.getRandValue(), MovieType.getRandValue(), MovieStatus.getRandValue(), new MovieShowDuration(LocalTime.of(10, 0), LocalTime.of(11, 45)), date, MovieCategory.DRAMA, MovieRating.getRandValue(), 0.0, ""));
        movieList.add(new Movie("DAYS OF THUNDER", PlaceType.getRandValue(), MovieType.getRandValue(), MovieStatus.getRandValue(), new MovieShowDuration(LocalTime.of(12, 30), LocalTime.of(14, 15)), date, MovieCategory.ACTION, MovieRating.getRandValue(), 0.0, ""));
        movieList.add(new Movie("MARABUNTA", PlaceType.getRandValue(), MovieType.getRandValue(), MovieStatus.getRandValue(), new MovieShowDuration(LocalTime.of(15, 0), LocalTime.of(16, 45)), date, MovieCategory.HORROR, MovieRating.getRandValue(), 0.0, ""));
        movieList.add(new Movie("ON THE BORDERLINE", PlaceType.getRandValue(), MovieType.getRandValue(), MovieStatus.getRandValue(), new MovieShowDuration(LocalTime.of(17, 30), LocalTime.of(19, 15)), date, MovieCategory.THRILLER, MovieRating.getRandValue(), 0.0, ""));
        movieList.add(new Movie("RUNAWAY HEARTS", PlaceType.getRandValue(), MovieType.getRandValue(), MovieStatus.getRandValue(), new MovieShowDuration(LocalTime.of(20, 30), LocalTime.of(22, 15)), date, MovieCategory.FAMILY, MovieRating.getRandValue(), 0.0, ""));
        setMoviesPrices(movieList, client, date);
        return movieList;
    }

    static List<Movie> getZhovtenMovieList(LocalDate date, Client client) {
        List<Movie> movieList = new ArrayList<>();
        movieList.add(new Movie("BARN RED", PlaceType.getRandValue(), MovieType.getRandValue(), MovieStatus.getRandValue(), new MovieShowDuration(LocalTime.of(7, 0), LocalTime.of(8, 45)), date, MovieCategory.FAMILY, MovieRating.getRandValue(), 0.0, ""));
        movieList.add(new Movie("CONGO", PlaceType.getRandValue(), MovieType.getRandValue(), MovieStatus.getRandValue(), new MovieShowDuration(LocalTime.of(9, 30), LocalTime.of(11, 15)), date, MovieCategory.ACTION, MovieRating.getRandValue(), 0.0, ""));
        movieList.add(new Movie("THIS IS MY YEAR", PlaceType.getRandValue(), MovieType.getRandValue(), MovieStatus.getRandValue(), new MovieShowDuration(LocalTime.of(12, 0), LocalTime.of(13, 45)), date, MovieCategory.DRAMA, MovieRating.getRandValue(), 0.0, ""));
        movieList.add(new Movie("JEFF DUNHAM - CONTROLLED CHAOS", PlaceType.getRandValue(), MovieType.getRandValue(), MovieStatus.getRandValue(), new MovieShowDuration(LocalTime.of(14, 30), LocalTime.of(16, 15)), date, MovieCategory.COMEDY, MovieRating.getRandValue(), 0.0, ""));
        movieList.add(new Movie("WHAT THE WATERS LEFT BEHIND", PlaceType.getRandValue(), MovieType.getRandValue(), MovieStatus.getRandValue(), new MovieShowDuration(LocalTime.of(17, 30), LocalTime.of(19, 15)), date, MovieCategory.HORROR, MovieRating.getRandValue(), 0.0, ""));
        movieList.add(new Movie("ROB THE MOB", PlaceType.getRandValue(), MovieType.getRandValue(), MovieStatus.getRandValue(), new MovieShowDuration(LocalTime.of(20, 0), LocalTime.of(21, 45)), date, MovieCategory.ACTION, MovieRating.getRandValue(), 0.0, ""));
        movieList.add(new Movie("STROSZEK", PlaceType.getRandValue(), MovieType.getRandValue(), MovieStatus.getRandValue(), new MovieShowDuration(LocalTime.of(22, 0), LocalTime.of(23, 45)), date, MovieCategory.COMEDY, MovieRating.getRandValue(), 0.0, ""));
        setMoviesPrices(movieList, client, date);
        return movieList;
    }

    private static void setMoviesPrices(List<Movie> movies, Client client, LocalDate date) {
        movies.forEach(movie -> movie.setPrice(getPrice(movie, client, date)));
    }

    private static double getDiscount(Movie movie, Client client, LocalDate date) {
        double discount;
        if (movie.getMovieStatus().equals(MovieStatus.AVAILABLE)
                && client.isStudent()
                && date.equals(LocalDate.now())) {
            discount = AFTER_PREMIERE_DISCOUNT + STUDENT_DISCOUNT + ON_DAY_DISCOUNT;
            movie.setPriceExplanation("discount: for student - " + STUDENT_DISCOUNT
                    + "%, after premier - " + AFTER_PREMIERE_DISCOUNT
                    + "%, on day of movie - " + ON_DAY_DISCOUNT + "%, " +
                    "total discount - " + discount + "%");
        } else if (movie.getMovieStatus().equals(MovieStatus.AVAILABLE)
                && !client.isStudent()
                && date.equals(LocalDate.now())) {
            discount = AFTER_PREMIERE_DISCOUNT + ON_DAY_DISCOUNT;
            movie.setPriceExplanation("discount: "
                    + "after premier - " + AFTER_PREMIERE_DISCOUNT
                    + "%, on day of movie - " + ON_DAY_DISCOUNT + "%, " +
                    "total discount - " + discount + "%");
        } else if (movie.getMovieStatus().equals(MovieStatus.AVAILABLE)
                && client.isStudent()
                && !date.equals(LocalDate.now())) {
            discount = AFTER_PREMIERE_DISCOUNT + STUDENT_DISCOUNT;
            movie.setPriceExplanation("discount: for student - " + STUDENT_DISCOUNT
                    + "%, after premier - " + AFTER_PREMIERE_DISCOUNT + "%, " +
                    "total discount - " + discount + "%");
        } else if (movie.getMovieStatus().equals(MovieStatus.AVAILABLE)
                && !client.isStudent()
                && !date.equals(LocalDate.now())) {
            discount = AFTER_PREMIERE_DISCOUNT;
            movie.setPriceExplanation("discount: "
                    + " after premier - " + AFTER_PREMIERE_DISCOUNT + "%");
        } else {
            discount = 0.0;
            movie.setPriceExplanation("discount: 0%");
        }
        return discount;
    }

    private static double getPrice(Movie movie, Client client, LocalDate date) {
        double price = 0.0;
        double discount = 1 - getDiscount(movie, client, date) / 100;
        PlaceType placeType = movie.getPlaceType();
        switch (placeType) {
            case NORMAL:
                price = NORMAL_PRICE * discount;
                movie.setPriceExplanation("PRICE FOR MOVIE: " + price + "grn." + ", original price - " + NORMAL_PRICE + "grn., " + movie.getPriceExplanation());
                break;
            case PREMIUM:
                price = PREMIUM_PRICE * discount;
                movie.setPriceExplanation("PRICE FOR MOVIE: " + price + "grn." + ", original price - " + PREMIUM_PRICE + "grn., " + movie.getPriceExplanation());
                break;
            case VIP:
                price = VIP_PRICE * discount;
                movie.setPriceExplanation("PRICE FOR MOVIE: " + price + "grn." + ", original price - " + VIP_PRICE + "grn., " + movie.getPriceExplanation());
                break;
        }
        return price;
    }
}
