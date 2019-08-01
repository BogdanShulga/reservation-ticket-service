package mainObjects;


import enums.PlaceType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Setter
@Getter
public class Cinema {
    private String name;
    private String address;
    private List<Movie> movies;
    private Hall normalHall;
    private Hall premiumHall;
    private Hall vipHall;

    public Cinema(String name, String address, List<Movie> movies) {
        this.name = name;
        this.address = address;
        this.movies = movies;
        initHalls(movies);
    }

    private void initHalls(List<Movie> movies) {
        getMoviesByPlaceType(movies).forEach((key, value) -> {
            switch (key) {
                case VIP:
                    vipHall = new Hall(3, 10, PlaceType.VIP, value);
                    break;
                case NORMAL:
                    normalHall = new Hall(10, 15, PlaceType.NORMAL, value);
                    break;
                case PREMIUM:
                    premiumHall = new Hall(5, 15, PlaceType.PREMIUM, value);
                    break;
            }
        });
    }

    private Map<PlaceType, List<Movie>> getMoviesByPlaceType(List<Movie> movieList) {
        return movieList.stream()
                .collect(Collectors
                        .groupingBy(Movie::getPlaceType));
    }
}
