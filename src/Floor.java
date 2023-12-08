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
            callButton.requestDown();
        } else {
            callButton.requestUp();
        }
    }

    // Method to handle elevator arrival at the floor
    public synchronized void elevatorArrival(Elevator elevator) {
        // Let arrived passengers leave the elevator
            elevator.unloadPassengers();

        // Let waiting passengers enter the elevator
        Iterator<Passenger> it2 = waitingPassengers.iterator();
        while (it2.hasNext()){
            Passenger passenger = it2.next();
            // Checking if loading the passengers is successful, meaning that there is enough capacity
            if (elevator.loadPassenger(passenger)) {
                passenger.enterElevator();
                it2.remove(); // If the passenger is loaded to the elevator, delete him from the list
            }
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
    public int getWaitingPassengers() {
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