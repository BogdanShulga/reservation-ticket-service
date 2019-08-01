package enums;

import java.util.Random;

public enum MovieCategory {
    DRAMA,
    ACTION,
    HORROR,
    THRILLER,
    COMEDY,
//    ROMANCE,
//    SCI_FI,
//    MYSTERY,
    FAMILY;
//    WESTERN,
//    DOCUMENTARIES;

    public static MovieCategory getRandValue() {
        MovieCategory[] values = values();
        int index = new Random().nextInt(values.length);
        return values[index];
    }
}
