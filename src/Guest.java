import java.sql.Time;

public class Guest extends Passenger implements RequestElevator{

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
    public void hasArrived(){


    }


}

