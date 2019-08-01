package enums;

import java.util.Random;

public enum MovieType {
    ORIGINAL("original"),
    TRANSLATED("translated");

    String type;

    MovieType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public static MovieType getRandValue() {
        MovieType[] values = values();
        int index = new Random().nextInt(values.length);
        return values[index];
    }
}
