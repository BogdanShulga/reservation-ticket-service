package mainObjects;

import handlers.Booking;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class Client {
    private String name;
    private String surName;
    private int age;
    private String email;
    private int phone;
    private boolean student;
    private List<Booking> bookings;

    @Override
    public String toString() {
        return "name='" + name + '\'' +
                ", surName='" + surName + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", phone=" + phone +
                ", student=" + student;
    }
}
