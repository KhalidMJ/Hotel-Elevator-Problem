import java.util.ArrayList;

/**
 * Represents a floor in a building.
 */
public class Floor {
    /**
     * The list of passengers waiting on the floor.
     */
    private final ArrayList<Passenger> waitingPassengers;

    /**
     * The number of the floor.
     */
    private final int FLOOR_NUMBER;

    /**
     * The type of the floor.
     */
    private final String FLOOR_TYPE;

    /**
     * The call button for requesting elevators to this floor.
     */
    private final CallButtons callButton;

    /**
     * Constructs a new Floor instance with a specified floor number and floor type.
     *
     * @param floorNumber The number of the floor.
     * @param floorType   The type of the floor.
     */
    public Floor(int floorNumber, String floorType) {
        this.FLOOR_NUMBER = floorNumber;
        this.FLOOR_TYPE = floorType;
        this.waitingPassengers = new ArrayList<>();
        this.callButton = new CallButtons(2);
    }

    /**
     * Adds a passenger to the waiting list on the floor.
     *
     * @param person The passenger to add.
     */
    public synchronized void addPassenger(Passenger person) {
        waitingPassengers.add(person);
        // Notify the elevator system that the call button has been pressed
        if (person.getDestinationFloor() < FLOOR_NUMBER){
            person.requestDown(this.callButton);
        } else {
            person.requestUp(this.callButton);
        }
    }

    /**
     * Handles elevator arrival at the floor, unloading and loading passengers.
     *
     * @param elevator The elevator that arrived at the floor.
     */
    public void elevatorArrival(Elevator elevator) {
        // Taking a copy of the call button in case not all the passengers will enter the elevator, then resetting it
        boolean[] callButtonStatus = callButton.getButtonsStatus().clone();
        callButton.clearAllButtons();
        elevator.getCabButtons().clearButton(this.FLOOR_NUMBER); // Clear the cab button of the current floor

        // Opening the elevator doors, and letting arrived passengers leave the elevator
        elevator.openDoors();
        elevator.unloadPassengers();

        // Let waiting passengers enter the elevator
        synchronized (this){
            elevator.loadPassengers(this.waitingPassengers);
            // if not all the waiting passengers entered the elevator, restore the call button status
            if (!this.waitingPassengers.isEmpty()) {
                callButton.setButtonsStatus(callButtonStatus);
            }
            elevatorDeparture(elevator);
        }
    }

    /**
     * Handles elevator departure from the floor, closing doors and resetting the call button.
     *
     * @param elevator The elevator that departs from the floor.
     */
    public void elevatorDeparture(Elevator elevator) {
        elevator.closeDoors();
        // Reset the call button after the elevator departs
        if (waitingPassengers.isEmpty()) callButton.clearAllButtons(); // reset the button if there is no more waiting passengers
    }

    /**
     * Clears the waiting passengers list after they have entered the elevator.
     */
    private synchronized void clearWaitingPassengers() {
        waitingPassengers.clear();
    }

    /**
     * Gets the call button for requesting elevators to this floor.
     *
     * @return The call button for this floor.
     */
    public CallButtons getCallButton() {
        return callButton;
    }

    /**
     * Gets the number of waiting passengers on the floor.
     *
     * @return The number of waiting passengers.
     */
    public int getWaitingPassengersCount() {
        return waitingPassengers.size();
    }

    /**
     * Gets the number of the floor.
     *
     * @return The number of the floor.
     */
    public int getFloorNumber() {
        return FLOOR_NUMBER;
    }

    /**
     * Gets the type of the floor.
     *
     * @return The type of the floor.
     */
    public String getFloorType() {
        return FLOOR_TYPE;
    }
}