package enums;

import java.util.Random;

public enum MovieRating {
    ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN;

    public static MovieRating getRandValue() {
        MovieRating[] values = values();
        int index = new Random().nextInt(values.length);
        return values[index];
    }
}
