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
        this.doorsStatus = DoorsStatus.OPEN;
    }
    // Recursive Method to move the elevator to a requested floor
    public void moveTo(int level){
        // Base case: Elevator is already at the wanted level
        if (level == this.currentFloor) {
            pause();
            return;
        } else if (this.doorsStatus != DoorsStatus.CLOSED){
            closeDoors();
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

        System.out.print(this); // TODO delete this, was used for testing only
        moveTo(level);
    }

    // Method to pause the elevator and update its status
    public void pause(){
        this.elevatorStatus = ElevatorStatus.IDLE;
        System.out.print(this); // TODO delete this, was used for testing only
        openDoors();
    }

    // Method to open the doors of the elevator and update the doors status
    public void openDoors(){
        if (this.doorsStatus == DoorsStatus.OPEN) return; // if doors already open, do nothing
        this.doorsStatus = DoorsStatus.OPENING;
        System.out.print(this); // // TODO delete this, was used for testing only
        Simulation.delay(2); // It would take the doors 2 seconds to be fully open
        this.doorsStatus = DoorsStatus.OPEN;
        System.out.print(this); // // TODO delete this, was used for testing only
    }

    // Method to open the doors of the elevator and update the doors status
    public void  closeDoors(){
        if (this.doorsStatus == DoorsStatus.CLOSED) return; // if doors already closed, do nothing
        this.doorsStatus = DoorsStatus.CLOSING;
        System.out.print(this); // TODO delete this
        Simulation.delay(2); // It would take the doors 2 seconds to be fully closed
        this.doorsStatus = DoorsStatus.CLOSED;
        System.out.print(this); // TODO delete this
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

    public ElevatorStatus getElevatorStatus() {
        return elevatorStatus;
    }

    public void setElevatorStatus(ElevatorStatus elevatorStatus) {
        this.elevatorStatus = elevatorStatus;
    }

    public DoorsStatus getDoorsStatus() {
        return doorsStatus;
    }

    public void setDoorsStatus(DoorsStatus doorsStatus) {
        this.doorsStatus = doorsStatus;
    }

    @Override
    public String toString() {
        return "This elevator is at " + this.currentFloor + " and it is " + elevatorStatus + ", the doors are " + this.doorsStatus + "\r";
    }
}

// enums for the elevator status and elevator's doors status
enum ElevatorStatus {MOVING_UP, MOVING_DOWN, IDLE}

enum DoorsStatus {OPEN, CLOSED, OPENING, CLOSING}