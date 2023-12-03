import java.util.ArrayList;

public class Floor {
    private final ArrayList<Passenger> waitingPassengers;
    private final int FLOOR_NUMBER;
    private final String FLOOR_TYPE;
    private final CallButton callButton;

    // Constructor
    public Floor(int floorNumber, String floorType) {
        this.FLOOR_NUMBER = floorNumber;
        this.FLOOR_TYPE = floorType;
        this.waitingPassengers = new ArrayList<>();
        this.callButton = new CallButton();
    }

    // Method to add a passenger to the waiting list
    public void addPassenger(Passenger person) {
        waitingPassengers.add(person);
        // Notify the elevator system that the call button has been pressed
        callButton.pressButton();
        //ElevatorSystem.notifyFloorCall(this); FIXME
        System.out.println("Passenger added to waiting list at Floor " + FLOOR_NUMBER);
    }

    // Method to handle elevator arrival at the floor
    public void elevatorArrival(Elevator elevator) {
        // Let waiting passengers enter the elevator
        for (Passenger passenger : waitingPassengers) {
            if (elevator.loadPassenger(passenger)) waitingPassengers.remove(passenger); // If the passenger is loaded to the elevator, delete him from the list
        }
        elevatorDeparture();
    }

    // Method to handle elevator departure from the floor
    public void elevatorDeparture() {
        closeDoors();
        // Reset the call button after the elevator departs
        if (waitingPassengers.isEmpty()) callButton.resetButton(); // reset the button if there is no more waiting passengers
    }

    // Method to clear the waiting passengers list after they have entered the elevator
    private void clearWaitingPassengers() {
        waitingPassengers.clear();
        System.out.println("Waiting passengers cleared at Floor " + FLOOR_NUMBER);
    }

    // Method to open elevator doors
    private void openDoors() {
        // Additional logic for opening doors can be added here
        System.out.println("Doors open at Floor " + FLOOR_NUMBER);
    }

    // Method to close elevator doors
    private void closeDoors() {
        // Additional logic for closing doors can be added here
        System.out.println("Doors close at Floor " + FLOOR_NUMBER);
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

    // Inner class for CallButton
    public class CallButton {
        private boolean isPressed;

        // Constructor
        public CallButton() {
            this.isPressed = false;
        }

        // Getter method
        public boolean isPressed() {
            return isPressed;
        }

        // Method to press the call button
        public void pressButton() {
            isPressed = true;
            // Additional logic for handling the call button press can be added here
        }

        // Method to reset the call button
        public void resetButton() {
            isPressed = false;
            // Additional logic for handling the call button reset can be added here
        }
    }
}