import java.sql.Time;

public class Guest extends Passenger {
    private int currentFloor;
    private int destinationFloor;
    private Time requestTime;
    private Time arriveTime;

    public Guest() {
    }

    public void requestUp() {
        this.destinationFloor = this.currentFloor + 1;
    }

    public void requestDown() {
        this.destinationFloor = this.currentFloor - 1;
    }



    public String getName() {
        return getName();
    }
    public int getId() {
        return getId();
    }

    public double getWeight() {
        return getWeight();
    }}