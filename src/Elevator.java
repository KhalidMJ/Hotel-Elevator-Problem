public class Elevator {
    private final int CAPACITY = 10;
    private final double MAX_WEIGHT = 700; // in kg
    private final int SPEED = 3; // seconds per floor

    private int currentFloor;
    private Passenger[] currentPassengers;
    private CabButtons cabButtons;

    private ElevatorStatus elevatorStatus;
    private DoorsStatus doorsStatus;

    public Elevator(){
        this.currentFloor = 0;
        this.currentPassengers = new Passenger[this.CAPACITY];
        this.cabButtons = new CabButtons();
        this.elevatorStatus = ElevatorStatus.IDLE;
        this.doorsStatus = DoorsStatus.CLOSED;

    }

    public void moveTo(int level){

    }
    public void pause(){

    }

    public void openDoors(){

    }

    public void  closeDoors(){

    }

    // Setters/Getters -------------------------------------


    public int getCAPACITY() {
        return CAPACITY;
    }

    public double getMAX_WEIGHT() {
        return MAX_WEIGHT;
    }

    public int getSPEED() {
        return SPEED;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public ElevatorStatus elevatorStatus() {
        return elevatorStatus;
    }

    public void setElevatorStatus(ElevatorStatus elevatorStatus) {
        this.elevatorStatus = elevatorStatus;
    }

    public DoorsStatus doorsStatus() {
        return doorsStatus;
    }

    public void setDoorsStatus(DoorsStatus doorsStatus) {
        this.doorsStatus = doorsStatus;
    }

    @Override
    public String toString() {
        return "This elevator is at " + this.currentFloor;
    }
}

// enums for the elevator status and elevator's doors status
enum ElevatorStatus {MOVING_UP, MOVING_DOWN, IDLE}

enum DoorsStatus {OPEN, CLOSED, OPENING, CLOSING}