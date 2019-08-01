package mainObjects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalTime;

@AllArgsConstructor
@Setter
@Getter
public class MovieShowDuration {
    private LocalTime from;
    private LocalTime to;

    public String getDuration() {
        return Duration.between(from, to)
                .toString()
                .substring(2)
                .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                .toLowerCase();
    }

    @Override
    public String toString() {
        return "from " + getFrom() + " to " + getTo();
    }
}
