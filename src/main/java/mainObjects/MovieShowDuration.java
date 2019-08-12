package mainObjects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@AllArgsConstructor
@Setter
@Getter
public class MovieShowDuration {
    private LocalTime from;
    private LocalTime to;

    @Override
    public String toString() {
        return "from " + getFrom() + " to " + getTo();
    }
}
