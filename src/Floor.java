import java.util.ArrayList;
import java.util.Iterator;

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
            person.requestDown(this.callButton);
        } else {
            person.requestUp(this.callButton);
        }
    }

    // Method to handle elevator arrival at the floor
    public synchronized void elevatorArrival(Elevator elevator) {
        // Taking a copy of the call button in case not all the passengers will enter the elevator, then resetting it
        boolean[] callButtonStatus = callButton.getButtonsStatus().clone();
        callButton.clearAllButtons();
        elevator.getCabButtons().clearButton(this.FLOOR_NUMBER); // Clear the cab button of the current floor
        // Opening the elevator doors
        elevator.openDoors();

        // Let arrived passengers leave the elevator
        elevator.unloadPassengers();

        // Let waiting passengers enter the elevator
        elevator.loadPassengers(this.waitingPassengers);

        // if not all the waiting passengers entered the elevator, restore the call button status
        if (!this.waitingPassengers.isEmpty()) {
            callButton.setButtonsStatus(callButtonStatus);
        }
        elevatorDeparture(elevator);
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