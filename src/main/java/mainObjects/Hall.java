package mainObjects;

import enums.PlaceType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter
public class Hall {
    private int rows;
    private int sitsInRow;
    private int placeAmount;
    private List<Place> places = new ArrayList<>();
    private volatile PlaceType placeType;
    private List<Movie> movies;

    Hall(int rows, int sitsInRow, PlaceType placeType, List<Movie> movies) {
        this.rows = rows;
        this.sitsInRow = sitsInRow;
        this.placeType = placeType;
        this.movies = movies;
        this.placeAmount = rows * sitsInRow;
        initPlaces();
    }

    private void initPlaces() {
        Random random = new Random();
        int number = 0;
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= sitsInRow; j++) {
                places.add(new Place(placeType, i, j, random.nextBoolean(), null, ++number));
            }
        }
    }

    public void printHall() {
        char[] sits = new char[]{'O', 'X'};
        int number = 0;
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= sitsInRow; j++) {
                ++number;
                char reserved = places.get(number - 1).isReserved() ? sits[1] : sits[0];
                System.out.printf("  %3d=%1c  ", number, reserved);
            }
            System.out.println();
        }
    }
}
