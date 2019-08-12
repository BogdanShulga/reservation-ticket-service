package handlers;

import enums.MovieCategory;
import enums.PaymentStatus;
import lombok.Getter;
import lombok.Setter;
import mainObjects.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class Service implements Runnable {
    private Scanner scanner;
    private int count;
    private List<Movie> multiplexMovieList;
    private List<Movie> zhovtenMovieList;
    private List<Client> clients;
    private Client currentClient;
    private LocalDate bookedDate;
    private Cinema multiplex;
    private Cinema zhovten;
    private Cinema currentCinema;
    private Map<LocalDate, List<Cinema>> dateListMap;
    private Set<Booking> bookings;
    private Booking currentBooking;
    private List<Movie> currentMovies;
    private Movie currentMovie;
    private Hall currentHall;
    List<Place> chosenPlaces;
    private int unpaidBookingsCount;

    @Override
    public void run() {
        System.out.println("Hi! You are in Reservation Ticket Service!");
        registerClient();
        askBookedDate();
    }

    public Service(Map<LocalDate, List<Cinema>> dateListMap, List<Client> clients, Set<Booking> bookings) {
        this.dateListMap = dateListMap;
        this.clients = clients;
        this.bookings = bookings;
        this.scanner = new Scanner(System.in);
    }

    private void initDateCinemas(LocalDate date, Client client) {
        if (dateListMap.containsKey(date)) {
            multiplex = dateListMap.get(date).get(0);
            zhovten = dateListMap.get(date).get(1);
            multiplexMovieList = multiplex.getMovies();
            zhovtenMovieList = zhovten.getMovies();
            bookedDate = date;
        } else {
            multiplexMovieList = ListGenerator.getMultiplexMovieList(date, client);
            zhovtenMovieList = ListGenerator.getZhovtenMovieList(date, client);
            multiplex = new Cinema("Multiplex", "Gnata Khotkevicha street, 1B, Kyiv", multiplexMovieList);
            zhovten = new Cinema("Zhovten", "Kostyantunivska street, 26, Kyiv", zhovtenMovieList);
            List<Cinema> cinemaList = new ArrayList<>();
            cinemaList.add(multiplex);
            cinemaList.add(zhovten);
            dateListMap.put(date, cinemaList);
            bookedDate = date;
        }
    }

    private void registerClient() {
        System.out.println("!!!Registration!!!");
        System.out.println("Please, enter your name:");
        String name = getStringClientInput();
        System.out.println("Please, enter your surname:");
        String surname = getStringClientInput();
        System.out.println("Please, enter your age:");
        int age = getIntClientInput();
        System.out.println("Please, enter your email:");
        String email = getStringClientInput();
        System.out.println("Please, enter your phone:");
        int phone = getIntClientInput();
        System.out.println("Please, enter 1 if you are student or 2 if not:");
        boolean student = getIntClientInput(1, 2) == 1;
        this.currentClient = new Client(name, surname, age, email, phone, student, null);
        clients.add(currentClient);
        System.out.println("Your client profile:");
        System.out.println(this.currentClient);
    }

    private void askBookedDate() {
        System.out.println("Starting movie selecting!");
        System.out.println(currentClient.getName() + ", please, enter 1 to choose date or 2 - to view all your bookings or 3 to exit:");
        int anInt = getIntClientInput(1, 2, 3);
        if (anInt == 1) {
            System.out.println(currentClient.getName() + ", please, enter a date when you want go to the cinema (for example: \"" + LocalDate.now() + "\"):");
            bookedDate = getLocalDateClientInput();
            initDateCinemas(bookedDate, currentClient);
            selectCinema();
            chooseAction();
        } else if (anInt == 2) {
            viewBookings();
            askBookedDate();
        } else if (anInt == 3) {
            System.out.println("Exiting!");
        }
    }

    private void selectCinema() {
        System.out.println(currentClient.getName() + ", please, select a cinema: enter 1 to choose Multiplex or 2 to choose Zhovten or 3 to view all your bookings or 4 to exit or 0 to go to choosing date menu:");
        int anInt = getIntClientInput(0, 1, 2, 3, 4);
        if (anInt == 1) {
            System.out.println("You chose cinema Multiplex");
            currentCinema = multiplex;
            currentMovies = multiplexMovieList;
        } else if (anInt == 2) {
            System.out.println("You chose cinema Zhovten");
            currentCinema = zhovten;
            currentMovies = zhovtenMovieList;
        } else if (anInt == 3) {
            viewBookings();
        } else if (anInt == 0) {
            askBookedDate();
        } else {
            System.out.println("Exiting!");
        }
    }

    private void chooseAction() {
        System.out.println(currentClient.getName() + ", please, enter the number of operation you want to do:");
        System.out.println(
                "1 - to choose movie from a list of all the movies in " + currentCinema.getName() + " cinema for " + bookedDate + "\n" +
                        "2 - to search and choose movie by category in " + currentCinema.getName() + " cinema for " + bookedDate + "\n" +
                        "3 - to search and choose movie by rating in " + currentCinema.getName() + " cinema for " + bookedDate + "\n" +
                        "4 - to view all your bookings\n" +
                        "0 - to go to choosing date menu:");
        int anInt = getIntClientInput(0, 1, 2, 3, 4);
        if (anInt == 1) {
            printMovieList(currentMovies);
            chooseMovie(currentMovies);
        } else if (anInt == 2) {
            searchByMovieCategory(currentMovies);
        } else if (anInt == 3) {
            searchByMovieRating(currentMovies);
        } else if (anInt == 4) {
            viewBookings();
            chooseAction();
        } else if (anInt == 0) {
            askBookedDate();
        }
    }

    private boolean isTwoUnpaidBookingsPresent() {
        unpaidBookingsCount = 0;
        boolean yes = false;
        bookings.forEach(booking -> {
            if (booking.isReserved()) unpaidBookingsCount++;
        });
        if (unpaidBookingsCount >= 2) yes = true;
        return yes;
    }

    private void booking() {
        System.out.println(currentClient.getName() + ", you started an order for booking tickets for movie " + currentMovie.getName() + " for date " + bookedDate);
        System.out.println("You have 15 minutes to complete this order!");

        currentBooking = new Booking.BookingBuilder().bookedDate(bookedDate).paymentStatus(PaymentStatus.UNPAID)
                .bookedMovie(currentMovie).places(null).cinema(currentCinema).client(currentClient)
                .hall(null).reserved(false).service(this).build();

        currentBooking.startTimer();
        chooseHall();
        choosePlaces();
        currentBooking.setPlaces(chosenPlaces);
        currentBooking.setHall(currentHall);
        currentBooking.setConfirm(getClientBookingConfirmation());
        System.out.println("Your ticket booking order:");
        System.out.println(currentBooking);
        askBookedDate();
    }

    private boolean getClientBookingConfirmation() {
        boolean confirm = false;
        System.out.println("Time to the end of your ticket booking session: " + currentBooking.getSessionMinutes() + " minutes ");
        System.out.println(currentClient.getName() + ", please, enter 1 - to confirm your ticket booking or 2 - to discard it or 3 to go to choosing date menu:");
        int anInt = getIntClientInput(1, 2, 3);
        if (anInt == 1) {
            currentBooking.setReserved(true);
            bookings.add(currentBooking);
            System.out.println("Your booking for movie " + currentMovie.getName() + " for the date " + bookedDate + " is confirmed");
            setPaymentDeadline();
            confirm = true;
        } else if (anInt == 2) {
            discardCurrentBooking();
            confirm = true;
        } else {
            System.out.println("You don't confirm booking for movie " + currentMovie.getName());
            System.out.println("When 15 minutes expire, the session of this order will be closed!");
        }
        return confirm;
    }

    private void setPaymentDeadline() {
        currentBooking.setBookingEnd(LocalDateTime.of(bookedDate, currentMovie.getMovieShowDuration().getFrom()));
        if (bookedDate.equals(LocalDate.now())) {
            currentBooking.setPaymentDeadlineString("You must pay for your ticket booking today! before " + currentMovie.getMovieShowDuration().getFrom() + "!");
        } else {
            currentBooking.setPaymentDeadlineString("You must pay for your ticket booking before " + bookedDate + " " + currentMovie.getMovieShowDuration().getFrom() + "!");
        }
    }

    void discardCurrentBooking() {
        chosenPlaces.forEach(place -> currentHall.getPlaces().get(place.getNumber()).setReserved(false));
        bookings.remove(currentBooking);
    }

    private void choosePlaces() {
        System.out.println(currentClient.getName() + ", please, choose one or more places separate them with space (for example: \"1 5 3\")");
        System.out.println("\'O\' means that place is free, \'X\' - place is reserved. You can to choose only free places!");
        currentHall.printHall();
        chosenPlaces = getChosenPlaces();
        System.out.println("You have chosen next places: ");
        for (Place place : chosenPlaces) {
            place.setReserved(true);
            place.setClient(currentClient);
            System.out.println("place # " + place.getNumber() + " in row #" + place.getRowNumber() + " number in row is " + place.getNumberInRow());
        }
    }

    private void chooseHall() {
        System.out.println(currentClient.getName() + ", please, choose a hall in " + currentCinema.getName() + " cinema:\n" +
                "1 - to choose normal hall or " +
                "2 - to choose premium hall or " +
                "3 - to choose VIP hall");
        int anInt = getIntClientInput(1, 2, 3);
        switch (anInt) {
            case 1:
                currentHall = currentCinema.getNormalHall();
                break;
            case 2:
                currentHall = currentCinema.getPremiumHall();
                break;
            case 3:
                currentHall = currentCinema.getVipHall();
                break;
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("You chose " + currentHall.getPlaceType() + " hall");
    }

    private List<Place> getChosenPlaces() {
        List<Place> allHallPlaces = currentHall.getPlaces();
        List<Place> chosenPlaces = new ArrayList<>();
        boolean stop = false;
        while (!stop) {
            List<Integer> places = getUserPlacesInput();
            if (isFreePlaces(places)) {
                places.forEach(i -> chosenPlaces.add(allHallPlaces.get(i - 1)));
                stop = true;
            } else {
                System.out.println("You enter some reserved place or places, please, try again, choose only free places!");
            }
        }
        return chosenPlaces;
    }

    private boolean isFreePlaces(List<Integer> list) {
        List<Place> places = currentHall.getPlaces();
        boolean free = true;
        for (Integer i : list) {
            if (places.get(i - 1).isReserved()) {
                free = false;
                break;
            }
        }
        return free;
    }

    private void viewBookings() {
        if (bookings.isEmpty()) {
            System.out.println("You have no bookings yet!");
        } else {
            int i = 0;
            for (Booking b : bookings) {
                System.out.println("Booking #" + ++i);
                System.out.println(b);
            }
        }
    }

    private void searchByMovieCategory(List<Movie> movies) {
        Map<MovieCategory, List<Movie>> movieCategoryListMap = movies.stream()
                .collect(Collectors.groupingBy(Movie::getMovieCategory));
        System.out.println(currentClient.getName() + ", please, choose movie category\n");
        int count = 0;
        List<MovieCategory> movieCategories = new ArrayList<>();
        for (MovieCategory c : movieCategoryListMap.keySet()) {
            System.out.println(++count + " - to see " + c + "s");
            movieCategories.add(c);
        }
        int[] needed = new int[count];
        for (int i = 0; i < count; i++) needed[i] = i;
        int anInt = getIntClientInput(needed);
        movies = movieCategoryListMap.get(movieCategories.get(anInt - 1));
        printMovieList(movies);
        chooseMovie(movies);
    }

    private void searchByMovieRating(List<Movie> movies) {
        movies = movies.stream()
                .sorted(Comparator.comparingInt(m -> m.getMovieRating().ordinal()))
                .collect(Collectors.toList());
        printMovieList(movies);
        chooseMovie(movies);
    }


    private void printMovieList(List<Movie> movieList) {
        this.count = 0;
        System.out.println("________________________________________________________________"
                + "______________________________________________________________________"
                + "_______________________________");
        String had = new Formatter().format("| #  | %-35s | %-13s "
                        + "| %-10s | %-5s | %-12s "
                        + "| %-11s | %-20s | %-10s "
                        + "| %-6s |",
                "name", "movie category",
                "movie type", "movie rating", "movie status",
                "place type", "duration of movie show", "date",
                "price")
                .toString();
        System.out.println(had);
        System.out.println("|____|_____________________________________|________________|___"
                + "_________|______________|______________|_____________|________________"
                + "________|____________|________|");
        for (Movie movie : movieList) {
            String number = new Formatter().format("| %-2d ", ++this.count).toString();
            System.out.println(number + movie);
        }
        System.out.println("|____|_____________________________________|________________|___"
                + "_________|______________|______________|_____________|________________"
                + "________|____________|________|");
    }

    private void chooseMovie(List<Movie> movieList) {
        System.out.println("Please, enter number of movie you want to reserved  from the list above or 0 to go to preview menu:");
        int[] needed = new int[movieList.size() + 1];
        for (int i = 0; i < movieList.size() + 1; i++) needed[i] = i;
        int anInt = getIntClientInput(needed);
        if (anInt == 0) {
            selectCinema();
        } else {
            Movie chosenMovie = movieList.get(anInt - 1);
            System.out.println("You chose movie number " + anInt + " " + chosenMovie.getName());
            System.out.println("If you want to bay ticket or tickets for this movie enter 1 or 2 if you want to choose another movie");
            anInt = getIntClientInput(1, 2);
            if (anInt == 1) {
                currentMovie = chosenMovie;
                if (isTwoUnpaidBookingsPresent()) {
                    System.out.println(currentClient.getName() + "! Sorry! But you can't make another booking, because you already have 2 unpaid ticket bookings!\n" +
                            "Please, pay them and then you can do more ticket bookings!");
                    askBookedDate();
                } else {
                    booking();
                }
            } else if (anInt == 2) {
                chooseMovie(movieList);
            }
        }
    }

    private List<Integer> getUserPlacesInput() {
        final List<Integer> intPlaces = new ArrayList<>();
        List<String> places;
        boolean stop = false;
        while (!stop) {
            String place = getStringClientInput();
            places = new ArrayList<>(Arrays.asList(place.trim().split(" ")));
            try {
                places.forEach(s -> intPlaces.add(Integer.parseInt(s.trim())));
            } catch (NumberFormatException ex) {
                System.out.println("You enter incorrect data! Please try again!");
                continue;
            }
            stop = true;
            int n = currentHall.getPlaces().size();
            for (Integer i : intPlaces) {
                if (i < 1 || i > n) {
                    System.out.println("You enter incorrect data! Please try again!");
                    stop = false;
                    break;
                }
            }
        }
        return intPlaces;
    }

    private String getStringClientInput() {
        String string = "";
        boolean stop = false;
        while (!stop) {
            try {
                string = scanner.nextLine().trim();
            } catch (IllegalStateException | NoSuchElementException ex) {
                System.out.println("You enter incorrect data! Please try again!");
                continue;
            }
            stop = true;
        }
        return string;
    }

    private LocalDate getLocalDateClientInput() {
        LocalDate localDate = LocalDate.now();
        boolean stop = false;
        while (!stop) {
            try {
                localDate = LocalDate.parse(getStringClientInput());
            } catch (IllegalStateException | NoSuchElementException | DateTimeParseException ex) {
                System.out.println("You enter incorrect data! Please try again!");
                continue;
            }
            if (localDate.isBefore(LocalDate.now())) {
                System.out.println("You enter incorrect data, your date is before now! Please try again!");
                continue;
            }
            stop = true;
        }
        return localDate;
    }

    private int getIntClientInput(int... needed) {
        int anInt = 0;
        boolean stop = false;
        while (!stop) {
            try {
                anInt = Integer.parseInt(scanner.nextLine());
            } catch (IllegalStateException | NoSuchElementException | NumberFormatException ex) {
                System.out.println("You enter incorrect data! Please try again!");
                scanner.nextLine();
                continue;
            }
            boolean good = true;
            for (int i : needed) {
                if (i == anInt) {
                    good = false;
                    break;
                }
            }
            if (!good) {
                stop = true;
            } else {
                System.out.println("You enter incorrect data! Please try again!!!!!!!!");
            }
        }
        return anInt;
    }

    private int getIntClientInput() {
        int anInt = 0;
        boolean stop = false;
        while (!stop) {
            try {
                anInt = Integer.parseInt(scanner.nextLine());
            } catch (IllegalStateException | NoSuchElementException | NumberFormatException ex) {
                System.out.println("You enter incorrect data! Please try again!");
                continue;
            }
            stop = true;
        }
        return anInt;
    }
}
