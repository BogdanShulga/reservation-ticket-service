package mainObjects;

import enums.PlaceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public
class Place {
    private PlaceType placeType;
    private int rowNumber;
    private int numberInRow;
    private boolean reserved;
    private Client client;
    private int number;

    @Override
    public String toString() {
        return number + " (row=" + rowNumber + ", number in row=" + numberInRow + ")";
    }
}
