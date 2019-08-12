package handlers;

import mainObjects.Hall;
import mainObjects.Place;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

class UserInput {
    private static Scanner scanner = new Scanner(System.in);

    static String getStringClientInput() {
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

    static LocalDate getLocalDateClientInput() {
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

    static int getIntClientInput(int... needed) {
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

    static int getIntClientInput() {
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

    static List<Place> getChosenPlaces(Hall currentHall) {
        List<Place> allHallPlaces = currentHall.getPlaces();
        List<Place> chosenPlaces = new ArrayList<>();
        boolean stop = false;
        while (!stop) {
            List<Integer> places = getUserPlacesInput(currentHall);
            if (isFreePlaces(places, currentHall)) {
                places.forEach(i -> chosenPlaces.add(allHallPlaces.get(i - 1)));
                stop = true;
            } else {
                System.out.println("You enter some reserved place or places, please, try again, choose only free places!");
            }
        }
        return chosenPlaces;
    }

    private static boolean isFreePlaces(List<Integer> list, Hall currentHall) {
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

    private static List<Integer> getUserPlacesInput(Hall currentHall) {
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
}
