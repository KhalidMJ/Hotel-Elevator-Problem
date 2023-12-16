import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents an elevator in a hotel.
 *
 * @author Khalid
 */
public class Elevator {
    /**
     * The maximum capacity of the elevator in terms of the number of passengers it can hold.
     */
    private final int CAPACITY = 10;

    /**
     * The maximum weight capacity of the elevator in kilograms.
     */
    private final double MAX_WEIGHT = 700; // in kg

    /**
     * The speed of the elevator, indicating the time it takes to move between floors in seconds per floor.
     */
    private final int SPEED = 2; // seconds per floor

    /**
     * A reference to the hotel that this elevator belongs to.
     */
    private Hotel currentHotel; // A reference to the hotel that this elevator belong to

    /**
     * The current floor of the elevator.
     */
    private int currentFloor;

    /**
     * The list of passengers currently inside the elevator.
     */
    private ArrayList<Passenger> currentPassengers;

    /**
     * The CabButtons object associated with the elevator.
     */
    private CabButtons cabButtons;

    /**
     * The current status of the elevator. It can be either: (IDLE, MOVING_UP, MOVING_DOWN)
     */
    private ElevatorStatus elevatorStatus;

    /**
     * The current status of the elevator doors. It can be either: (CLOSED, CLOSING, OPENING, OPEN)
     */
    private DoorsStatus doorsStatus;

    /**
     * The total weight of the current passengers inside the elevator.
     */
    private double totalCurrentPassengersWeight;

    /**
     * Constructor for the Elevator class.
     */
    public Elevator(){
        this.currentFloor = 0;
        this.currentPassengers = new ArrayList<>();
        this.cabButtons = new CabButtons(8);
        this.elevatorStatus = ElevatorStatus.IDLE;
        this.doorsStatus = DoorsStatus.CLOSED;
    }

    /**
     * Recursive method to move the elevator to a requested floor.
     *
     * @param level The target floor.
     */
    public synchronized void moveTo(int level){
        // Base case: Elevator is already at the wanted level OR There is a passenger who want to get off on the current floor
        Floor servicedFloor = currentHotel.getFloors()[currentFloor];
        if (level == this.currentFloor) {
            pause();
            servicedFloor.elevatorArrival(this);
            return;
        }
        if (cabButtons.getButtonsStatus()[this.currentFloor] || currentHotel.getFloors()[this.currentFloor].getCallButton().isPressed()){ // Stop if the cab button or the call button of the current floor is clicked
            pause();
            servicedFloor.elevatorArrival(this);
        }

        // Assertions TODO: change it into an exception
        assert (level >= 0 && level <= 7) : "The elevator is bound to move between 0 and 7 floors only";
        assert this.doorsStatus == DoorsStatus.CLOSED : "The doors are not closed";

        // Check the target floor and start moving the elevator
        if (level > this.currentFloor) {
            this.elevatorStatus = ElevatorStatus.MOVING_UP;
            Simulation.delay(this.SPEED);
            this.currentFloor++;
        } else {
            this.elevatorStatus = ElevatorStatus.MOVING_DOWN;
            Simulation.delay(this.SPEED);
            this.currentFloor--;
        }
        moveTo(level); // Recursive call
    }

    /**
     * Method to pause the elevator and update its status.
     */
    public void pause(){
        this.elevatorStatus = ElevatorStatus.IDLE;
    }

    /**
     * Method to open the doors of the elevator and update the doors status.
     */
    public void openDoors(){
        if (this.doorsStatus == DoorsStatus.OPEN) return; // if doors already open, do nothing
        this.doorsStatus = DoorsStatus.OPENING;
        Simulation.delay(1); // It would take the doors 1 seconds to be fully open
        this.doorsStatus = DoorsStatus.OPEN;
    }

    /**
     * Method to close the doors of the elevator and update the doors status.
     */
    public void closeDoors(){
        if (this.doorsStatus == DoorsStatus.CLOSED) return; // if doors already closed, do nothing
        this.doorsStatus = DoorsStatus.CLOSING;
        Simulation.delay(1); // It would take the doors 1 seconds to be fully closed
        this.doorsStatus = DoorsStatus.CLOSED;
    }

    /**
     * Method to check if a passenger can be loaded onto the elevator.
     *
     * @param passenger The passenger to check.
     * @return True if loading is possible, false otherwise.
     */
    public boolean checkPassenger(Passenger passenger){
        if ((this.currentPassengers.size() < CAPACITY) && (passenger.getWeight() + this.totalCurrentPassengersWeight < MAX_WEIGHT)){
            return true;
        }
        return false;
    }

    /**
     * Method to unload passengers who have reached their destination floor.
     */
    public synchronized void unloadPassengers(){
        Iterator<Passenger> it = currentPassengers.iterator();
        while (it.hasNext()){
            Passenger passenger = it.next();
            if (passenger.getDestinationFloor() == currentFloor){
                passenger.exitElevator();
                totalCurrentPassengersWeight -= passenger.getWeight();
                it.remove();
            }
        }
    }

    /**
     * Method to load passengers onto the elevator.
     *
     * @param waitingPassengers The list of passengers waiting to board.
     */
    public synchronized void loadPassengers(ArrayList<Passenger> waitingPassengers) {
        ArrayList<Passenger> loadedPassengers = new ArrayList<>();
        for (Passenger passenger : waitingPassengers){
            if (checkPassenger(passenger)){
                passenger.enterElevator();
                currentPassengers.add(passenger);
                totalCurrentPassengersWeight += passenger.getWeight();
                this.cabButtons.pressButton(passenger.getDestinationFloor());
                loadedPassengers.add(passenger);
            }
        }
        waitingPassengers.removeAll(loadedPassengers);
    }

    /**
     * Method to start the elevator with the specified algorithm.
     *
     * @param algorithm The algorithm to use.
     */
    public void start(String algorithm){
        if (algorithm.equals("SCAN")){
            SCAN();
        }
    }

    /**
     * Method starting the SCAN algorithm where the elevator move between the top and the bottom floors and stops whenever there is a request
     */
    public void SCAN(){
        do {
            // Move to the top floor
            moveTo(this.currentHotel.getFloors().length - 1);
            // Move to the bottom floor
            moveTo(0);
        } while (!Simulation.isSimEnded());
    }


    // Setters/Getters -------------------------------------
    /**
     * Gets the maximum capacity of the elevator in terms of the number of passengers it can hold.
     *
     * @return The maximum capacity of the elevator.
     */
    public int getCAPACITY() {
        return CAPACITY;
    }

    /**
     * Gets the maximum weight capacity of the elevator.
     *
     * @return The maximum weight capacity of the elevator in kilograms.
     */
    public double getMAX_WEIGHT() {
        return MAX_WEIGHT;
    }

    /**
     * Gets the speed of the elevator, indicating the time it takes to move between floors.
     *
     * @return The speed of the elevator in seconds per floor.
     */
    public int getSPEED() {
        return SPEED;
    }

    /**
     * Gets the current floor of the elevator.
     *
     * @return The current floor of the elevator.
     */
    public int getCurrentFloor() {
        return currentFloor;
    }

    /**
     * Sets the current floor of the elevator.
     *
     * @param currentFloor The new current floor of the elevator.
     */
    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    /**
     * Gets the list of passengers currently inside the elevator.
     *
     * @return The list of passengers currently inside the elevator.
     */
    public ArrayList<Passenger> getCurrentPassengers(){
        return currentPassengers;
    }

    /**
     * Gets the current status of the elevator.
     *
     * @return The current status of the elevator.
     */
    public ElevatorStatus getElevatorStatus() {
        return elevatorStatus;
    }

    /**
     * Sets the elevator status.
     *
     * @param elevatorStatus The new status of the elevator.
     */
    public void setElevatorStatus(ElevatorStatus elevatorStatus) {
        this.elevatorStatus = elevatorStatus;
    }

    /**
     * Gets the current status of the elevator doors.
     *
     * @return The current status of the elevator doors.
     */
    public DoorsStatus getDoorsStatus() {
        return doorsStatus;
    }

    /**
     * Gets the CabButtons object associated with the elevator.
     *
     * @return The CabButtons object associated with the elevator.
     */
    public CabButtons getCabButtons() {
        return cabButtons;
    }

    /**
     * Sets the doors status of the elevator.
     *
     * @param doorsStatus The new status of the elevator doors.
     */
    public void setDoorsStatus(DoorsStatus doorsStatus) {
        this.doorsStatus = doorsStatus;
    }

    /**
     * Sets the current hotel reference for the elevator.
     *
     * @param hotel The hotel to which the elevator belongs.
     */
    public void setCurrentHotel(Hotel hotel){
        this.currentHotel = hotel;
    }

    @Override
    public String toString() {
        return "This elevator is at " + this.currentFloor + " and it is " + elevatorStatus + ", the doors are " + this.doorsStatus + "\r";
    }


}

/**
 * Enum representing the status of the elevator (MOVING_UP, MOVING_DOWN, IDLE).
 */
enum ElevatorStatus {MOVING_UP, MOVING_DOWN, IDLE}

/**
 * Enum representing the status of the elevator doors (OPEN, CLOSED, OPENING, CLOSING).
 */
enum DoorsStatus {OPEN, CLOSED, OPENING, CLOSING}

