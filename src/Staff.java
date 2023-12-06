import java.sql.Time;

public class Staff extends Passenger implements RequestElevator{
    private String Job;

    public Staff(String name, String id, double weight, int age, int currentFloor, int destinationFloor, long requestTime) {
        super(name, id, weight, age);
        this.currentFloor = currentFloor;
        this.destinationFloor = destinationFloor;
        this.requestTime = requestTime;
    }

    public Staff() {
    }


    public void hasArrived(){

    }
}

