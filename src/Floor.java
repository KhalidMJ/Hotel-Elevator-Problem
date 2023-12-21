import java.util.ArrayList;

public class Floor {
    private final ArrayList<Passenger> waitingPassengers;
    private final int FLOOR_NUMBER;
    private final String FLOOR_TYPE;
    private final CallButtons callButton;

    // Constructor
    public Floor(int floorNumber, String floorType) {
        this.FLOOR_NUMBER = floorNumber;
        this.FLOOR_TYPE = floorType;
        this.waitingPassengers = new ArrayList<>();
        this.callButton = new CallButtons(2);
    }

    // Method to add a passenger to the waiting list
    public synchronized void addPassenger(Passenger person) {
        waitingPassengers.add(person);
        // Notify the elevator system that the call button has been pressed
        if (person.getDestinationFloor() < FLOOR_NUMBER){
            person.requestDown();
        } else {
            person.requestUp();
        }
    }

    // Method to handle elevator arrival at the floor
    public void elevatorArrival(Elevator elevator) {
        // Taking a copy of the call button in case not all the passengers will enter the elevator, then resetting it
        boolean[] callButtonStatusCopy = callButton.getButtonsStatus().clone(); // shallow copy

        // Clearing the necessary buttons
        callButton.clearAllButtons();
        elevator.getCabButtons().clearButton(this.FLOOR_NUMBER);

        // Opening the elevator doors, and letting arrived passengers leave the elevator
        elevator.openDoors();
        elevator.unloadPassengers();

        // Let waiting passengers enter the elevator
        synchronized (this){
            elevator.loadPassengers(this.waitingPassengers);
            if (!this.waitingPassengers.isEmpty()) { // if not all the waiting passengers entered the elevator, restore the call button status
                callButton.setButtonsStatus(callButtonStatusCopy);
            }
            elevatorDeparture(elevator); // Depart the elevator
        }
    }

    // Method to handle elevator departure from the floor
    public void elevatorDeparture(Elevator elevator) {
        elevator.closeDoors();
        // Reset the call button after the elevator departs
        if (waitingPassengers.isEmpty()) callButton.clearAllButtons(); // reset the button if there is no more waiting passengers
    }

    // Method to clear the waiting passengers list after they have entered the elevator
    private synchronized void clearWaitingPassengers() {
        waitingPassengers.clear();
    }

    // Method to get the call button
    public CallButtons getCallButton() {
        return callButton;
    }

    // Method to get the number of waiting passengers
    public int getWaitingPassengersCount() {
        return waitingPassengers.size();
    }

    // Method to get the floor number
    public int getFloorNumber() {
        return FLOOR_NUMBER;
    }

    // Method to get the floor type
    public String getFloorType() {
        return FLOOR_TYPE;
    }
}