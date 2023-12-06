import java.sql.Time;

public class Staff extends Passenger {
    private int currentFloor;
    private int destinationFloor;
    private long requestTime;
    private Time arriveTime;
    private String Job;

    public Staff(String name, String id, double weight, int age, int currentFloor, int destinationFloor, long requestTime) {
        super(name, id, weight, age);
        this.currentFloor = currentFloor;
        this.destinationFloor = destinationFloor;
        this.requestTime = requestTime;
    }

    public Staff() {
    }

    public void requestUp() {

        this.destinationFloor = this.currentFloor + 1;
    }

    public void requestDown() {

        this.destinationFloor = this.currentFloor - 1;
    }
}

