package enums;

import java.util.Random;

public enum PlaceType {
    NORMAL,
    PREMIUM,
    VIP;

    public static PlaceType getRandValue() {
        PlaceType[] values = values();
        int index = new Random().nextInt(values.length);
        return values[index];
    }
}
