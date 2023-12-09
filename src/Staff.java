import java.sql.Time;

public class Staff extends Passenger implements RequestElevator{
    private String Job;

    public Staff(String name, String id, double weight, int age, int currentFloor, int destinationFloor, long arrivalTime) {
        super(name, id, weight, age , currentFloor,destinationFloor,arrivalTime);

    }

    public Staff() {
    }


    public void hasArrived(){

    }

    @Override
    public void requestUp() {

    }

    @Override
    public void requestDown() {

    }
}

