import java.sql.Time;

public class Guest extends Passenger implements RequestElevator{

    public Guest(String name, String id, double weight, int age, int currentFloor, int destinationFloor, long arrivalTime ) {
        super(name, id, weight, age,currentFloor,destinationFloor,arrivalTime);

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

