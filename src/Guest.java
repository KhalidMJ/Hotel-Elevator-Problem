/**
 * Represents a guest, extending the functionality of the Passenger class.
 * @author Fares
 */
public class Guest extends Passenger{

    /**
     * Constructs a new Guest instance with specified attributes.
     *
     * @param name              The name of the guest.
     * @param id                The ID of the guest.
     * @param weight            The weight of the guest in kilograms.
     * @param age               The age of the guest.
     * @param arrivalTime       The time when the guest arrived.
     * @param currentFloor      The current floor where the guest is located.
     * @param destinationFloor  The destination floor where the guest wants to go.
     */
    public Guest(String name, String id, double weight, int age, long arrivalTime, int currentFloor, int destinationFloor) {
        super(name, id, weight, age, arrivalTime, currentFloor,destinationFloor);
    }

    /**
     * Constructs an anonymous guest with default values.
     */
    public Guest() {
        super();
    }
}