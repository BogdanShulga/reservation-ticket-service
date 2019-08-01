package mainObjects;

import handlers.Service;
import enums.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Formatter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Setter
@Getter
public class Booking {
    private LocalDate bookedDate;
    private PaymentStatus paymentStatus;
    private Movie bookedMovie;
    private List<Place> places;
    private Cinema cinema;
    private Client client;
    private Hall hall;
    private boolean reserved;
    private LocalDateTime bookingEnd;
    private String paymentDeadlineString;
    private int sessionMinutes = 15;
    private Service service;
    private boolean confirm;


    public Booking(LocalDate bookedDate, PaymentStatus paymentStatus, Movie bookedMovie, List<Place> places, Cinema cinema, Client client, Hall hall, boolean reserved, Service service) {
        this.bookedDate = bookedDate;
        this.paymentStatus = paymentStatus;
        this.bookedMovie = bookedMovie;
        this.places = places;
        this.cinema = cinema;
        this.client = client;
        this.hall = hall;
        this.reserved = reserved;
        this.service = service;
        startTimer();
    }

    private void startTimer() {
        final Timer timer = new Timer();
        sessionMinutes = 15;
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                --sessionMinutes;
                if (sessionMinutes < 0 && !reserved) {
                    service.discardCurrentBooking();
                    System.out.println("Your booking for movie " + bookedMovie + " for the date " + bookedDate + " is discarded, because session time has elapsed");
                    timer.cancel();
                } else if (confirm) {
                    timer.cancel();
                }
            }
        }, 0, 60_000);
    }

    @Override
    public String toString() {
        double price = bookedMovie.getPrice();
        StringBuilder sits = new StringBuilder();
        for (Place p : places) {
            int pn = p.getNumber();
            sits.append(pn).append(", ");
        }
        Formatter f = new Formatter();
        f.format("| Movie name: %-64s |\n" +
                        "| show date and time: %-10s, %-44s |\n" +
                        "| cinema: %-68s |\n" +
                        "| hall: %-70s |\n" +
                        "| places: %-68s |\n" +
                        "| client name: %-63s |\n" +
                        "| reserved status: %-59s |\n" +
                        "| payment status: %-60s |\n" +
                        "| price: %-69.2f |",
                bookedMovie.getName(),
                bookedDate,
                bookedMovie.getMovieShowDuration().getFrom(),
                cinema.getName(),
                hall.getPlaceType(),
                sits.toString(),
                client.getName() + " " + client.getSurName(),
                reserved ? "reserved" : "not reserved",
                paymentStatus,
                price);

        String s1 = "________________________________________________________________________________\n";
        String s2 = "\n|______________________________________________________________________________|\n";
        System.out.println(bookedMovie.getPriceExplanation());
        return s1 + f.toString() + s2;
    }
}
