public class Staff extends Passenger{
    private String Job;

    // Constructor
    public Staff(String name, String id, double weight, int age, long arrivalTime, Floor currentFloor, Floor destinationFloor) {
        super(name, id, weight, age, arrivalTime, currentFloor,destinationFloor);
    }

    // Default Constructor
    public Staff() {
        super();
    }
}