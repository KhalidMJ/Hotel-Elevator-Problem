import java.sql.Time;

public class Staff extends Passenger{
    private String Job;

    public Staff(String name, String id, double weight, int age, long arrivalTime, int currentFloor, int destinationFloor) {
        super(name, id, weight, age, arrivalTime, currentFloor,destinationFloor);

    }

    public Staff() {
    }

}

