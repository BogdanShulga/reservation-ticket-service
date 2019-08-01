package enums;

import java.util.Random;

public enum MovieType {
    ORIGINAL,
    TRANSLATED;

    public static MovieType getRandValue() {
        MovieType[] values = values();
        int index = new Random().nextInt(values.length);
        return values[index];
    }
}
