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
        ElevatorSystem.notifyFloorCall(this);
        System.out.println("Passenger added to waiting list at Floor " + FLOOR_NUMBER);
    }

    // Method to handle elevator arrival at the floor
    public void elevatorArrival() {
        // Additional logic for handling elevator arrival can be added here
        System.out.println("Elevator has arrived at Floor " + FLOOR_NUMBER);
        openDoors();
        // Let waiting passengers enter the elevator
        for (Passenger passenger : waitingPassengers) {
            // Assuming there is a method in the Passenger class to enter the elevator
            passenger.enterElevator();
    }
        clearWaitingPassengers();
    }

    // Method to handle elevator departure from the floor
    public void elevatorDeparture() {
        System.out.println("Elevator departing from Floor " + FLOOR_NUMBER);
        closeDoors();
        // Reset the call button after the elevator departs
        callButton.resetButton();
    }

    // Method to clear the waiting passengers list after they have entered the elevator
    private void clearWaitingPassengers() {
        waitingPassengers.clear();
        System.out.println("Waiting passengers cleared at Floor " + FLOOR_NUMBER);
    }

    // Method to simulate the time taken for a passenger to press the call button
    public void simulatePassengerPressingButton() {
        // Simulate some delay before the passenger presses the button
        try {
            Thread.sleep(1000); // 1 second delay
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Press the call button after the delay
        addPassenger(new Passenger());
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