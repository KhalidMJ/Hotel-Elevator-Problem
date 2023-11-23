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