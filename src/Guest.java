import java.sql.Time;

public class Guest extends Passenger implements RequestElevator{
    private int currentFloor;
    private int destinationFloor;
    private long requestTime;
    private long arriveTime;

    public Guest(String name, String id, double weight, int age, int currentFloor, int destinationFloor, long requestTime) {
        super(name, id, weight, age);
        this.currentFloor = currentFloor;
        this.destinationFloor = destinationFloor;
        this.requestTime = requestTime;
    }

    public Guest(int currentFloor, int destinationFloor){
        super();
        this.currentFloor = currentFloor;
        this.destinationFloor = destinationFloor;
    }

    public Guest() {
        super();
    }

    public void requestUp() {
        this.destinationFloor = this.currentFloor + 1;
    }

    public void requestDown() {
        this.destinationFloor = this.currentFloor - 1;
    }

    public int currentFloor() {
        return this.currentFloor;
    }

    public int destinationFloor() {
        return this.destinationFloor;
    }
}