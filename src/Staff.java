/**
 * Represents a staff member, extending the functionality of the Passenger class.
 * @author Fares
 */
public class Staff extends Passenger{
    private String Job;
    public Staff(String name, String id, double weight, int age, long arrivalTime, int currentFloor, int destinationFloor) {
        super(name, id, weight, age, arrivalTime, currentFloor,destinationFloor);
    }

    /**
     * Constructs an anonymous staff member with default values.
     */
    public Staff() {
    }
}