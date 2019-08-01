package enums;

import java.util.Random;

public enum PaymentStatus {
    PAID, UNPAID;

    public static PaymentStatus getRandValue() {
        PaymentStatus[] values = values();
        int index = new Random().nextInt(values.length);
        return values[index];
    }
}
