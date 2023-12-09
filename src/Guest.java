import java.sql.Time;

public class Guest extends Passenger implements RequestElevator{

    public Guest(String name, String id, double weight, int age, long arrivalTime, int currentFloor, int destinationFloor) {
        super(name, id, weight, age, arrivalTime, currentFloor,destinationFloor);

    }

    public Guest(int currentFloor, int destinationFloor){
        super();
        this.currentFloor = currentFloor;
        this.destinationFloor = destinationFloor;
    }

    public Guest() {
        super();
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

