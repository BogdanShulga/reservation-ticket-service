package enums;

import java.util.Random;

public enum MovieStatus {
    PRE_PREMIERE,
    PREMIERE,
    AVAILABLE;

    public static MovieStatus getRandValue() {
        MovieStatus[] values = values();
        int index = new Random().nextInt(values.length);
        return values[index];
    }
}
