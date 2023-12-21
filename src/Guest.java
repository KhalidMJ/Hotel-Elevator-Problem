public class Guest extends Passenger{

    // Constructor
    public Guest(String name, String id, double weight, int age, long arrivalTime, Floor currentFloor, Floor destinationFloor) {
        super(name, id, weight, age, arrivalTime, currentFloor,destinationFloor);
    }

    // Default Constructor
    public Guest() {
        super();
    }
}