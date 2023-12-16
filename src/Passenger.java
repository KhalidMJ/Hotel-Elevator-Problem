/**
 * Represents a passenger in the elevator system.
 * @author Fares
 */
public class Passenger{
    /**
     * The name of the passenger.
     */
    private final String name;

    /**
     * The ID of the passenger.
     */
    private final String id;

    /**
     * The weight of the passenger in kilograms.
     */
    private final double weight;

    /**
     * The age of the passenger.
     */
    private final int age;

    /**
     * The current floor where the passenger is located.
     */
    private int currentFloor;

    /**
     * The destination floor where the passenger wants to go.
     */
    private int destinationFloor;

    /**
     * The time when the passenger arrived.
     */
    private long arrivalTime;

    /**
     * The time when the passenger departed.
     */
    private long departureTime;

    /**
     * The total waiting time of the passenger.
     */
    private long waitingTime;

    /**
     * Constructs a new Passenger instance with specified attributes.
     *
     * @param name              The name of the passenger.
     * @param id                The ID of the passenger.
     * @param weight            The weight of the passenger in kilograms.
     * @param age               The age of the passenger.
     * @param arrivalTime       The time when the passenger arrived.
     * @param currentFloor      The current floor where the passenger is located.
     * @param destinationFloor  The destination floor where the passenger wants to go.
     */
    protected Passenger(String name, String id, double weight, int age, long arrivalTime, int currentFloor, int destinationFloor) {
        this.name = name;
        this.id = id;
        this.weight = weight;
        this.age = age;
        this.currentFloor = currentFloor;
        this.destinationFloor = destinationFloor;
        this.arrivalTime = arrivalTime;

    }

    /**
     * Constructs an anonymous guest passenger with default values.
     */
    protected Passenger() {
        this.name = "Anonymous  guest";
        this.id = "11111";
        this.weight = 70;
        this.age = 25;
    }

    /**
     * Simulates the passenger entering the elevator.
     */
    public void enterElevator() {
        Simulation.delay(2);
    }

    /**
     * Simulates the passenger exiting the elevator, calculates waiting time, and updates results.
     */
    public void exitElevator() {
        Simulation.delay(2);
        departureTime = Simulation.getElapsedTime();
        waitingTime = departureTime - this.arrivalTime;
        Simulation.addWaitingTime(this.waitingTime);
        ResultsTablePane.updateTable(this);
    }

    /**
     * Requests an elevator to go up and records the arrival time.
     *
     * @param callButtons The call buttons to request an elevator.
     */
    public void requestUp(CallButtons callButtons) {
        arrivalTime = Simulation.getElapsedTime();
        callButtons.requestUp();
    }

    /**
     * Requests an elevator to go down and records the arrival time.
     *
     * @param callButtons The call buttons to request an elevator.
     */
    public void requestDown(CallButtons callButtons) {
        arrivalTime = Simulation.getElapsedTime();
        callButtons.requestDown();
    }

    // Setters and Getters ---------------------------------------------
    /**
     * Gets the name of the passenger.
     *
     * @return The name of the passenger.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the ID of the passenger.
     *
     * @return The ID of the passenger.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Gets the weight of the passenger in kilograms.
     *
     * @return The weight of the passenger.
     */
    public double getWeight() {
        return this.weight;
    }

    /**
     * Gets the age of the passenger.
     *
     * @return The age of the passenger.
     */
    public int getAge() {
        return this.age;
    }

    /**
     * Gets the current floor where the passenger is located.
     *
     * @return The current floor of the passenger.
     */
    public int getCurrentFloor() {
        return this.currentFloor;
    }

    /**
     * Gets the destination floor where the passenger wants to go.
     *
     * @return The destination floor of the passenger.
     */
    public int getDestinationFloor() {
        return this.destinationFloor;
    }

    /**
     * Gets the time when the passenger arrived.
     *
     * @return The arrival time of the passenger.
     */
    public long getArrivalTime() {
        return this.arrivalTime;
    }

    /**
     * Gets the time when the passenger departed.
     *
     * @return The departure time of the passenger.
     */
    public long getDepartureTime() {
        return this.departureTime;
    }

    /**
     * Gets the total waiting time of the passenger.
     *
     * @return The waiting time of the passenger.
     */
    public long getWaitingTime() {
        return this.waitingTime;
    }

    /**
     * Generates a string representation of the Passenger object.
     *
     * @return A string representation of the Passenger object.
     */
    @Override
    public String toString() {
        return "Passenger{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", weight=" + weight +
                ", age=" + age +
                ", currentFloor=" + currentFloor +
                ", destinationFloor=" + destinationFloor +
                ", arrivalTime=" + arrivalTime +
                ", departureTime=" + departureTime +
                ", waitingTime=" + waitingTime +
                '}' + "\n";
    }

 }