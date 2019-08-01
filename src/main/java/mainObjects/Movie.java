package mainObjects;

import enums.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Formatter;

@Getter
@Setter
@AllArgsConstructor
public class Movie {
    private String name;
    private PlaceType placeType;
    private MovieType movieType;
    private MovieStatus movieStatus;
    private MovieShowDuration movieShowDuration;
    private LocalDate date;
    private MovieCategory movieCategory;
    private MovieRating movieRating;
    private double price;
    private String priceExplanation;

    @Override
    public String toString() {
        return new Formatter().format("| %-35s | %-14s "
                        + "| %-10s | %-12s | %-12s "
                        + "| %-11s | %-22s | %-10s "
                        + "| %-6.2f |",
                name, movieCategory,
                movieType, movieRating, movieStatus,
                placeType, movieShowDuration, date,
                price)
                .toString();
    }
}
